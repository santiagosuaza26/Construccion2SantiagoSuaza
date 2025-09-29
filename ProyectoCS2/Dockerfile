# ===============================================
# DOCKERFILE PARA APLICACIÓN SPRING BOOT
# Sistema de Gestión Médica - Clínica CS2
# ===============================================

# Etapa 1: Construir la aplicación
FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de Maven
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

# Descargar dependencias (para cachear esta capa)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir aplicación
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: Imagen de producción
FROM openjdk:17-jre-alpine

LABEL maintainer="Clínica CS2 Development Team"
LABEL version="1.0"
LABEL description="Sistema de Gestión Médica - Clínica CS2"

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S clinicapp && \
    adduser -S clinicuser -u 1001 -G clinicapp

# Crear directorios necesarios
RUN mkdir -p /app/logs /app/uploads /app/temp && \
    chown -R clinicuser:clinicapp /app

WORKDIR /app

# Copiar JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Cambiar propietario del archivo
RUN chown clinicuser:clinicapp app.jar

# Cambiar a usuario no-root
USER clinicuser

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# Puerto de exposición
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# ===============================================
# INSTRUCCIONES DE USO:
# ===============================================
# Construir imagen:
#   docker build -t clinic-app:1.0 .
#
# Ejecutar en desarrollo:
#   docker run -p 8080:8080 clinic-app:1.0
#
# Ejecutar en producción con docker-compose:
#   docker-compose up -d app
#
# Ver logs:
#   docker-compose logs -f app
# ===============================================