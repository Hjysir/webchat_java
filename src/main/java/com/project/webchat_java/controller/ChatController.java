package com.project.webchat_java.controller;

import com.project.webchat_java.dto.CreateDto;
import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.Message;
import com.project.webchat_java.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/api/createchatroom/{userId}/{flag}")
    public ChatRoom createChatRoom(@PathVariable("userId") String userId, @PathVariable("flag") String flag, @RequestBody CreateDto chatRoom) {
        return chatRoomService.createChatRoom(userId, flag, chatRoom.getChatname());
    }

    @PostMapping("/api/joinchatroom/{username}")
    public ChatRoom joinChatRoom(@PathVariable("username") String username, @RequestBody CreateDto chatRoom) {
        return chatRoomService.addUserToChatRoom(username, chatRoom.getChatname());
    }

    @DeleteMapping("/api/leavechatroom/{username}")
    public RequestDto leaveChatRoom(@PathVariable("username") String username, @RequestBody CreateDto chatRoom) {
        return chatRoomService.removeUserFromChatRoom(username, chatRoom.getChatname());
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

    @GetMapping("/api/gethistory/{chatname}")
    public List<Message> getHistory(@PathVariable("chatname") String chatname) {
        return chatRoomService.getHistory(chatname);
    }

    @GetMapping("/api/gethistory/{chatname}/{tag}")
    public List<Message> getHistoryByTag(@PathVariable("chatname") String chatname, @PathVariable("tag") String tag) {
        return chatRoomService.getHistoryByTag(chatname, tag);
    }

}