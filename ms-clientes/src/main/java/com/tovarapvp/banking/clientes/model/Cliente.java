package com.tovarapvp.banking.clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "clientes")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor 
@AllArgsConstructor 
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