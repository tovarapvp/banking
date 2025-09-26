# =========================================================================
# Etapa 1: Build - Construye el microservicio ms-gateway
# =========================================================================
# Usamos una imagen con herramientas de construcción (JDK, Maven)
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23 AS build

# Cambiar temporalmente a ROOT para crear el usuario de la aplicación
USER root

# Crear un usuario y grupo 'appuser' para mejorar la seguridad
RUN groupadd --gid 1001 appuser && \
    useradd --uid 1001 --gid 1001 -m appuser
WORKDIR /app

# Cambiar la propiedad del directorio a nuestro nuevo usuario
RUN chown -R appuser:appuser /app

# Cambiar al usuario no-root para todas las operaciones siguientes
USER appuser

# --- Optimización de caché de Docker ---
# Copiar solo los archivos pom.xml primero. Si no cambian, Docker reutilizará la capa de dependencias.
COPY --chown=appuser:appuser pom.xml .
# Asegúrate de copiar el pom.xml de todos los módulos de los que dependa 'ms-gateway'.
COPY --chown=appuser:appuser common/pom.xml ./common/
COPY --chown=appuser:appuser ms-clientes/pom.xml ./ms-clientes/
COPY --chown=appuser:appuser ms-cuentas/pom.xml ./ms-cuentas/
COPY --chown=appuser:appuser ms-config-server/pom.xml ./ms-config-server/
COPY --chown=appuser:appuser ms-gateway/pom.xml ./ms-gateway/
COPY --chown=appuser:appuser ms-eureka-server/pom.xml ./ms-eureka-server/

# Descargar todas las dependencias en una capa separada.
RUN mvn dependency:go-offline

# --- Copia del código fuente ---
# Copiar el código fuente de los módulos que se van a construir.
COPY --chown=appuser:appuser ms-gateway/src ./ms-gateway/src

# --- Construcción del módulo específico ---

RUN mvn -am -pl ms-gateway clean package -DskipTests

# =========================================================================
# Etapa 2: Package - Crear la imagen final de ejecución
# =========================================================================
# Usamos una imagen base más ligera, solo con el entorno de ejecución de Java
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23

# Cambiar temporalmente a ROOT para crear el usuario
USER root

# Crear el mismo usuario no-root que en la etapa de build
RUN groupadd --gid 1001 appuser && useradd --uid 1001 --gid 1001 -m appuser
WORKDIR /app

# Copiar el artefacto final (.jar) desde la etapa de build
COPY --from=build --chown=appuser:appuser /app/ms-gateway/target/*.jar app.jar

# Cambiar al usuario no-root para ejecutar la aplicación
USER appuser

# Exponer el puerto en el que corre el gateway
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
