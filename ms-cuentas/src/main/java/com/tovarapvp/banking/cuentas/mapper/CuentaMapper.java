package com.tovarapvp.banking.cuentas.mapper;

import com.tovarapvp.banking.cuentas.dto.CuentaRequest;
import com.tovarapvp.banking.cuentas.dto.CuentaResponse;
import com.tovarapvp.banking.cuentas.model.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CuentaMapper {
  CuentaResponse toResponse(Cuenta cuenta);

  Cuenta toEntity(CuentaRequest request);

  void updateEntity(CuentaRequest request, @MappingTarget Cuenta cuenta);
}
