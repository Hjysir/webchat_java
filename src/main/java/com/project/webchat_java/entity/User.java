package com.project.webchat_java.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    @TableId
    private String id;
    private String name;
    private String password;
    private String email;
    private String avatar;
    private String phone;
}
