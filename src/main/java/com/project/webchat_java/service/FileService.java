package com.project.webchat_java.service;

import com.project.webchat_java.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final SimpMessagingTemplate brokerMessagingTemplate;
    private MessageService messageService;
    private ChatRoomService chatRoomService;
    private UserService userService;

    @Autowired
    public FileService(MessageService messageService, ChatRoomService chatRoomService, UserService userService, SimpMessagingTemplate brokerMessagingTemplate) {
        this.messageService = messageService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.brokerMessagingTemplate = brokerMessagingTemplate;
    }

    private static String UPLOADED_FOLDER = "D://WebChat_File_Location//";

    @Transactional
    public void saveUploadedFiles(String username, String chatname, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);

        // 把文件存储到数据库
        Message message = new Message();
        message.setURL(path.toString());
        message.setContent(file.getOriginalFilename());
        message.setMessagetype(1);
        message.setId(userService.getUserByUsername(username).getId());
        message.setChatid(chatRoomService.getChatRoomByName(chatname).getChatid());

        messageService.insertMessage(message);

        brokerMessagingTemplate.convertAndSend("/topic/" + chatname, message);
    }

}