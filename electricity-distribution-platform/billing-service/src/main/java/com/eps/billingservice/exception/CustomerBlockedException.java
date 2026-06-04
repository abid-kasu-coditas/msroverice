package com.eps.billingservice.exception;

public class CustomerBlockedException extends RuntimeException {
    public CustomerBlockedException(String message) {
        super(message);
    }
}
