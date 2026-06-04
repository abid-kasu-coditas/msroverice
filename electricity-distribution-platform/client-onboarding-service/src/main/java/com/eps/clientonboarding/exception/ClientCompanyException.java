package com.eps.clientonboarding.exception;

public class ClientCompanyException extends RuntimeException {
  public ClientCompanyException(String message) {
    super(message);
  }

  public ClientCompanyException(String message, Throwable cause) {
    super(message, cause);
  }
}
