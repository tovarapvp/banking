package com.tovarapvp.banking.clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "clientes")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Cliente extends Persona {
  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @NotBlank private String contrasena;

  @NotNull @Builder.Default
  private Boolean estado = true;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @PrePersist
  void pre() {
    if (id == null) id = UUID.randomUUID();
  }
}