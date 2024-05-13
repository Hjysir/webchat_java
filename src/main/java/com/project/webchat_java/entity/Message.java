package com.project.webchat_java.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Message {
    private String id;
    private String type;
    private String content;
    private String senderId;
    private String chatRoomId;
    private Timestamp timestamp;
    private String filename;
}