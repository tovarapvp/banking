package com.tovarapvp.banking.cuentas.repository;

import com.tovarapvp.banking.cuentas.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
  List<Cuenta> findByClienteId(UUID clienteId);
}
