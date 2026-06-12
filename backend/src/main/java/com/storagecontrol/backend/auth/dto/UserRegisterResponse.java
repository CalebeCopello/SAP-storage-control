package com.storagecontrol.backend.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.storagecontrol.backend.domain.User;

public record UserRegisterResponse (
    UUID id,
    String name,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
