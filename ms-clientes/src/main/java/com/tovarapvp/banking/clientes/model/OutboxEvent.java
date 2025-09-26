package com.tovarapvp.banking.clientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "aggregate_type")
    private String aggregateType;

    @Column(name = "aggregate_id")
    private String aggregateId;

    @Column(name = "event_type")
    private String eventType;

    @Column(columnDefinition = "text")
    private String payload;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}