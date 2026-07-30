package com.learnsphere.controller;

import com.learnsphere.dto.UserRegistrationRequest;
import com.learnsphere.entity.User;
import com.learnsphere.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody UserRegistrationRequest request) {

        return userService.registerUser(request);

    }

}