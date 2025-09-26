package com.tovarapvp.banking.clientes;

import com.tovarapvp.banking.clientes.dto.ClienteRequest;
import com.tovarapvp.banking.clientes.dto.ClienteResponse;
import com.tovarapvp.banking.clientes.repository.ClienteRepository;
import com.tovarapvp.banking.clientes.service.ClienteService;
import org.junit.jupiter.api.Test;
import com.tovarapvp.banking.clientes.repository.OutboxEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Agregamos Transactional para hacer rollback al final de la prueba
class ClienteServiceTest {

  @Autowired
  private ClienteService service;
  @Autowired
  private ClienteRepository repo;
  @Autowired
  private OutboxEventRepository outboxRepo; // Repositorio para verificar el evento

  @Test
  @DisplayName("Crear un cliente debe guardarlo en la BD y publicar un evento en la tabla outbox")
  void creaCliente_yPublicaEvento() {
    // Arrange: Preparamos todos los datos necesarios
    ClienteRequest c = new ClienteRequest();
    c.setNombre("Marianela Montalvo"); // Corregido: setNombre
    c.setIdentificacion("1234567890"); // Añadido: campo obligatorio
    c.setContrasena("5678");
    c.setGenero("Femenino");
    c.setEdad(30);
    c.setDireccion("Amazonas y NNUU");
    c.setTelefono("0987654321");

    long outboxCountBefore = outboxRepo.count();

    // Act: Ejecutamos el método a probar
    ClienteResponse saved = service.create(c);

    // Assert: Verificamos los resultados
    assertNotNull(saved.getId());

    // 1. Verificar que el cliente se guardó correctamente en su repositorio
    assertEquals("Marianela Montalvo", repo.findById(saved.getId()).orElseThrow().getNombre()); // Corregido: getNombre

    // 2. Verificar que se creó un evento en la tabla outbox
    long outboxCountAfter = outboxRepo.count();
    assertEquals(outboxCountBefore + 1, outboxCountAfter, "Debería haberse creado un evento en la tabla outbox");
  }
}
