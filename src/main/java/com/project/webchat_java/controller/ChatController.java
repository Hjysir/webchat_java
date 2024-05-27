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

    @PostMapping("/api/createchatroom/{userName}/{flag}")
    public RequestDto createChatRoom(@PathVariable("userName") String userName, @PathVariable("flag") String flag, @RequestBody CreateDto chatRoom) {
        return chatRoomService.createChatRoom(userName, flag, chatRoom.getChatname());
    }

    @PostMapping("/api/joinchatroom/{userName}")
    public RequestDto joinChatRoom(@PathVariable("userName") String username, @RequestBody CreateDto chatRoom) {
        return chatRoomService.addUserToChatRoom(username, chatRoom.getChatname());
    }

    @DeleteMapping("/api/leavechatroom/{userName}")
    public RequestDto leaveChatRoom(@PathVariable("userName") String username, @RequestBody CreateDto chatRoom) {
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

    @GetMapping("/api/gethistory/{chatName}")
    public List<Message> getHistory(@PathVariable("chatName") String chatName) {
        return chatRoomService.getHistory(chatName);
    }

    @GetMapping("/api/gethistory/{chatName}/{tag}")
    public List<Message> getHistoryByTag(@PathVariable("chatName") String chatName, @PathVariable("tag") String tag) {
        return chatRoomService.getHistoryByTag(chatName, tag);
    }

    @GetMapping("/api/getmessage/{userName}")
    public Message getMessage(@PathVariable("userName") String userName) {
        return chatRoomService.getMessageById(userName);
    }

}