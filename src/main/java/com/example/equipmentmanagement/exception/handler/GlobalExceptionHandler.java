package com.example.equipmentmanagement.exception.handler;

import com.example.equipmentmanagement.dto.exception.ExceptionResponse;
import com.example.equipmentmanagement.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleNotFoundException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidPasswordException.class,
            UserAlreadyExistsException.class,
            BadRequestAlertException.class
    })
    public ResponseEntity<ExceptionResponse> handleBadRequestException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(RuntimeException e) {
        return buildResponseBodyWithStatus(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    private ResponseEntity<ExceptionResponse> buildResponseBodyWithStatus(Exception e, HttpStatus httpStatus) {
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(httpStatus.value());
        response.setError(httpStatus.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(httpStatus).body(response);
    }
}
