
package com.example.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound(
            StudentNotFoundException exception) {

        Map<String, String> response = new HashMap<>();
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

@ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
public ResponseEntity<Map<String, String>> handleConstraintViolations(
        jakarta.validation.ConstraintViolationException exception) {

    Map<String, String> errors = new HashMap<>();

    exception.getConstraintViolations().forEach(violation ->
        errors.put(
            violation.getPropertyPath().toString(),
            violation.getMessage()
        )
    );

    return ResponseEntity.badRequest().body(errors);
}
}

