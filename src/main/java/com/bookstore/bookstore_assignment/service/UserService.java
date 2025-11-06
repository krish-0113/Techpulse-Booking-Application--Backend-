package com.bookstore.bookstore_assignment.service;

import com.bookstore.bookstore_assignment.dtos.AuthLoginResponse;
import com.bookstore.bookstore_assignment.dtos.RegisterRequest;
import com.bookstore.bookstore_assignment.dtos.RegisterResponse;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest request);
    AuthLoginResponse loginUser(String email, String password);
}
