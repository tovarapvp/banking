package com.tovarapvp.banking.clientes.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ClienteResponse {
  private UUID id;
  private String nombre;
  private String identificacion;
  private String genero;
  private int edad;
  private String direccion;
  private String telefono;
  private Boolean estado;
  private Instant createdAt;
}
