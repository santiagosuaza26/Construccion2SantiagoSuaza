# Multi-stage build para optimización
FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de Maven
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Descargar dependencias (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Construir aplicación
RUN ./mvnw clean package -DskipTests

# Imagen de runtime
FROM openjdk:17-jre-alpine

LABEL maintainer="Clínica CS2" \
      description="Sistema de Gestión Médica - Clínica CS2" \
      version="1.0.0"

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S clinic && \
    adduser -S clinic -u 1001

# Copiar JAR desde builder stage
COPY --from=builder /app/target/*.jar app.jar

# Cambiar ownership del archivo
RUN chown clinic:clinic app.jar

# Cambiar a usuario no-root
USER clinic

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# Configuración JVM para producción
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication"

# Puerto de la aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]