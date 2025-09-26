package com.tovarapvp.banking.cuentas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name="cuentas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cuenta {
  @Id @Column(columnDefinition="uuid") private UUID id;
  @Column(unique=true, nullable=false) private String numero;
  @NotBlank private String tipo;
  @NotNull private BigDecimal saldoInicial;
  @NotNull private BigDecimal saldo;
  @NotNull private Boolean estado = true;
  @Column(name="cliente_id", columnDefinition="uuid") private UUID clienteId;
  @PrePersist void pre(){ if(id==null) id=UUID.randomUUID(); if(saldo==null) saldo=saldoInicial; }
}
