package com.tovarapvp.banking.clientes.mapper;

import com.tovarapvp.banking.clientes.dto.ClienteRequest;
import com.tovarapvp.banking.clientes.dto.ClienteResponse;
import com.tovarapvp.banking.clientes.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
  ClienteResponse toResponse(Cliente cliente);

  Cliente toEntity(ClienteRequest request);

  void updateEntity(ClienteRequest request, @MappingTarget Cliente cliente);
}
