package com.project.webchat_java.controller;

import com.project.webchat_java.entity.Message;
import com.project.webchat_java.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/api/sendmessage/{chatRoomName}")
    public void sendMessage(@PathVariable("chatRoomName") String chatRoomName, @RequestBody Message message) {
        messageService.sendMessage(chatRoomName, message);
    }

    @GetMapping("/api/getmessages/{usernmae}")
    public List<Message> getMessages(@PathVariable("username") String Username) {
        return messageService.getMessagesInChatRoom(Username);
    }
}
