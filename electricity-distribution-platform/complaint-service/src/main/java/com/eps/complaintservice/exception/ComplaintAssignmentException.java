package com.eps.complaintservice.exception;

public class ComplaintAssignmentException extends RuntimeException {
    public ComplaintAssignmentException(String message) {
        super(message);
    }

    public ComplaintAssignmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
