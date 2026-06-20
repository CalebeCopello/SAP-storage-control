package com.storagecontrol.backend.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.storagecontrol.backend.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    private final JwtProperties jwtProperties;
    private final SecretKey signinKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        this.signinKey = Keys.hmacShaKeyFor(
            jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String email) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date(now))
            .expiration(new Date(now + jwtProperties.expirationMs()))
            .signWith(signinKey)
            .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email) {
        try {
            String extracted = extractEmail(token);
            return extracted.equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(signinKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
