package com.booking.application.service.impl;


import com.booking.application.dtos.request.LoginRequest;
import com.booking.application.dtos.request.RegisterRequest;
import com.booking.application.dtos.response.AuthResponse;
import com.booking.application.entity.User;
import com.booking.application.enums.Role;
import com.booking.application.exceptions.CustomException;
import com.booking.application.exceptions.UserAlreadyExistsException;
import com.booking.application.repository.UserRepository;
import com.booking.application.security.CustomUserDetails;
import com.booking.application.security.jwt.JwtTokenProvider;
import com.booking.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    // ================= REGISTER =================
    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new CustomException("User already exists with this email");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(userDetails);

        return new AuthResponse(
                token,
                userDetails.getUsername(),
                userDetails.getRole()
        );
    }
}



