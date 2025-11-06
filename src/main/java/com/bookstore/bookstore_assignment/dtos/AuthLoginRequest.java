package com.bookstore.bookstore_assignment.dtos;

import lombok.Data;

@Data
public class AuthLoginRequest {
    private String role;
    private String email;
    private String password;
}
