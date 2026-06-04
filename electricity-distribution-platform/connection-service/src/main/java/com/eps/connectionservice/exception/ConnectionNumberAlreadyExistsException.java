package com.eps.connectionservice.exception;

public class ConnectionNumberAlreadyExistsException extends RuntimeException {
    public ConnectionNumberAlreadyExistsException(String message) {
        super(message);
    }
}
