package com.learnsphere.dto;

import com.learnsphere.entity.Role;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String fullName;

    private String email;

    private String password;
    private Role role;

}