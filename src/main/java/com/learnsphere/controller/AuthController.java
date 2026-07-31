package com.learnsphere.controller;

import com.learnsphere.dto.LoginRequest;
import com.learnsphere.dto.LoginResponse;
import com.learnsphere.dto.UserRegistrationRequest;
import com.learnsphere.entity.User;
import com.learnsphere.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}