package com.project.webchat_java.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRoom {
    @TableId
    private String chatid;
    private String chatname;
    private String chattype;
    private String avatar;
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();


    public void addUser(User user) {
        this.users.add(user);
    }

}