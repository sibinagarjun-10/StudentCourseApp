package com.example.studentapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, String> errorBody(String error, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("error", error);
        body.put("message", message);
        return body;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleAppException(AppException ex) {
        HttpStatus status = switch (ex.getType()) {
            case CONFLICT            -> HttpStatus.CONFLICT;                // 409
            case NOT_FOUND           -> HttpStatus.NOT_FOUND;               // 404
            case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;            // 401
            case DB_ERROR            -> HttpStatus.INTERNAL_SERVER_ERROR;   // 500
        };
        return new ResponseEntity<>(errorBody(ex.getTitle(), ex.getMessage()), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("error", "Validation Failed");
        body.put("fields", fieldErrors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return new ResponseEntity<>(errorBody("Internal Server Error", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}