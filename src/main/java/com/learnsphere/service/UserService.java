package com.learnsphere.service;

import com.learnsphere.dto.UserRegistrationRequest;
import com.learnsphere.entity.User;
import com.learnsphere.exception.EmailAlreadyExistsException;
import com.learnsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserRegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role("STUDENT")
                .enabled(true)
                .build();

        return userRepository.save(user);
    }
}