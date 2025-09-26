package com.tovarapvp.banking.cuentas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MovimientoRequest {
  @NotNull private UUID cuentaId;
  @NotNull private BigDecimal valor;
  private String descripcion;
}
