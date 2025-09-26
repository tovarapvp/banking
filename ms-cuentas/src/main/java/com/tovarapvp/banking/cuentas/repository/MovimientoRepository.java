package com.tovarapvp.banking.cuentas.repository;

import com.tovarapvp.banking.cuentas.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MovimientoRepository extends JpaRepository<Movimiento, UUID> {
  List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(
      UUID cuentaId, Instant desde, Instant hasta);
}
