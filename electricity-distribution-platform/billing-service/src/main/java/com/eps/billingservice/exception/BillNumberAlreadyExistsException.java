package com.eps.billingservice.exception;

public class BillNumberAlreadyExistsException extends RuntimeException {
    public BillNumberAlreadyExistsException(String message) {
        super(message);
    }
}
