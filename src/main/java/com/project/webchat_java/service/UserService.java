package com.project.webchat_java.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webchat_java.component.SnowflakeIdWorker;
import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private UserMapper userMapper;
    private RedisService redisService;
    private CommenService commenService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper,
                       RedisService redisService,
                       CommenService commenService,
                       PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.redisService = redisService;
        this.commenService = commenService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean CheckOnPassWord(String input, String userPassWord) {
        return passwordEncoder.matches(input, userPassWord);
    }

    public RequestDto userRegister(User userinfo) {
        RequestDto requestDto = new RequestDto();

        log.info("用户尝试注册：{}", userinfo.getName());

        if (userMapper.getUserByUsername(userinfo.getName()) != null) {

            requestDto.setCode(400);
            requestDto.setMessage("用户名已存在");

        } else {
            userinfo.setPassword(String.valueOf(passwordEncoder.encode(userinfo.getPassword())));

            SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);

            userinfo.setId(String.valueOf(snowflakeIdWorker.nextId()));

            userMapper.insertUser(userinfo);

            requestDto.setCode(200);
            requestDto.setMessage("注册成功");

        }
        return requestDto;
    }

    public RequestDto userLogin(User userinfo) {
        // 返回的数据
        RequestDto requestDto = new RequestDto();

        User user = userMapper.getUserByUsername(userinfo.getName());

        // springboot日志输出
        log.info("用户尝试登录：{}", userinfo.getName());

        if (user == null) {
            // 检查邮箱是否存在
            user = userMapper.getUserByEmail(userinfo.getEmail());

            if (user == null) {
                requestDto.setCode(400);
                requestDto.setMessage("用户名不存在");
            }
        }
        else {
            String password = String.valueOf(user.getPassword());
            // 密码处理
            password = passwordEncoder.encode(password);

            System.out.println(password);
            System.out.println(user.getPassword());

            if (CheckOnPassWord(user.getPassword(), password)) {
                requestDto.setCode(200);
                requestDto.setMessage("登录成功");
                requestDto.setData(user);

                // 登录成功后，将用户信息存储到Redis中
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    String userJson = objectMapper.writeValueAsString(user);

                    redisService.set(user.getName(), userJson);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            else {
                requestDto.setCode(400);
                requestDto.setMessage("密码错误");
            }
        }

        return requestDto;
    }

    public RequestDto updateUserPassword(String password, String username) {
        String userId = commenService.getUserId(username);
        if (userId == null) {
            return new RequestDto().fail(500, "用户不存在", null);
        }
        userMapper.updateUserPassword(String.valueOf(password.hashCode()), userId);
        return new RequestDto().success();
    }

    public RequestDto updateUserAvatar(String avatar, String username) {
        String userId = commenService.getUserId(username);
        if (userId == null) {
            return new RequestDto().fail(500, "用户不存在", null);
        }
        userMapper.updateUserAvatar(avatar, userId);
        return new RequestDto().success();
    }

    public RequestDto userLogout(String username) {
        if (redisService.get(username) == null) {
            return new RequestDto().fail(500, "用户未登录", null);
        }
        redisService.delete(username);
        return new RequestDto().success();
    }

    public User getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}

