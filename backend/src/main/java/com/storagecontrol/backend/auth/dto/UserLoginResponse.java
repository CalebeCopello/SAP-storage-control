package com.storagecontrol.backend.auth.dto;

public record UserLoginResponse(
    String token,
    String type,
    String email
) {}
