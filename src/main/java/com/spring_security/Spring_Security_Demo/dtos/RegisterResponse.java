package com.spring_security.Spring_Security_Demo.dtos;

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
