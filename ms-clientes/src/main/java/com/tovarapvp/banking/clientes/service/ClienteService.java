package com.tovarapvp.banking.clientes.service;

import com.tovarapvp.banking.clientes.dto.ClienteRequest;
import com.tovarapvp.banking.clientes.dto.ClienteResponse;
import com.tovarapvp.banking.clientes.exception.NotFoundException;
import com.tovarapvp.banking.clientes.mapper.ClienteMapper;
import com.tovarapvp.banking.clientes.model.Cliente;
import com.tovarapvp.banking.clientes.model.OutboxEvent;
import com.tovarapvp.banking.clientes.repository.ClienteRepository;
import com.tovarapvp.banking.clientes.repository.OutboxEventRepository;
import com.tovarapvp.banking.common.events.ClienteCreadoEvent;
import com.tovarapvp.banking.common.events.model.ClienteCreadoPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {
  private final ClienteRepository repo;
  private final OutboxEventRepository outboxRepo; // Inyectar el nuevo repo
  private final ClienteMapper mapper;
  private final ObjectMapper objectMapper; // Inyectar ObjectMapper

  @Transactional // ¡Muy importante!
  public ClienteResponse create(ClienteRequest request) {
    Cliente cliente = mapper.toEntity(request);
    Cliente saved = repo.save(cliente);

    // 1. Crear el payload y el evento
    ClienteCreadoPayload payload = new ClienteCreadoPayload(saved.getId(), saved.getNombre());
    ClienteCreadoEvent event = new ClienteCreadoEvent(payload);

    try {
      // 2. Serializar el payload a JSON
      String eventPayloadJson = objectMapper.writeValueAsString(event.getData());

      // 3. Crear y guardar la entidad OutboxEvent
      OutboxEvent outboxEvent = OutboxEvent.builder()
              .id(event.getEventId())
              .aggregateType("Cliente")
              .aggregateId(saved.getId().toString())
              .eventType("ClienteCreado")
              .payload(eventPayloadJson)
              .build();
      outboxRepo.save(outboxEvent);

    } catch (JsonProcessingException e) {
      // Manejar la excepción (aunque es poco probable con objetos POJO)
      throw new RuntimeException("Error serializando el evento", e);
    }

    // Ya NO enviamos el evento directamente a RabbitMQ
    // amqp.convertAndSend(...) -> esta línea se elimina

    return mapper.toResponse(saved);
  }

  // ... resto de los métodos (list, get, update, delete) sin cambios
  public Page<ClienteResponse> list(Pageable p) {
    return repo.findAll(p).map(mapper::toResponse);
  }

  public Optional<ClienteResponse> get(UUID id) {
    return repo.findById(id).map(mapper::toResponse);
  }

  @Transactional
  public ClienteResponse update(UUID id, ClienteRequest request) {
    Cliente cliente = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    mapper.updateEntity(request, cliente);
    return mapper.toResponse(repo.save(cliente));
  }

  @Transactional
  public void delete(UUID id) {
    repo.deleteById(id);
  }
}