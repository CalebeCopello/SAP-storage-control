package com.storagecontrol.backend.exception;

import java.util.Objects;
import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = Objects.requireNonNull(status, "HttpStatus must not be null");
    }

    public HttpStatus getStatus() {
        return status;
    }
}