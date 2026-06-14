package com.storagecontrol.backend.auth.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storagecontrol.backend.auth.dto.UserRegisterRequest;
import com.storagecontrol.backend.auth.dto.UserRegisterResponse;
import com.storagecontrol.backend.auth.repository.UserRepository;
import com.storagecontrol.backend.domain.User;
import com.storagecontrol.backend.exception.BusinessException;


@Service
@Transactional(readOnly = true)

public class UserRegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRegisterResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return UserRegisterResponse.from(userRepository.save(user));
    }

    public List<UserRegisterResponse> findAll() {
        return userRepository.findAll()
            .stream()
            .map(UserRegisterResponse::from)
            .toList();
    }

    public UserRegisterResponse findById(UUID id) {
        return userRepository.findById(Objects.requireNonNull(id))
            .map(UserRegisterResponse::from)
            .orElseThrow(() -> new BusinessException("User not found: " + id, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void softDelete(UUID id) {
        User user = userRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new BusinessException("User not found: " + id, HttpStatus.NOT_FOUND));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
