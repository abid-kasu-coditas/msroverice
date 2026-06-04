package com.eps.meterservice.exception;

public class MeterSerialNumberAlreadyExistsException extends RuntimeException {
    public MeterSerialNumberAlreadyExistsException(String message) {
        super(message);
    }
}
