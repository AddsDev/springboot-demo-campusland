package com.campusland.springbootdemo.infrastructure.error;

import com.campusland.springbootdemo.infrastructure.util.error.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FieldError> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new FieldError("error", e.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<FieldError> handleCategoryNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<FieldError> handleTopicNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleFieldsException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors().stream()
                .map(field -> new FieldError(field.getField(), field.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(fieldErrors);
    }
}
