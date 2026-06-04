package com.eps.meterservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MeterSerialNumberAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleMeterSerialNumberAlreadyExists(
        MeterSerialNumberAlreadyExistsException ex) {
        logger.error("Meter serial number already exists: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Meter Serial Number Already Exists", ex.getMessage());
    }

    @ExceptionHandler({MeterAccountNotFoundException.class, MeterReadingNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        logger.error("Meter resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Meter Resource Not Found", ex.getMessage());
    }

    @ExceptionHandler(InvalidMeterReadingException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidMeterReading(InvalidMeterReadingException ex) {
        logger.error("Invalid meter reading: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Meter Reading", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation error");
        Map<String, String> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .collect(Collectors.toMap(
                error -> ((FieldError) error).getField(),
                error -> error.getDefaultMessage()
            ));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        logger.error("Unexpected error", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, Object message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
