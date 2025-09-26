package com.tovarapvp.banking.clientes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrors {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> beanValidation(MethodArgumentNotValidException ex) {
    List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> Map.of("field", e.getField(), "message", e.getDefaultMessage()))
        .toList();
    return ResponseEntity.badRequest().body(Map.of("errors", errors));
  }
}
