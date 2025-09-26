package com.tovarapvp.banking.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequest {
  @NotBlank private String nombre;
  @NotBlank private String identificacion;
  private String genero;
  private int edad;
  private String direccion;
  private String telefono;
  @NotBlank private String contrasena;
}
