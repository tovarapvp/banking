# Proyecto de Microservicios Bancarios

Este proyecto implementa un sistema bancario básico utilizando una arquitectura de microservicios con Java y Spring Boot.

## Arquitectura

El sistema se compone de los siguientes microservicios:

- **ms-config-server**: Servidor de configuración centralizado para todos los microservicios.
- **ms-eureka-server**: Servidor de descubrimiento de servicios (Service Discovery).
- **ms-gateway**: Puerta de enlace (API Gateway) que actúa como punto de entrada único para todas las solicitudes de los clientes.
- **ms-clientes**: Gestiona toda la lógica de negocio relacionada con los clientes (CRUD completo).
- **ms-cuentas**: Gestiona la lógica de cuentas y movimientos, además de la generación de reportes.
- **common**: Módulo con clases compartidas (ej. eventos de dominio) entre los microservicios.

## Características Implementadas

- **Clientes**: Creación, Lectura, Actualización y Eliminación (CRUD).
- **Cuentas**: Creación, Lectura y Actualización (CRU).
- **Movimientos**: Creación, Lectura y Actualización (CRU).
- **Reportes**: Generación de estado de cuenta por cliente y rango de fechas.
- **Comunicación Asíncrona**: Uso de RabbitMQ para la comunicación de eventos entre servicios (Patrón Outbox).
- **Pruebas Automatizadas**: Cobertura de pruebas unitarias, de integración y de API (Karate).

---

## Requisitos Previos

- JDK 17 o superior.
- Maven 3.9 o superior.
- Docker y Docker Compose.

---

## Guía de Ejecución y Pruebas

### 1. Ejecutar la Aplicación (Recomendado)

La forma más sencilla de levantar todo el entorno es utilizando Docker Compose. Este comando construirá las imágenes de los microservicios y levantará todos los contenedores necesarios (Base de Datos, RabbitMQ y todos los servicios de Spring).

```bash
# Desde la raíz del proyecto, ejecuta:
docker-compose up --build
```

Una vez que todos los servicios se hayan iniciado, la API principal será accesible a través del Gateway en la siguiente URL:

- **API Gateway**: `http://localhost:8082`

Otros puertos importantes:
- **Eureka Dashboard**: `http://localhost:8761`
- **RabbitMQ Management**: `http://localhost:15672` (user: `guest`, pass: `guest`)

### 2. Ejecutar las Pruebas Automatizadas

El proyecto está configurado para ejecutar todas las pruebas (unitarias, de integración con Testcontainers y de API con Karate) con un solo comando de Maven.

```bash
# Desde la raíz del proyecto, ejecuta:
mvn clean install
```

Esto compilará el proyecto, ejecutará todo el set de pruebas y generará los artefactos `.jar`.

### 3. Probar la API Manualmente (Postman)

Se incluye una colección de Postman con ejemplos para cada endpoint de la API.

1.  **Importar la colección**: Importa el archivo `postman/collection.json` en tu cliente Postman.
2.  **Realizar peticiones**: La colección contiene peticiones para todas las funcionalidades, como crear clientes, registrar movimientos y generar reportes. Todas las peticiones deben apuntar al API Gateway (`http://localhost:8082`).

---

## Entregables del Proyecto

- **Script de Base de Datos**: El archivo `BaseDatos.sql` contiene el DDL para crear todo el esquema de la base de datos y algunos datos de ejemplo.
- **Colección de Postman**: El archivo `postman/collection.json` contiene una colección completa para probar todos los endpoints de la API.
- **Pruebas de Karate**: El set de pruebas de API se encuentra en `ms-clientes/src/test/resources/com/tovarapvp/banking/clientes/karate/` y se ejecuta automáticamente con `mvn test`.