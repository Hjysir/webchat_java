package com.project.webchat_java.controller;


import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.dto.UserDto;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public RequestDto register(@RequestBody User Userinfo) {
        return userService.userRegister(Userinfo);
    }

    @PostMapping("/api/login")
    public RequestDto login(@RequestBody User Userinfo) {
        return userService.userLogin(Userinfo);
    }

    @PostMapping("/api/updateavatar/{username}")
    public void updateAvatar(@PathVariable("username") String username, @RequestBody String avatar) {
        userService.updateUserAvatar(avatar, username);
    }

    @PostMapping("/api/updatepassword/{username}")
    public void updatePassword(@PathVariable("username") String username, @RequestBody UserDto password) {
        userService.updateUserPassword(password.getNewPassword(), username);
    }

    @PostMapping("/api/logout/{username}")
    public void logout(@PathVariable("username") String username) {
        userService.userLogout(username);
    }

    @GetMapping("/api/getuser/{username}")
    public User getUser(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

}
