package com.storagecontrol.backend.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storagecontrol.backend.auth.dto.UserRegisterRequest;
import com.storagecontrol.backend.auth.dto.UserRegisterResponse;
import com.storagecontrol.backend.auth.service.UserRegisterService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")

public class UserRegisterController {
    private final UserRegisterService userRegisterService;

    public UserRegisterController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserRegisterResponse response = userRegisterService.register(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
