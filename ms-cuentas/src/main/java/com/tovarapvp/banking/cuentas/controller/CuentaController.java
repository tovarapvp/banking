package com.tovarapvp.banking.cuentas.controller;

import com.tovarapvp.banking.cuentas.dto.CuentaRequest;
import com.tovarapvp.banking.cuentas.dto.CuentaResponse;
import com.tovarapvp.banking.cuentas.exception.NotFoundException;
import com.tovarapvp.banking.cuentas.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {
  private final CuentaService service;

  @PostMapping
  public ResponseEntity<CuentaResponse> create(@Valid @RequestBody CuentaRequest c) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(c));
  }

  @GetMapping
  public Page<CuentaResponse> list(Pageable p) {
    return service.list(p);
  }

  @GetMapping("/{id}")
  public CuentaResponse get(@PathVariable UUID id) {
    return service.get(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
  }

  @PutMapping("/{id}")
  public CuentaResponse update(@PathVariable UUID id, @Valid @RequestBody CuentaRequest up) {
    return service.update(id, up);
  }
}
