package com.example.equipmentmanagement.controller.handlers;

import com.example.equipmentmanagement.exception.*;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidPasswordException.class,
            UserAlreadyExistsException.class,
            ValidationException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequestException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Map<String, Object>> buildResponseBodyWithStatus(Exception e, HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", e.getMessage());
        return ResponseEntity.status(httpStatus).body(body);
    }

}
