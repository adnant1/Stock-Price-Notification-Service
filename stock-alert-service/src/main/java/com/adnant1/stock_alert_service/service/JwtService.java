package com.adnant1.stock_alert_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * This service class is responsible for generating JWT tokens for user authentication.
 */
@Service
public class JwtService {

    // You can move this to application.yml for more control
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 1 day expiration time
    private final long expirationMs = 86400000;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

}