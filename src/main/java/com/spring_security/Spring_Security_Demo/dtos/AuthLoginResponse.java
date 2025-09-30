package com.spring_security.Spring_Security_Demo.dtos;


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
