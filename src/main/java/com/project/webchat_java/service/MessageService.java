package com.project.webchat_java.service;

import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.Message;
import com.project.webchat_java.mapper.ChatRoomMapper;
import com.project.webchat_java.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private MessageMapper messageMapper;
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    public MessageService(MessageMapper messageMapper, ChatRoomMapper chatRoomMapper) {
        this.messageMapper = messageMapper;
        this.chatRoomMapper = chatRoomMapper;
    }

    public List<Message> getMessagesInChatRoom(String chatRoomId) {
        return messageMapper.getMessagesByChatRoomId(chatRoomId);
    }

    public void sendMessage(String chatRoomName, Message message) {
        String chatRoomId = String.valueOf(chatRoomMapper.getChatRoomById(chatRoomName).getId());
        message.setChatRoomId(chatRoomId);
        messageMapper.insertMessage(message);
    }

}