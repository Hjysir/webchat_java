package com.project.webchat_java.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class CommenService {
    private RedisService redisService;
    private UserMapper userMapper;

    @Autowired
    public CommenService(RedisService redisService, UserMapper userMapper) {
        this.redisService = redisService;
        this.userMapper = userMapper;
    }

    public String getUserIdFromRedis(String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("从redis中获取用户id" + username);
        String userJson = redisService.get(username);
        if (userJson != null) {
            try {
                User user = objectMapper.readValue(userJson, User.class);
                return user.getId();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null; // 没有找到
    }

    public String getUserId(String username) {
        if (username == null) {
            return null;
        }
        String userId = String.valueOf(getUserIdFromRedis(username));
        if (userId.equals("null")) {
            System.out.println("从数据库中获取用户id");
            User user = userMapper.getUserByUsername(username);
            if (Objects.nonNull(user)) {
                userId = user.getId();
            }
        }
        return userId;
    }
}
