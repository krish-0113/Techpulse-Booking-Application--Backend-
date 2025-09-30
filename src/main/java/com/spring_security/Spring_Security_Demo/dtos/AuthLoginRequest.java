package com.spring_security.Spring_Security_Demo.dtos;

import lombok.Data;

@Data
public class AuthLoginRequest {
    private String role;
    private String email;
    private String password;
}
