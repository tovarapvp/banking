package com.tovarapvp.banking.clientes.service;

import com.tovarapvp.banking.clientes.model.OutboxEvent;
import com.tovarapvp.banking.clientes.repository.OutboxEventRepository;
import com.tovarapvp.banking.common.events.ClienteCreadoEvent;
import com.tovarapvp.banking.common.events.model.ClienteCreadoPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private final OutboxEventRepository outboxRepo;
    private final AmqpTemplate amqp;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10000L) // Se ejecuta cada 10 segundos
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> events = outboxRepo.findTop100ByOrderByCreatedAtAsc();
        if (!events.isEmpty()) {
            log.info("Publicando {} eventos desde la tabla outbox", events.size());
        }

        for (OutboxEvent outboxEvent : events) {
            try {
                // Reconstruir el payload y el evento original
                ClienteCreadoPayload payload = objectMapper.readValue(outboxEvent.getPayload(), ClienteCreadoPayload.class);
                ClienteCreadoEvent eventToPublish = new ClienteCreadoEvent(payload);
                eventToPublish.setEventId(outboxEvent.getId()); // Opcional: mantener el mismo ID

                // Enviar a RabbitMQ
                amqp.convertAndSend("clientes.exchange", "cliente.creado", eventToPublish);

                // Eliminar el evento de la tabla outbox
                outboxRepo.delete(outboxEvent);

            } catch (JsonProcessingException e) {
                log.error("Error al deserializar el evento con id {}: ", outboxEvent.getId(), e);
                // Aquí podrías mover el evento a una tabla de "letra muerta" o incrementar un contador de reintentos
            } catch (Exception e) {
                log.error("Error al procesar el evento con id {}: ", outboxEvent.getId(), e);
                // El evento no se elimina, por lo que se reintentará en la próxima ejecución
            }
        }
    }
}