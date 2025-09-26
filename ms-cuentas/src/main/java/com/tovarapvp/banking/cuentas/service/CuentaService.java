package com.tovarapvp.banking.cuentas.service;

import com.tovarapvp.banking.cuentas.dto.CuentaRequest;
import com.tovarapvp.banking.cuentas.dto.CuentaResponse;
import com.tovarapvp.banking.cuentas.exception.NotFoundException;
import com.tovarapvp.banking.cuentas.mapper.CuentaMapper;
import com.tovarapvp.banking.cuentas.model.Cuenta;
import com.tovarapvp.banking.cuentas.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CuentaService {
  private final CuentaRepository repo;
  private final CuentaMapper mapper;

  public CuentaResponse create(CuentaRequest request) {
    Cuenta cuenta = mapper.toEntity(request);
    return mapper.toResponse(repo.save(cuenta));
  }

  public Page<CuentaResponse> list(Pageable p) {
    return repo.findAll(p).map(mapper::toResponse);
  }

  public Optional<CuentaResponse> get(UUID id) {
    return repo.findById(id).map(mapper::toResponse);
  }

  public CuentaResponse update(UUID id, CuentaRequest request) {
    Cuenta cuenta = repo.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
    mapper.updateEntity(request, cuenta);
    return mapper.toResponse(repo.save(cuenta));
  }
}
