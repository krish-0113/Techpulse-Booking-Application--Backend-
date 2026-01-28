package com.booking.application.security.jwt;

import com.booking.application.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
/**
 * 🔐 JWT Utility class
 * Responsible for:
 *  - Generating JWT token
 *  - Extracting data from token
 *  - Validating token
 */
@Component
public class JwtTokenProvider {

    // 🔑 Secret key used to sign JWT (from application.properties)
    @Value("${jwt.secret}")
    private String secret;

    // ⏳ Token validity time (milliseconds)
    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    /**
     * Converts secret string into SecretKey
     * Used internally by JWT library for signing & validation
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * ✅ Generates JWT token after successful OTP verification
     */
    public String generateToken(CustomUserDetails userDetails) {

        // Claims = custom data inside JWT payload
        Map<String, Object> claims = new HashMap<>();

        // Single role (USER)
        claims.put("role", userDetails.getRole());

        // Optional but useful
        claims.put("userId", userDetails.getUserId());

        // subject = email (used as username)
        return createToken(claims, userDetails.getUsername());
    }


    /**
     * Builds and signs the JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)                         // custom payload
                .setSubject(subject)                       // email
                .setIssuedAt(new Date())                   // token creation time
                .setExpiration(new Date(
                        System.currentTimeMillis() + jwtExpirationInMs)) // expiry
                .signWith(getSigningKey())                 // sign with secret key
                .compact();
    }

    /**
     * Extracts all claims (payload) from token
     */
    public Claims getAllClaimsFromToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // same key for validation
                .build()
                .parseClaimsJws(token)          // verifies signature
                .getBody();                     // returns payload
    }

    /**
     * Extracts email (subject) from JWT
     */
    public String extractEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Extracts role from JWT
     */
    public String extractRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    /**
     * Checks if token has expired
     */
    public boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * Validates JWT token
     */
    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token); // signature + expiry check
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
