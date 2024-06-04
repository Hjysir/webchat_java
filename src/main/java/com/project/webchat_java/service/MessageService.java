package com.project.webchat_java.service;

import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.Message;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.mapper.ChatRoomMapper;
import com.project.webchat_java.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private MessageMapper messageMapper;
    private ChatRoomMapper chatRoomMapper;
    private UserService userService;

    @Autowired
    public MessageService(MessageMapper messageMapper, ChatRoomMapper chatRoomMapper, UserService userService) {
        this.messageMapper = messageMapper;
        this.chatRoomMapper = chatRoomMapper;
        this.userService = userService;
    }

    public List<Message> getMessagesInChatRoom(String chatRoomName) {
        ChatRoom chatRoom = chatRoomMapper.getChatRoomByName(chatRoomName);
        if (chatRoom == null) {
            return null;
        }
        List<Message> messages = messageMapper.getMessagesByChatRoomId(chatRoom.getChatid());
        for (Message message : messages) {
            User user = userService.getUserById(message.getId());
            message.setChatid(chatRoom.getChatid());
            message.setId(user.getName());
        }
        return messages;
    }

    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
    }

    public List<Message> getMessagesInChatRoomByTag(String chatRoomName, String tag) {
        ChatRoom chatRoom = chatRoomMapper.getChatRoomByName(chatRoomName);
        if (chatRoom == null) {
            return null;
        }
        List<Message> messages = messageMapper.getMessagesByChatRoomIdAndTag(chatRoom.getChatid(), tag);
        for (Message message : messages) {
            User user = userService.getUserById(message.getId());
            message.setChatid(chatRoom.getChatid());
            message.setId(user.getName());
        }
        return messages;
    }

    public Message getMessageById(String UserId) {
        return messageMapper.getMessageByUserId(UserId);
    }
}