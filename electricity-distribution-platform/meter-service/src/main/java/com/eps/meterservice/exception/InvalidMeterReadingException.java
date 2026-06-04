package com.eps.meterservice.exception;

public class InvalidMeterReadingException extends RuntimeException {
    public InvalidMeterReadingException(String message) {
        super(message);
    }
}
