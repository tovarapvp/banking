Feature: Test de API para Clientes

# Asume que ms-clientes corre en este puerto
  Background:
    * url 'http://localhost:' + karate.properties['server.port']

  Scenario: Crear y obtener un cliente

    # 1. Crear un nuevo cliente
    Given path '/clientes'
    And request { nombre: 'Juan Perez', contrasena: '1234', genero: 'Masculino', edad: 30, identificacion: '1234567890', direccion: 'Av. Siempre Viva 123', telefono: '0987654321' }
    When method post
    Then status 201

    # Extraer el ID del cliente creado
    And def clienteId = response.id

    # 2. Obtener el cliente creado usando su ID
    Given path '/clientes', clienteId
    When method get
    Then status 200
    And match response.nombre == 'Juan Perez'
    And match response.id == clienteId
