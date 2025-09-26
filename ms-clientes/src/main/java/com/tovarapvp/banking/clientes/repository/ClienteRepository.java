package com.tovarapvp.banking.clientes.repository;

import com.tovarapvp.banking.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {}
