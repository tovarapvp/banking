package com.tovarapvp.banking.cuentas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrors {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> beanValidation(MethodArgumentNotValidException ex) {
    // Collectors.toMap crea un mapa a partir del stream
    Map<String, String> errors =
            ex.getBindingResult().getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            e -> e.getField(), // Clave del mapa
                            e -> e.getDefaultMessage() // Valor del mapa
                    ));

    return ResponseEntity.badRequest().body(Map.of("errors", errors));
  }
}