package com.booking.application.service;

import com.booking.application.dtos.request.LoginRequest;
import com.booking.application.dtos.request.RegisterRequest;
import com.booking.application.dtos.response.AuthResponse;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}



