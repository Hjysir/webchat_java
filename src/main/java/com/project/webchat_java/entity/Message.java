package com.project.webchat_java.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class Message implements Serializable {
    private String id;
    private String chatid;
    private int messagetype;
    private String content;
    private Timestamp timestamp;
    private String URL;
}