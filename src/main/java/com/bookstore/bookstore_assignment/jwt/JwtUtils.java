package com.bookstore.bookstore_assignment.jwt;

import com.bookstore.bookstore_assignment.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {

    // 🔑 JWT secret (application.properties me set hota hai)
    @Value("${jwt.secret}")
    private String secret;

    // ⏳ JWT expiration time in milliseconds (properties se aata hai)
    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    // ✅ Helper method: String secret ko SecretKey me convert karna
    private SecretKey getSigningKey() {
        // 1️⃣ secret string ko UTF-8 bytes me convert kar rahe hain
        //     JWT HMAC algorithms ko bytes chahiye hoti hain signature generate karne ke liye
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        // 2️⃣ Keys.hmacShaKeyFor() → byte array se SecretKey object banata hai
        //     SecretKey JWT signing me use hoti hai
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        // 3️⃣ SecretKey return kar rahe hain taaki JWT token sign kar sake
        return secretKey;
    }


    // 1️⃣ Generate JWT Token
    public String generateToken(CustomUserDetails customUser) {
        HashMap<String, Object> claims = new HashMap<>();
        // Roles as String List
        claims.put("role", customUser.getRoleAsString());
        claims.put("userId",customUser.getUserId());

        return createToken(claims,customUser.getUsername());
    }

    //create token
    public String createToken(HashMap<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // username ya email
                .setIssuedAt(new Date(System.currentTimeMillis())) // abhi ka time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // abhi + expiry
                .signWith(getSigningKey()) // secret key se sign
                .compact();
    }




    // 2️⃣ Extract all claims from token
    public Claims getAllClaimsFromToken(String token) {
        // parserBuilder → token ko parse + validate karta hai
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // same secret key pass karna zaroori hai
                .build()
                .parseClaimsJws(token)          // signature check karega
                .getBody();                     // andar ka payload (claims) return karega
    }

    // 3️⃣ Extract username (subject) from token
    public String extractUsernamefromToken(String token) {
        return getAllClaimsFromToken(token).getSubject(); // subject = email
    }

    // 4️⃣ Extract role claim from token
    public String getRolefromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
        // .get("role", String.class) → "role" claim ko String type me cast karega
    }

    // 5️⃣ Extract expiration time
    public Date extractExpirationInMs(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    // 6️⃣ Check if token expired
    public Boolean isTokenExpired(String token) {
        return extractExpirationInMs(token).before(new Date());
        // agar expiry date current date se pehle hai → token expired
    }

    // 7️⃣ Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsernamefromToken(token); // token se username nikala
        // valid tab hoga jab:
        // (1) token ka username == db se load kiya gaya userDetails username
        // (2) token abhi expired na ho
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
