package com.bookstore.bookstore_assignment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse {
    private String email;
    private List<String> roles;
    private String message;
}
