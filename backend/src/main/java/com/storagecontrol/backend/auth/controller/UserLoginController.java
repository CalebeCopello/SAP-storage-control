package com.storagecontrol.backend.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storagecontrol.backend.auth.dto.UserLoginRequest;
import com.storagecontrol.backend.auth.dto.UserLoginResponse;
import com.storagecontrol.backend.auth.service.UserLoginService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")
public class UserLoginController {
    private final UserLoginService userLoginService;

    public UserLoginController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userLoginService.login(request);
        
        return ResponseEntity.ok(response);
    }
    
}
