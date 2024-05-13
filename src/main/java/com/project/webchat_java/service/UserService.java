package com.project.webchat_java.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserMapper userMapper;
    private RedisService redisService;
    private CommenService commenService;

    @Autowired
    public UserService(UserMapper userMapper, RedisService redisService, CommenService commenService) {
        this.userMapper = userMapper;
        this.redisService = redisService;
        this.commenService = commenService;
    }

    public RequestDto userRegister(User userinfo) {
        RequestDto requestDto = new RequestDto();
        System.out.println(userinfo.getUserName());
        System.out.println(userinfo.getPassWord().hashCode());
        if (userMapper.getUserByUsername(userinfo.getUserName()) != null) {
            requestDto.setCode(400);
            requestDto.setMessage("用户名已存在");
        } else {
            userinfo.setPassWord(String.valueOf(userinfo.getPassWord().hashCode()));
            userMapper.insertUser(userinfo);
            requestDto.setCode(200);
            requestDto.setMessage("注册成功");
        }
        return requestDto;
    }

    public RequestDto userLogin(User userinfo) {
        RequestDto requestDto = new RequestDto();
        System.out.println(userinfo.getUserName());
        User user = userMapper.getUserByUsername(userinfo.getUserName());
        User userByEmail = userMapper.getUserByEmail(userinfo.getEmail());
        System.out.println(userinfo.getPassWord().hashCode());
        if (user == null && userByEmail == null) {
            requestDto.setCode(400);
            requestDto.setMessage("用户名不存在");
        } else if (user != null && !String.valueOf(user.getPassWord()).equals(String.valueOf(userinfo.getPassWord().hashCode())) ||
                userByEmail != null && !String.valueOf(userByEmail.getPassWord()).equals(String.valueOf(userinfo.getPassWord().hashCode()))) {
            requestDto.setCode(400);
            requestDto.setMessage("密码错误");
        } else {
            requestDto.setCode(200);
            requestDto.setMessage("登录成功");
            requestDto.setData(user);
            if (user != null) {
                user.setOnline(true);
            }

            // 登录成功后，将用户信息存储到Redis中
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String userJson = objectMapper.writeValueAsString(user);
                if (user != null) {
                    redisService.set(user.getUserName(), userJson);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
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

    public void userLogout(String username) {
        userMapper.userLogout(username);
        redisService.delete(username);
    }

    public User getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}

