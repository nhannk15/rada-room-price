package com.radar.backend.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private String path;

    public ErrorResponse(int statusCode, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
        this.path = path;
    }

}
