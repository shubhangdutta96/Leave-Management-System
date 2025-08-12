package com.example.project.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
