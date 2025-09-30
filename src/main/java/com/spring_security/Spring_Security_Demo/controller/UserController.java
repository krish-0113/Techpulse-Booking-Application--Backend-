package com.spring_security.Spring_Security_Demo.controller;

import com.spring_security.Spring_Security_Demo.dtos.AuthLoginRequest;
import com.spring_security.Spring_Security_Demo.dtos.AuthLoginResponse;
import com.spring_security.Spring_Security_Demo.dtos.RegisterRequest;
import com.spring_security.Spring_Security_Demo.dtos.RegisterResponse;
import com.spring_security.Spring_Security_Demo.entity.User;
import com.spring_security.Spring_Security_Demo.payload.ApiResponseSuccess;
import com.spring_security.Spring_Security_Demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Register user
    @PostMapping("/register")
    public ResponseEntity<ApiResponseSuccess<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request){
        RegisterResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseSuccess<>("User registered successfully!", response));
    }


    // Login user
    @PostMapping("/login")
    public ResponseEntity<ApiResponseSuccess<AuthLoginResponse>> login(@Valid @RequestBody AuthLoginRequest loginRequest){
        AuthLoginResponse response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseSuccess<>("Login successful!", response));
    }

    //for profile get
    @GetMapping("/profile/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("This is a protected USER profile endpoint");
    }


    @GetMapping("/profile/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getProfileOfAdmin() {
        return ResponseEntity.ok("This is a protected ADMIN profile endpoint");
    }

}

