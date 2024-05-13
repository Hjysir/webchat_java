package com.project.webchat_java.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String userName;
    private String passWord;
    private String email;
    private String avatar;
    private boolean isOnline;
}
