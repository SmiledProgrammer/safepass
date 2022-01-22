package com.szinton.safepass.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return handleExceptionInternal(ex, BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpired(TokenExpiredException ex) {
        return handleExceptionInternal(ex, UNAUTHORIZED, "Token expired. Please log in again.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return handleExceptionInternal(ex, FORBIDDEN, "Access denied.");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        return handleExceptionInternal(ex, NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return handleExceptionInternal(ex, BAD_REQUEST, StringUtils.join(errorMessages, ";"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return handleExceptionInternal(ex, BAD_REQUEST, StringUtils.join(errorMessages, ";"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAnythingElse(Exception ex) {
        return handleExceptionInternal(ex, INTERNAL_SERVER_ERROR, "Server error occurred.");
    }

    private ResponseEntity<ApiError> handleExceptionInternal(Exception ex, HttpStatus status, String message) {
        log.error("{}: {}", ex, ex.getMessage());
        ApiError apiError = new ApiError(status, message);
        return new ResponseEntity<>(apiError, status);
    }
}
