package com.spring_security.Spring_Security_Demo.service;

import com.spring_security.Spring_Security_Demo.dtos.AuthLoginResponse;
import com.spring_security.Spring_Security_Demo.dtos.RegisterRequest;
import com.spring_security.Spring_Security_Demo.dtos.RegisterResponse;
import com.spring_security.Spring_Security_Demo.entity.User;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest request);
    AuthLoginResponse loginUser(String email, String password);
}
