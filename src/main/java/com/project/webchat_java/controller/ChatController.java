package com.project.webchat_java.controller;

import com.project.webchat_java.dto.CreateDto;
import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.service.ChatRoomService;
import com.project.webchat_java.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @Autowired
    public ChatController(ChatRoomService chatRoomService, MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @PostMapping("/api/createchatroom/{userId}")
    public ChatRoom createChatRoom(@PathVariable("userId") String userId, @RequestBody CreateDto chatRoom) {
        return chatRoomService.createChatRoom(userId, chatRoom.getName());
    }

    @PostMapping("/api/joinchatroom/{username}")
    public ChatRoom joinChatRoom(@PathVariable("username") String username, @RequestBody CreateDto chatRoom) {
        return chatRoomService.addUserToChatRoom(username, chatRoom.getName());
    }

    @DeleteMapping("/api/leavechatroom/{username}")
    public RequestDto leaveChatRoom(@PathVariable("username") String username, @RequestBody CreateDto chatRoom) {
        return chatRoomService.removeUserFromChatRoom(username, chatRoom.getName());
    }

    @DeleteMapping("/api/deletechatroom/{chatRoomName}")
    public void deleteChatRoom(@PathVariable("chatRoomName") String chatRoomName) {
        chatRoomService.deleteChatRoom(chatRoomName);
    }

    @GetMapping("/api/getchatroom/{chatRoomName}")
    public ChatRoom getChatRoom(@PathVariable("chatRoomName") String chatRoomName) {
        return chatRoomService.getChatRoomByName(chatRoomName);
    }

    @GetMapping("/api/getchatlist/{userName}")
    public List<ChatRoom> getChatList(@PathVariable("userName") String userName) {
        return chatRoomService.getChatRoomsByUserId(userName);
    }

}