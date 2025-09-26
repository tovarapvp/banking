# =========================================================================
# Etapa 1: Build - Construye el microservicio ms-cuentas
# =========================================================================
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23 AS build

# Cambiar temporalmente a ROOT para crear el usuario
USER root

# Crear un usuario y grupo 'appuser' con un ID fijo para evitar conflictos
RUN groupadd --gid 1001 appuser && \
    useradd --uid 1001 --gid 1001 -m appuser
WORKDIR /app

# Cambiar la propiedad del directorio de trabajo a nuestro nuevo usuario
RUN chown -R appuser:appuser /app

# Cambiar al usuario no-root para todas las operaciones siguientes
USER appuser

# Copiar todos los archivos pom.xml para aprovechar el caché de Docker.
COPY --chown=appuser:appuser pom.xml .
COPY --chown=appuser:appuser common/pom.xml ./common/
COPY --chown=appuser:appuser ms-clientes/pom.xml ./ms-clientes/
COPY --chown=appuser:appuser ms-cuentas/pom.xml ./ms-cuentas/
COPY --chown=appuser:appuser ms-config-server/pom.xml ./ms-config-server/
COPY --chown=appuser:appuser ms-gateway/pom.xml ./ms-gateway/
COPY --chown=appuser:appuser ms-eureka-server/pom.xml ./ms-eureka-server/

# Descargar dependencias en una capa separada para mejorar el caché
RUN mvn dependency:go-offline

# Copiar el código fuente de los módulos necesarios para el build
COPY --chown=appuser:appuser common/src ./common/src
COPY --chown=appuser:appuser ms-cuentas/src ./ms-cuentas/src


# Construir solo el módulo ms-cuentas y sus dependencias
RUN mvn -am -pl ms-cuentas clean package -DskipTests

# =========================================================================
# Etapa 2: Package - Crear la imagen final de ejecución
# =========================================================================
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23

# Cambiar temporalmente a ROOT para crear el usuario
USER root

# Crear el mismo usuario no-root que en la etapa de build
RUN groupadd --gid 1001 appuser && useradd --uid 1001 --gid 1001 -m appuser
WORKDIR /app

# Copiar el artefacto final desde la etapa de build
COPY --from=build --chown=appuser:appuser /app/ms-cuentas/target/*.jar app.jar

# Cambiar al usuario no-root para ejecutar la aplicación
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

