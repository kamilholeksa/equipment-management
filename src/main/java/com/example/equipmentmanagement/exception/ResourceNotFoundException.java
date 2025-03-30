package com.example.equipmentmanagement.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entityName, Long id) {
        super(String.format("%s with id %d not found", entityName, id));
    }

    public ResourceNotFoundException(String entityName, String stringId) {
        super(String.format("%s with id %s not found", entityName, stringId));
    }
}
