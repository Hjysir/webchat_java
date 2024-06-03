package com.project.webchat_java.controller;

import cn.hutool.json.JSONUtil;
import cn.hutool.json.ObjectMapper;
import com.project.webchat_java.entity.Message;
import com.project.webchat_java.service.MessageService;
import com.project.webchat_java.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.ExecutorService;


@Slf4j
@Controller
public class WebSocketController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    private ExecutorService executorService;

    @MessageMapping("/ws/{chatroom}/{username}")
    @SendTo("/topic/{chatroom}")
    public void handleChatMessage(@DestinationVariable String chatroom,
                                  @DestinationVariable String username,
                                  @RequestBody Message message) {


        // 输出看看消息是什么
        log.info("chatroom = " + chatroom + "发到了" + chatroom + "的聊天室,订阅地址为/topic/" + chatroom + "内容为: " + username + ": " + message.getContent());

        // 发送消息给订阅了聊天室的所有用户
        messagingTemplate.convertAndSend("/topic/" + chatroom, username + ": " + message.getContent());



        if (message.getURL() == null) message.setURL("127.0.0.1");
        // 把Message对象转换为JSON字符串
        String msgJson = JSONUtil.toJsonStr(message);
        // 把消息存入Redis
        redisTemplate.opsForList().leftPush(chatroom, msgJson);

        // 把消息存入数据库
        message.setId(userService.getUserByUsername(username).getId());
        messageService.insertMessage(message);

        // 当redis内存大于128的时候，清空redis
        if (redisTemplate.opsForList().size(chatroom) > 128) {
            redisTemplate.delete(chatroom);
        }
    }
}
