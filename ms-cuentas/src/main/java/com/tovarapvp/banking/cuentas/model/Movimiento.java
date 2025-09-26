package com.tovarapvp.banking.cuentas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="movimientos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Movimiento {
  @Id @Column(columnDefinition="uuid") private UUID id;
  @Column(name="cuenta_id", columnDefinition="uuid") private UUID cuentaId;
  private Instant fecha = Instant.now();
  @NotBlank private String tipoMovimiento;
  @NotNull private BigDecimal valor;
  @NotNull private BigDecimal saldoResultante;
  private String descripcion;
  @PrePersist void pre(){ if(id==null) id=UUID.randomUUID(); }
}
