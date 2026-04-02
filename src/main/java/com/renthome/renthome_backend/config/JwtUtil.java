package com.renthome.renthome_backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "renthome_secret_key_12345678901234567890";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Long ownerId) {
        return Jwts.builder()
                .setSubject(String.valueOf(ownerId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key)
                .compact();
    }

    public Long extractOwnerId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("TOKEN SUBJECT: " + claims.getSubject());

            return Long.parseLong(claims.getSubject());

        } catch (Exception e) {
            System.out.println("JWT ERROR: " + e.getMessage()); // ✅ ADD THIS
            throw e;
        }

    }
}