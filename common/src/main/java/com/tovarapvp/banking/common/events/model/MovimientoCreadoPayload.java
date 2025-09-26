package com.tovarapvp.banking.common.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCreadoPayload implements Serializable {
    private UUID movimientoId;
    private UUID cuentaId;
    private BigDecimal valor;
    private BigDecimal saldoResultante;
}
