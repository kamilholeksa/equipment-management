package com.example.equipmentmanagement.controller;

import lombok.Getter;

@Getter
public class ResponseMessage {
    private final String message;

    public static ResponseMessage text(String message) {
        return new ResponseMessage(message);
    }

    public ResponseMessage(String message) {
        this.message = message;
    }
}
