package com.tovarapvp.banking.clientes.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Persona {

    @NotBlank
    private String nombre;

    @NotBlank
    private String identificacion;

    private String genero;

    private int edad;

    private String direccion;

    private String telefono;
}
