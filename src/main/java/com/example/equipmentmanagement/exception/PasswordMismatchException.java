package com.example.equipmentmanagement.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Password confirmation does not match new password");
    }
}
