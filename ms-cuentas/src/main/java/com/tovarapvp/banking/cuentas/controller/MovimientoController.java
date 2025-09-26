package com.tovarapvp.banking.cuentas.controller;

import com.tovarapvp.banking.cuentas.dto.MovimientoRequest;
import com.tovarapvp.banking.cuentas.dto.MovimientoResponse;
import com.tovarapvp.banking.cuentas.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {
  private final MovimientoService service;

  @PostMapping
  public ResponseEntity<MovimientoResponse> registrar(@RequestBody @Valid MovimientoRequest req) {
    MovimientoResponse m = service.registrar(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(m);
  }

  @PutMapping("/{id}")
  public MovimientoResponse update(@PathVariable UUID id, @RequestBody @Valid MovimientoRequest req) {
    return service.update(id, req);
  }

  @GetMapping
  public Page<MovimientoResponse> findAll(Pageable p) {
    return service.findAll(p);
  }
}


