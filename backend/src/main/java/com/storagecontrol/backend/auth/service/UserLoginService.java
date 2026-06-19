package com.storagecontrol.backend.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storagecontrol.backend.auth.dto.UserLoginRequest;
import com.storagecontrol.backend.auth.dto.UserLoginResponse;
import com.storagecontrol.backend.auth.repository.UserRepository;
import com.storagecontrol.backend.exception.BusinessException;
import com.storagecontrol.backend.domain.User;

@Service
@Transactional(readOnly = true)
public class UserLoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserLoginService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new BusinessException("Invalid credential", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.password(),user.getPassword())) {
            throw new BusinessException("Invalid credential", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user.getEmail());

        return new UserLoginResponse(token, "Bearer", user.getEmail());
    }
    
}
