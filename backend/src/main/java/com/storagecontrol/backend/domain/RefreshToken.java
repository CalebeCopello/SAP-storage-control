package com.storagecontrol.backend.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(nullable = true)
    private String location;

    @Column(name = "user_agent", nullable = true)
    private String userAgent;

    @Column(nullable = true)
    private String browser;
    
    @Column(nullable = true)
    private String os;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected RefreshToken() {}

    public RefreshToken(User user, String tokenHash, String ipAddress, String location,
                        String userAgent, String browser, String os, LocalDateTime expiresAt) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.ipAddress = ipAddress;
        this.location = location;
        this.userAgent = userAgent;
        this.browser = browser;
        this.os = os;
        this.expiresAt = expiresAt;
    }

}
