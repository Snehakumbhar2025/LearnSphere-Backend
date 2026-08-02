package com.learnsphere.controller;

import com.learnsphere.dto.LoginRequest;
import com.learnsphere.dto.LoginResponse;
import com.learnsphere.dto.UserRegistrationRequest;
import com.learnsphere.entity.User;
import com.learnsphere.repository.UserRepository;
import com.learnsphere.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    // ================= REGISTER =================

    @PostMapping("/register")
    public User register(@RequestBody UserRegistrationRequest request) {

        return userService.registerUser(request);
    }


    // ================= LOGIN =================

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return userService.login(request);
    }


    // ================= CURRENT USER =================

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();

        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return response;
    }
}