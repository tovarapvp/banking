# =========================================================================
# Etapa 1: Build - Construye la aplicación como un usuario no-root
# =========================================================================
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23 AS build

# Cambiar temporalmente a ROOT para crear el usuario
USER root

# Crear un usuario y grupo 'appuser' con un ID fijo para evitar conflictos
RUN groupadd --gid 1001 appuser && useradd --uid 1001 --gid 1001 -m appuser
WORKDIR /app

# Cambiar la propiedad del directorio de trabajo a nuestro nuevo usuario
# Esto es clave para que Maven pueda escribir los archivos de salida.
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

# Copiar el código fuente
COPY --chown=appuser:appuser common/src ./common/src
COPY --chown=appuser:appuser ms-clientes/src ./ms-clientes/src
# Nota: Si ms-cuentas y ms-config-server son necesarios para compilar ms-clientes,


# Construir solo el módulo ms-clientes y sus dependencias
RUN mvn -am -pl ms-clientes clean package -DskipTests

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
# El --chown asegura que el archivo pertenezca a appuser en la nueva etapa
COPY --from=build --chown=appuser:appuser /app/ms-clientes/target/*.jar app.jar

# Cambiar al usuario no-root para ejecutar la aplicación
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

