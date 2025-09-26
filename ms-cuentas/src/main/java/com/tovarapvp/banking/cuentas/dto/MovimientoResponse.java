package com.tovarapvp.banking.cuentas.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class MovimientoResponse {
  private UUID id;
  private UUID cuentaId;
  private Instant fecha;
  private BigDecimal valor;
  private BigDecimal saldoResultante;
  private String descripcion;
}
