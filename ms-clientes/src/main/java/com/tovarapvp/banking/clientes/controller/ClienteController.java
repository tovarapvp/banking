package com.tovarapvp.banking.clientes.controller;

import com.tovarapvp.banking.clientes.dto.ClienteRequest;
import com.tovarapvp.banking.clientes.dto.ClienteResponse;
import com.tovarapvp.banking.clientes.exception.NotFoundException;
import com.tovarapvp.banking.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
  private final ClienteService service;

  @PostMapping
  public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest c) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(c));
  }

  @GetMapping
  public Page<ClienteResponse> list(Pageable p) {
    return service.list(p);
  }

  @GetMapping("/{id}")
  public ClienteResponse get(@PathVariable UUID id) {
    return service.get(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
  }

  @PutMapping("/{id}")
  public ClienteResponse update(@PathVariable UUID id, @Valid @RequestBody ClienteRequest c) {
    return service.update(id, c);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
