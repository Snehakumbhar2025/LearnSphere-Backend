package com.learnsphere.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String fullName;

    private String email;

    private String password;

}