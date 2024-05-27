package com.project.webchat_java.controller;


import com.project.webchat_java.dto.RequestDto;
import com.project.webchat_java.dto.UserDto;
import com.project.webchat_java.entity.User;
import com.project.webchat_java.service.UserService;
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
    public RequestDto updateAvatar(@PathVariable("username") String username, @RequestBody String avatar) {
        return userService.updateUserAvatar(avatar, username);
    }

    @PostMapping("/api/updatepassword/{username}")
    public RequestDto updatePassword(@PathVariable("username") String username, @RequestBody UserDto password) {
        return userService.updateUserPassword(password.getPassword(), username);
    }

    @PostMapping("/api/logout/{username}")
    public RequestDto logout(@PathVariable("username") String username) {
        return userService.userLogout(username);
    }

    @GetMapping("/api/getuser/{username}")
    public User getUser(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

}
