package com.bookstore.bookstore_assignment.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private List<String> roles;  // optional, default ROLE_USER
}
