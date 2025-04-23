package com.example.equipmentmanagement.exception.handler;

import com.example.equipmentmanagement.dto.exception.ExceptionResponse;
import com.example.equipmentmanagement.exception.*;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

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
            ValidationException.class,
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception e) {
        return buildResponseBodyWithStatus(e, HttpStatus.INTERNAL_SERVER_ERROR);
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
