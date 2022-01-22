package com.szinton.safepass.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {
    private final int status;
    private final String error;
    private final String message;

    public ApiError(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }
}
