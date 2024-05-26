package com.project.webchat_java.service;

import com.project.webchat_java.entity.Message;
import com.project.webchat_java.mapper.ChatRoomMapper;
import com.project.webchat_java.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageMapper messageMapper, ChatRoomMapper chatRoomMapper) {
        this.messageMapper = messageMapper;
    }

    public List<Message> getMessagesInChatRoom(String chatRoomId) {
        return messageMapper.getMessagesByChatRoomId(chatRoomId);
    }

    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
    }

}