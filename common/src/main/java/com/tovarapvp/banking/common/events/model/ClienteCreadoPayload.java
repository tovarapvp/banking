package com.tovarapvp.banking.common.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreadoPayload implements Serializable {
    private UUID clienteId;
    private String nombre;
}
