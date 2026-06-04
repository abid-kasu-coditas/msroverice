package com.eps.meterservice.exception;

public class MeterAccountAlreadyExistsException extends RuntimeException {
  public MeterAccountAlreadyExistsException(String message) {
    super(message);
  }
}
