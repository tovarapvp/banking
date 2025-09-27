package com.tovarapvp.banking.cuentas.service;

import com.tovarapvp.banking.common.events.MovimientoCreadoEvent;
import com.tovarapvp.banking.common.events.model.MovimientoCreadoPayload;
import com.tovarapvp.banking.cuentas.dto.MovimientoRequest;
import com.tovarapvp.banking.cuentas.dto.MovimientoResponse;
import com.tovarapvp.banking.cuentas.exception.NotFoundException;
import com.tovarapvp.banking.cuentas.exception.SaldoNoDisponibleException;
import com.tovarapvp.banking.cuentas.mapper.MovimientoMapper;
import com.tovarapvp.banking.cuentas.model.Cuenta;
import com.tovarapvp.banking.cuentas.model.Movimiento;
import com.tovarapvp.banking.cuentas.repository.CuentaRepository;
import com.tovarapvp.banking.cuentas.repository.MovimientoRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoService {
  private final CuentaRepository cuentas;
  private final MovimientoRepository movimientos;
  private final AmqpTemplate amqp;
  private final MovimientoMapper mapper;

  public Page<MovimientoResponse> findAll(Pageable pageable) {
    return movimientos.findAll(pageable).map(mapper::toResponse);
  }

  @Transactional
  public MovimientoResponse update(UUID id, MovimientoRequest request) {
    Movimiento movimiento = movimientos.findById(id).orElseThrow(() -> new NotFoundException("Movimiento no encontrado"));

    // Solo permitir actualizar la descripción para mantener la integridad de los saldos
    movimiento.setDescripcion(request.getDescripcion());

    return mapper.toResponse(movimientos.save(movimiento));
  }

  @Transactional
  public MovimientoResponse registrar(MovimientoRequest request) {
    Cuenta c =
            cuentas
                    .findById(request.getCuentaId())
                    .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

    BigDecimal nuevoSaldo = c.getSaldo().add(request.getValor());
    if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
      throw new SaldoNoDisponibleException("Saldo no disponible");
    }

    c.setSaldo(nuevoSaldo);
    cuentas.save(c);

    Movimiento m = mapper.toEntity(request);
    m.setSaldoResultante(nuevoSaldo);
    m.setTipoMovimiento(request.getValor().compareTo(BigDecimal.ZERO) > 0 ? "deposito" : "retiro");
    Movimiento saved = movimientos.save(m);

    MovimientoCreadoPayload payload =
            MovimientoCreadoPayload.builder()
                    .movimientoId(saved.getId())
                    .cuentaId(c.getId())
                    .valor(request.getValor())
                    .saldoResultante(nuevoSaldo)
                    .build();

    amqp.convertAndSend(
            "movimientos.exchange", "movimiento.creado", new MovimientoCreadoEvent(payload));

    return mapper.toResponse(saved);


  }
}