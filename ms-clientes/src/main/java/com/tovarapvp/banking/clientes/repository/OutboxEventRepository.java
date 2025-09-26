package com.tovarapvp.banking.clientes.repository;

import com.tovarapvp.banking.clientes.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findTop100ByOrderByCreatedAtAsc();
}