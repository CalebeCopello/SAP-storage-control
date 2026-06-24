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


    @Column(name = "token_hash", nullable = false, unique = true)
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

    @Column(nullable = false)
    private boolean revoked = false;

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    //Gets and setters

    public UUID getId() { return id;}
    public User getUser() {return user;}
    public String getTokenHash() {return tokenHash;}
    public String getIpAddress() { return ipAddress;}
    public String getLocation() {return location;}
    public String getUserAgent() { return userAgent;}
    public String getBrowser() { return browser;}
    public String getOs() { return os;}
    public LocalDateTime getExpiresAt() {return expiresAt;}
    public boolean isRevoked() {return revoked;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}

    public void setLocation(String location) {
        this.location = location;
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }
}
