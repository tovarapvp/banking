package com.tovarapvp.banking.cuentas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CuentaRequest {
  @NotBlank private String numero;
  @NotBlank private String tipo;
  @NotNull private BigDecimal saldoInicial;
  @NotNull private Boolean estado;
  @NotNull private UUID clienteId;
}
