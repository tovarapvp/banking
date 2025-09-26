package com.tovarapvp.banking.cuentas.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CuentaResponse {
  private UUID id;
  private String numero;
  private String tipo;
  private BigDecimal saldoInicial;
  private BigDecimal saldo;
  private Boolean estado;
  private UUID clienteId;
}
