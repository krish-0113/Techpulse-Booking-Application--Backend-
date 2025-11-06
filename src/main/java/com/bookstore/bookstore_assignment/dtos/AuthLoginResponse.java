package com.bookstore.bookstore_assignment.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthLoginResponse {
    private String token;
    private String email;
    private List<String> roles;
}
