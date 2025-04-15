package com.example.equipmentmanagement.dto.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionResponse {
    private int status;
    private String error;
    private LocalDateTime timestamp;
    private String message;
}
