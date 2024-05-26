package com.project.webchat_java.controller;

import com.project.webchat_java.entity.Message;
import com.project.webchat_java.service.MessageService;
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

import java.util.List;
import java.util.concurrent.ExecutorService;


@Slf4j
@Controller
public class WebSocketController {

    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private StringRedisTemplate stringRedisTemplate;

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    private ExecutorService executorService;

    @MessageMapping("/ws/{chatroom}/{username}")
    @SendTo("/topic/{chatroom}")
    public void handleChatMessage(@DestinationVariable String chatroom,
                                  @DestinationVariable String username,
                                  Message message) {

        // 发送消息给订阅了聊天室的所有用户
        messagingTemplate.convertAndSend("/topic/" + chatroom, username + ": " + message);

        // 将消息存储到Redis中
        redisTemplate.opsForList().rightPush(chatroom, message);
        // 如果消息数量超过128条，将消息存储到MySQL中
        if (redisTemplate.opsForList().size(chatroom) >= 128) {
            List<Message> messages = redisTemplate.opsForList().range(chatroom, 0, -1);
            // 存储到MySQL的逻辑

            // 异步存储到MySQL, 避免阻塞WebSocket线程
            executorService.submit(() -> {
                for (Message msg : messages) {
                    messageService.insertMessage(msg);
                }
            });

            // 清空Redis中的列表
            redisTemplate.delete(chatroom);
        }

        log.info("chatroom = " + chatroom + "发到了" + chatroom + "的聊天室,订阅地址为/topic/" + chatroom + "内容为: " + username + ": " + message);
    }
}
