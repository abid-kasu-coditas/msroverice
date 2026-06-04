package com.eps.customerservice.exception;

public class CustomerBlockedException extends RuntimeException {
    public CustomerBlockedException(String message) {
        super(message);
    }
}
