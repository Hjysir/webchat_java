package com.project.webchat_java.service;

import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.Message;
import com.project.webchat_java.mapper.ChatRoomMapper;
import com.project.webchat_java.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Message> getMessagesInChatRoom(String chatRoomName) {
        ChatRoom chatRoom = chatRoomMapper.getChatRoomByName(chatRoomName);
        if (chatRoom == null) {
            return null;
        }
        return messageMapper.getMessagesByChatRoomId(chatRoom.getChatid());
    }

    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
    }

    public List<Message> getMessagesInChatRoomByTag(String chatRoomName, String tag) {
        ChatRoom chatRoom = chatRoomMapper.getChatRoomByName(chatRoomName);
        if (chatRoom == null) {
            return null;
        }
        return messageMapper.getMessagesByChatRoomIdAndTag(chatRoom.getChatid(), tag);
    }

    public Message getMessageById(String UserId) {
        return messageMapper.getMessageByUserId(UserId);
    }
}