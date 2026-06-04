package com.eps.meterservice.exception;

public class MeterAccountNotFoundException extends RuntimeException {
    public MeterAccountNotFoundException(String message) {
        super(message);
    }
}
