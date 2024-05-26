package com.project.webchat_java.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webchat_java.component.SnowflakeIdWorker;
import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private UserMapper userMapper;
    private RedisService redisService;
    private CommenService commenService;

    @Autowired
    public UserService(UserMapper userMapper,
                       RedisService redisService,
                       CommenService commenService) {
        this.userMapper = userMapper;
        this.redisService = redisService;
        this.commenService = commenService;
    }

    public boolean CheckOnPassWord(String input, String userPassWord) {
        return String.valueOf(input.hashCode()).equals(userPassWord);
    }

    public RequestDto userRegister(User userinfo) {
        RequestDto requestDto = new RequestDto();

        log.info("用户尝试注册：{}", userinfo.getName());

        if (userMapper.getUserByUsername(userinfo.getName()) != null) {

            requestDto.setCode(400);
            requestDto.setMessage("用户名已存在");

        } else {

            userinfo.setPassword(String.valueOf(DigestUtil.md5Hex(userinfo.getPassword())));

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

            if (CheckOnPassWord(userinfo.getPassword(), password)) {
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

    public void updateUserPassword(String password, String username) {
        String userId = commenService.getUserId(username);
        userMapper.updateUserPassword(String.valueOf(password.hashCode()), userId);
    }

    public void updateUserAvatar(String avatar, String username) {
        String userId = commenService.getUserId(username);
        userMapper.updateUserAvatar(avatar, userId);
    }

    public void userLogout(String username) { redisService.delete(username); }

    public User getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}

