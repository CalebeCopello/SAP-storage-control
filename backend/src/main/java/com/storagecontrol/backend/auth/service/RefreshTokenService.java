package com.storagecontrol.backend.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storagecontrol.backend.auth.repository.RefreshTokenRepository;
import com.storagecontrol.backend.config.JwtProperties;
import com.storagecontrol.backend.domain.RefreshToken;
import com.storagecontrol.backend.domain.User;

@Service
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProperties = jwtProperties;
    }

    public String createRefreshToken(User user, String ipAddress, String location, String userAgent, String browser, String os) {
        String tokenHash = null;
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder().encodeToString(randomBytes);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
    
            tokenHash = HexFormat.of().formatHex(hashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }

        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtProperties.refreshExpirationMs() / 1000);

        RefreshToken refreshToken = new RefreshToken(user, tokenHash, ipAddress, location, userAgent, browser, os, expiresAt);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }
}
