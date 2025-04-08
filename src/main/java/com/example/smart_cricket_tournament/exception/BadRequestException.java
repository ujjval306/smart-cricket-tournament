package com.example.smart_cricket_tournament.exception;

public class BadRequestException  extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
