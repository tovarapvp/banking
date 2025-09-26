package com.tovarapvp.banking.cuentas;
import com.tovarapvp.banking.cuentas.dto.MovimientoRequest;
import com.tovarapvp.banking.cuentas.exception.SaldoNoDisponibleException;
import com.tovarapvp.banking.cuentas.model.Cuenta;
import com.tovarapvp.banking.cuentas.repository.CuentaRepository;
import com.tovarapvp.banking.cuentas.service.MovimientoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
@Transactional // Rollback transactions after each test to ensure isolation
class MovimientoIT {

  @Container
  static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16-alpine");

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", pg::getJdbcUrl);
    r.add("spring.datasource.username", pg::getUsername);
    r.add("spring.datasource.password", pg::getPassword);
  }

  @Autowired
  private CuentaRepository cuentas;
  @Autowired
  private MovimientoService movimientos;

  @Test
  @DisplayName("Registrar un retiro con saldo insuficiente debe lanzar SaldoNoDisponibleException")
  void retiroConSaldo_insuficienteLanzaExcepcion() {
    // Arrange: Set up the initial state and data
    Cuenta c = cuentas.save(Cuenta.builder()
            .numero("225487")
            .tipo("Corriente")
            .saldoInicial(new BigDecimal("100.00"))
            .saldo(new BigDecimal("100.00"))
            .clienteId(UUID.randomUUID())
            .estado(true).build());

    BigDecimal montoRetiro = new BigDecimal("-140.00");
    String tipoMovimiento = "retiro";
    String expectedErrorMessage = "Saldo no disponible";

    var movimientoRequest = new MovimientoRequest();
    movimientoRequest.setCuentaId(c.getId());
    movimientoRequest.setValor(montoRetiro);
    movimientoRequest.setDescripcion(tipoMovimiento);
            
    // Act & Assert: Perform the action and verify the outcome
    SaldoNoDisponibleException thrown = assertThrows(
            SaldoNoDisponibleException.class,
            () -> movimientos.registrar(movimientoRequest),
            "Se esperaba que registrar lanzara una excepción por saldo no disponible, pero no lo hizo"
    );

    // Assert on the exception message for more specific verification
    assertEquals(expectedErrorMessage, thrown.getMessage());
  }
}
