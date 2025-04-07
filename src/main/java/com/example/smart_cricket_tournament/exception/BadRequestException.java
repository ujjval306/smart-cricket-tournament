package com.example.smart_cricket_tournament.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException  extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    public BadRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
