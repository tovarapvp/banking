package com.tovarapvp.banking.cuentas.mapper;

import com.tovarapvp.banking.cuentas.dto.MovimientoRequest;
import com.tovarapvp.banking.cuentas.dto.MovimientoResponse;
import com.tovarapvp.banking.cuentas.model.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {
  MovimientoResponse toResponse(Movimiento movimiento);

  Movimiento toEntity(MovimientoRequest request);

  @Mapping(target = "id", ignore = true)
  void updateEntity(MovimientoRequest request, @MappingTarget Movimiento movimiento);
}
