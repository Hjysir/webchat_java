package com.project.webchat_java.service;

import com.project.webchat_java.component.SnowflakeIdWorker;
import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.Message;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.mapper.ChatRoomMapper;
import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChatRoomService {

    private ChatRoomMapper chatRoomMapper;
    private MessageService messageService;
    private CommenService commenService;
    private UserService userService;

    private void setChatRooms(List<ChatRoom> chatRooms) {
        if (chatRooms.isEmpty()) {
            return;
        }
        for (ChatRoom chatRoom : chatRooms) {
            initUsersAndMessages(chatRoom);
        }
    }

    private void setChatRoom(ChatRoom chatRoom) {
        if (chatRoom == null) {
            return;
        }
        initUsersAndMessages(chatRoom);
    }

    private void initUsersAndMessages(ChatRoom chatRoom) {
        log.info("initUsersAndMessages:" + chatRoom.getChatname());
        List<String> userIds = chatRoomMapper.getUsersByChatRoomId(chatRoom.getChatid());
        for (String userId : userIds) {
            User user = userService.getUserById(userId);
            chatRoom.addUser(user);
        }
        chatRoom.setMessages(messageService.getMessagesInChatRoom(chatRoom.getChatid()));
    }

    @Autowired
    public ChatRoomService(ChatRoomMapper chatRoomMapper,
                           MessageService messageService,
                           CommenService commenService,
                           UserService userService) {
        this.chatRoomMapper = chatRoomMapper;
        this.messageService = messageService;
        this.commenService = commenService;
        this.userService = userService;
    }

    public ChatRoom createChatRoom(String username, String flag, String chatRoomName) {
        log.info("createChatRoom:" + chatRoomName + "with user:" + username);
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
        long chatRoomId = snowflakeIdWorker.nextId();

        System.out.println("chatRoomId:" + chatRoomId);
        System.out.println("chatRoomName:" + chatRoomName);

        chatRoomMapper.CreateChatRoom(String.valueOf(chatRoomId), chatRoomName, flag);

        ChatRoom chatRoom = chatRoom = chatRoomMapper.getChatRoomByName(chatRoomName);
        String userId = commenService.getUserId(username);; // 获取用户id
        chatRoomMapper.addUserToChatRoom(userId, chatRoom.getChatid());
        setChatRoom(chatRoom);

        return chatRoom;
    }

    public ChatRoom addUserToChatRoom(String user, String chatRoomName) {
        System.out.println("addUserToChatRoom:" + chatRoomName + "with user:" + user);
        String chatRoomId = chatRoomMapper.getChatRoomByName(chatRoomName).getChatid();

        if (chatRoomId == null) {
            return null;
        }

        String userId = commenService.getUserId(user); // 获取用户id
        if (chatRoomMapper.getUsersByChatRoomId(chatRoomId).contains(userId)) {
            return null;
        }

        chatRoomMapper.addUserToChatRoom(userId, chatRoomId);
        ChatRoom chatRoom = chatRoomMapper.getChatRoomById(chatRoomId);
        setChatRoom(chatRoom);

        return chatRoom;
    }

    @Transactional
    public RequestDto deleteChatRoom(String chatRoomName) {
        log.info("deleteChatRoom:" + chatRoomName);
        String chatRoomId = String.valueOf(chatRoomMapper.getChatRoomById(chatRoomName).getChatid());

        chatRoomMapper.deleteChatRoomRelatedById(chatRoomId);
        chatRoomMapper.deleteChatRoomById(chatRoomId);
        RequestDto requestDto = new RequestDto();

        return requestDto.success();
    }

    public RequestDto removeUserFromChatRoom(String user, String chatRoomName) {
        log.info("removeUserFromChatRoom:" + chatRoomName + "with user:" + user);

        String chatRoomId = String.valueOf(chatRoomMapper.getChatRoomById(chatRoomName).getChatid());
        if (chatRoomId == null) {
            return new RequestDto().fail(500, "聊天室不存在", null);
        }
        String userId = commenService.getUserId(user); // 获取用户id

        if (chatRoomMapper.getUsersByChatRoomId(chatRoomId).contains(userId)) {
            chatRoomMapper.deleteUserFromChatRoom(chatRoomId, userId);
            return new RequestDto().success();
        } else {
            return new RequestDto().fail(500, "用户不在聊天室中", null);
        }
    }

    public ChatRoom getChatRoomByName(String chatRoomName) {
        log.info("getChatRoomByName:" + chatRoomName);

        ChatRoom chatRoom = chatRoomMapper.getChatRoomByName(chatRoomName);
        if (chatRoom == null) {
            return null;
        }
        setChatRoom(chatRoom);

        return chatRoom;
    }

    public List<ChatRoom> getChatRoomsByUserId(String userName) {
        log.info("getChatRoomsByUserId:" + userName);

        String userId = commenService.getUserId(userName); // 获取用户id

        return getChatRooms(userId);
    }

    @Nullable
    private List<ChatRoom> getChatRooms(String userId) {
        List<String> chatRoomIds = chatRoomMapper.getChatRoomsByUserId(userId);
        List<ChatRoom> chatRooms = new ArrayList<>(); // 初始化为一个空列表

        if (!chatRoomIds.isEmpty()) {
            for (String chatRoomId : chatRoomIds) {
                ChatRoom chatRoom = chatRoomMapper.getChatRoomById(chatRoomId);
                chatRooms.add(chatRoom);
            }
            setChatRooms(chatRooms);
        }

        return chatRooms;
    }

    public List<Message> getHistory(String chatname) {
        return messageService.getMessagesInChatRoom(chatname);
    }

    public List<Message> getHistoryByTag(String chatname, String tag) {
        return messageService.getMessagesInChatRoomByTag(chatname, tag);
    }
}