package com.project.webchat_java.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ws/{chatroom}/{username}")
    @SendTo("/topic/{chatroom}")
    public void handleChatMessage(@DestinationVariable String chatroom,
                                  @DestinationVariable String username,
                                  String message) {
        System.out.println("chatroom = " + chatroom);
        // 发送消息给订阅了聊天室的所有用户
        messagingTemplate.convertAndSend("/topic/" + chatroom, username + ": " + message);
        log.info("发到了" + chatroom + "的聊天室,订阅地址为/topic/" + chatroom);
        log.info("内容为: " + username + ": " + message);
    }

    @MessageMapping("/ws/{username}")
    @SendTo("/user/{username}/queue/private")
    public void handlePrivateMessage(@DestinationVariable String username,
                                     String message) {
        System.out.println("username = " + username);
        messagingTemplate.convertAndSend("/user/" + username + "/queue/private", message);
        System.out.println("发到了" + username + "的私信,订阅地址为/user/" + username + "/queue/private");
    }

}
