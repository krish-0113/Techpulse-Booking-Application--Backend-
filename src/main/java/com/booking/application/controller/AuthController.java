package com.booking.application.controller;


import com.booking.application.dtos.request.LoginRequest;
import com.booking.application.dtos.request.RegisterRequest;
import com.booking.application.dtos.response.AuthResponse;
import com.booking.application.service.AuthService;
//import com.assignment.fullstack_assignment.service.RefreshTokenService;
import com.booking.application.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication related APIs:
 * Register, Login, OTP verification, Forgot/Reset password
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ============ REGISTER ============
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully", null)
        );
    }

    // ============ LOGIN ============
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", response)
        );
    }
}
