package com.tovarapvp.banking.cuentas.controller;

import com.tovarapvp.banking.cuentas.model.Cuenta;
import com.tovarapvp.banking.cuentas.model.Movimiento;
import com.tovarapvp.banking.cuentas.repository.CuentaRepository;
import com.tovarapvp.banking.cuentas.repository.MovimientoRepository;
import java.time.Instant;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

  private final CuentaRepository cuentas;
  private final MovimientoRepository movimientos;

  @GetMapping
  public Map<String, Object> estadoDeCuenta(
          @RequestParam UUID clienteId,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant desde,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant hasta) {

    List<Cuenta> cuentasCliente = cuentas.findByClienteId(clienteId);

    List<Map<String, Object>> cuentasDto = new ArrayList<>();

    for (Cuenta c : cuentasCliente) {
      List<Movimiento> movs =
              movimientos.findByCuentaIdAndFechaBetweenOrderByFechaAsc(c.getId(), desde, hasta);

      // CORRECCIÓN APLICA AQUÍ
      // Se cambia Map.of() por la construcción explícita de un HashMap para evitar
      // el problema de inferencia de tipos.
      List<Map<String, Object>> detalle =
              movs.stream()
                      .map(
                              m -> {
                                // Se crea un mapa mutable con los tipos correctos
                                Map<String, Object> movimientoMap = new HashMap<>();
                                movimientoMap.put("fecha", m.getFecha());
                                movimientoMap.put("valor", m.getValor());
                                movimientoMap.put("saldoResultante", m.getSaldoResultante());
                                movimientoMap.put("descripcion", m.getDescripcion());
                                return movimientoMap;
                              })
                      .collect(Collectors.toList());

      // Aquí Map.of() funciona bien porque uno de los valores ("movimientos")
      // ya es una colección compleja, forzando la inferencia a Object.
      // Sin embargo, por consistencia, también se podría usar un HashMap.
      Map<String, Object> cuentaMap = new HashMap<>();
      cuentaMap.put("numero", c.getNumero());
      cuentaMap.put("tipo", c.getTipo());
      cuentaMap.put("saldo", c.getSaldo());
      cuentaMap.put("saldoInicial", c.getSaldoInicial());
      cuentaMap.put("movimientos", detalle);

      cuentasDto.add(cuentaMap);
    }

    // Para el mapa de retorno final, se puede usar HashMap o Map.of sin problemas.
    Map<String, Object> response = new HashMap<>();
    response.put("clienteId", clienteId);
    response.put("desde", desde);
    response.put("hasta", hasta);
    response.put("cuentas", cuentasDto);

    return response;
  }
}