# 📖 Guía de Instalación y Configuración

## Índice
- [Requisitos del Sistema](#requisitos-del-sistema)
- [Instalación Rápida](#instalación-rápida)
- [Configuración Detallada](#configuración-detallada)
- [Despliegue con Docker](#despliegue-con-docker)
- [Configuración de Base de Datos](#configuración-de-base-de-datos)
- [Verificación de Instalación](#verificación-de-instalación)

## Requisitos del Sistema

### Mínimos
- **Sistema Operativo**: Windows 10/11, macOS 10.15+, Linux Ubuntu 18.04+
- **Memoria RAM**: 2 GB mínimo, 4 GB recomendado
- **Espacio en Disco**: 5 GB libre
- **Java**: OpenJDK 17 o superior
- **Maven**: 3.6 o superior

### Para Producción
- **Memoria RAM**: 8 GB mínimo
- **Espacio en Disco**: 20 GB libre
- **CPU**: 2 cores mínimo, 4 cores recomendado
- **Docker**: 20.10 o superior
- **Docker Compose**: 1.29 o superior

## Instalación Rápida

### Opción 1: Desarrollo Local (Recomendado para principiantes)

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd ProyectoCS2

# 2. Ejecutar la aplicación
./mvnw spring-boot:run

# 3. Acceder a la aplicación
# API: http://localhost:8081/api
# H2 Console: http://localhost:8081/api/h2-console
```

### Opción 2: Con perfil específico

```bash
# Desarrollo con H2
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Producción con MySQL (requiere configuración previa)
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Configuración Detallada

### 1. Variables de Entorno

Crea un archivo `.env` basado en `.env.example`:

```bash
cp .env.example .env
```

Edita el archivo `.env` con tus configuraciones:

```bash
# Base de datos MySQL
MYSQL_ROOT_PASSWORD=tu_password_seguro_aqui
MYSQL_DATABASE=clinicdb
MYSQL_USER=clinic_user
MYSQL_PASSWORD=tu_password_usuario_aqui

# MongoDB
MONGO_ROOT_PASSWORD=tu_password_mongo_aqui

# JWT
JWT_SECRET=tu_clave_secreta_jwt_de_al_menos_32_caracteres

# Puerto del servidor
SERVER_PORT=8080
```

### 2. Configuración de Perfiles

#### Perfil de Desarrollo (`dev`)
- **Base de datos**: H2 en memoria
- **Puerto**: 8081
- **Logs**: Nivel DEBUG
- **H2 Console**: Habilitado

#### Perfil de Producción (`prod`)
- **Base de datos**: MySQL 8.0
- **Puerto**: 8080 (configurable)
- **Logs**: Nivel INFO
- **Monitoreo**: Habilitado

### 3. Configuración de Base de Datos

#### MySQL (Producción)

```bash
# Crear base de datos manualmente
mysql -u root -p
CREATE DATABASE clinicdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'clinic_user'@'%' IDENTIFIED BY 'tu_password_seguro';
GRANT ALL PRIVILEGES ON clinicdb.* TO 'clinic_user'@'%';
FLUSH PRIVILEGES;
EXIT;
```

#### MongoDB (Historia Clínica)

```bash
# Conectar a MongoDB
mongosh -u admin -p tu_password_mongo

# Crear base de datos
use clinical_history_db

# Crear usuario (opcional)
db.createUser({
  user: "clinic_user",
  pwd: "tu_password_mongo",
  roles: [
    { role: "readWrite", db: "clinical_history_db" }
  ]
})
```

## Despliegue con Docker

### 1. Preparación

```bash
# Construir la imagen
docker build -t clinic-app:1.0 .

# Configurar variables de entorno
cp .env.example .env
# Editar .env con tus configuraciones
```

### 2. Despliegue Completo

```bash
# Levantar todos los servicios
docker-compose up -d

# Verificar estado
docker-compose ps

# Ver logs
docker-compose logs -f app
```

### 3. Servicios Disponibles

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Aplicación** | http://localhost:8080/api | API principal |
| **phpMyAdmin** | http://localhost:8081 | Administración MySQL |
| **Mongo Express** | http://localhost:8082 | Administración MongoDB |
| **MySQL** | localhost:3306 | Base de datos SQL |
| **MongoDB** | localhost:27017 | Base de datos NoSQL |

### 4. Comandos Útiles de Docker

```bash
# Reiniciar aplicación
docker-compose restart app

# Ver logs de un servicio específico
docker-compose logs -f mysql-db
docker-compose logs -f mongodb
docker-compose logs -f app

# Ejecutar comandos en contenedores
docker-compose exec app ./mvnw test
docker-compose exec mysql-db mysql -u clinic_user -p clinicdb

# Hacer backup
docker-compose exec mysql-db mysqldump -u clinic_user -p clinicdb > backup_$(date +%Y%m%d_%H%M%S).sql

# Parar todos los servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v
```

## Verificación de Instalación

### 1. Health Checks

```bash
# Verificar estado de la aplicación
curl http://localhost:8080/api/actuator/health

# Debería retornar:
# {"status":"UP"}
```

### 2. Pruebas de Conectividad

```bash
# Probar conexión a MySQL
docker-compose exec mysql-db mysql -u clinic_user -p -e "SELECT 1;"

# Probar conexión a MongoDB
docker-compose exec mongodb mongosh --eval "db.adminCommand('ping')"
```

### 3. Verificación de Logs

```bash
# Ver logs de la aplicación
docker-compose logs app | tail -20

# Buscar errores
docker-compose logs app | grep -i error
```

### 4. Pruebas de API

```bash
# Login de prueba
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin001",
    "password": "password123"
  }'

# Listar pacientes (requiere autenticación)
curl -X GET http://localhost:8080/api/patients \
  -H "User-ID: ADM001"
```

## Configuración Avanzada

### 1. Configuración de Logging

Editar `src/main/resources/logback-spring.xml`:

```xml
<configuration>
    <!-- Configuración para desarrollo -->
    <springProfile name="dev">
        <logger name="app" level="DEBUG" />
    </springProfile>

    <!-- Configuración para producción -->
    <springProfile name="prod">
        <logger name="app" level="INFO" />
    </springProfile>
</configuration>
```

### 2. Configuración de SSL (Producción)

```properties
# En application-prod.properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat
```

### 3. Configuración de Redis (Opcional)

```properties
# En application-prod.properties
spring.cache.type=redis
spring.redis.host=${REDIS_HOST:redis}
spring.redis.port=${REDIS_PORT:6379}
spring.redis.password=${REDIS_PASSWORD:}
```

## Solución de Problemas

### Problema: Puerto en uso
```bash
# Verificar qué proceso usa el puerto
netstat -ano | findstr :8080

# Cambiar puerto en .env
SERVER_PORT=8081
```

### Problema: Conexión a base de datos
```bash
# Verificar conectividad MySQL
docker-compose exec mysql-db mysql -u clinic_user -p clinicdb -e "SELECT 1;"

# Verificar logs de MySQL
docker-compose logs mysql-db
```

### Problema: Memoria insuficiente
```bash
# Aumentar memoria para Docker
# Editar docker-compose.yml
app:
  deploy:
    resources:
      limits:
        memory: 1G
      reservations:
        memory: 512M
```

## Próximos Pasos

1. ✅ **Instalación básica completada**
2. ⏭️ **Configurar backup automático**
3. ⏭️ **Configurar monitoreo**
4. ⏭️ **Configurar SSL para producción**
5. ⏭️ **Configurar dominio personalizado**

## Soporte

Si encuentras problemas durante la instalación:

1. Revisa los logs: `docker-compose logs -f app`
2. Verifica la configuración en `.env`
3. Consulta la [Guía de Troubleshooting](TROUBLESHOOTING.md)
4. Contacta al equipo de desarrollo

---

**¡Felicitaciones! 🎉 Tu Sistema de Gestión Médica está listo para usar.**