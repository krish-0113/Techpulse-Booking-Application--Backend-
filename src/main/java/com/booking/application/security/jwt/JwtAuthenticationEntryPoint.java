package com.booking.application.security.jwt;

import com.booking.application.exceptions.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 🔐 Handles cases where user is not authenticated:
 *  - Missing token
 *  - Invalid or expired token
 *  - Incorrect token format
 *
 *  Always returns a clean JSON response with status 401.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String authHeader = request.getHeader("Authorization");
        String message;
        String suggestion;

        if (authHeader == null) {
            message = "Authentication token is missing.";
            suggestion = "Please log in and include your JWT token in the 'Authorization' header.";
        } else if (!authHeader.startsWith("Bearer ")) {
            message = "Invalid Authorization header format.";
            suggestion = "Expected header format: 'Authorization: Bearer <your_jwt_token>'.";
        } else {
            message = "Invalid or expired authentication token.";
            suggestion = "Please generate a new token by logging in again.";
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized Access")
                .message(message + " " + suggestion)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().toString())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
