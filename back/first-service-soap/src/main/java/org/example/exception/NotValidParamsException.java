package org.example.exception;

public class NotValidParamsException extends RuntimeException {
    public NotValidParamsException(String message) {
        super(message);
    }
}
