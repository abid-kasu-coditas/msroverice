package com.eps.meterservice.exception;

public class MeterReadingNotFoundException extends RuntimeException {
    public MeterReadingNotFoundException(String message) {
        super(message);
    }
}
