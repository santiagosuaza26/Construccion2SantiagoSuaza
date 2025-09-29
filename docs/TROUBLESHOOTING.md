# 🐛 Guía de Troubleshooting

## Índice
- [Errores Comunes](#errores-comunes)
- [Problemas de Base de Datos](#problemas-de-base-de-datos)
- [Problemas de Autenticación](#problemas-de-autenticación)
- [Problemas de Despliegue](#problemas-de-despliegue)
- [Problemas de Performance](#problemas-de-performance)
- [Logs y Debugging](#logs-y-debugging)
- [Herramientas de Diagnóstico](#herramientas-de-diagnóstico)

---

## Errores Comunes

### 1. Error 500 - Internal Server Error

**Síntomas:**
- La aplicación responde con código 500
- Funcionalidad básica no opera

**Causas Posibles:**
1. **Problema de autenticación**: El método `getCurrentUser()` no encuentra al usuario
2. **Inyección de dependencias fallida**: Servicios no se inicializan correctamente
3. **Base de datos no disponible**: Conexión perdida
4. **Excepción no manejada**: Error en el código de la aplicación

**Soluciones:**

```bash
# 1. Verificar logs de la aplicación
docker-compose logs -f app

# 2. Buscar errores específicos
docker-compose logs app | grep -i error

# 3. Verificar estado de servicios
docker-compose ps

# 4. Reiniciar servicios problemáticos
docker-compose restart app
```

### 2. Error de Conexión a Base de Datos

**Síntomas:**
- No se puede conectar a MySQL/MongoDB
- Operaciones CRUD fallan

**Causas Posibles:**
1. **Contenedores no iniciados**: Servicios de BD no están corriendo
2. **Variables de entorno incorrectas**: Credenciales erróneas
3. **Puerto ocupado**: Otro servicio usando el puerto
4. **Red Docker mal configurada**: Contenedores no se comunican

**Soluciones:**

```bash
# 1. Verificar estado de BD
docker-compose ps

# 2. Ver logs de MySQL
docker-compose logs mysql-db

# 3. Probar conectividad
docker-compose exec mysql-db mysql -u clinic_user -p clinicdb -e "SELECT 1;"

# 4. Verificar variables de entorno
docker-compose exec app env | grep -E "(DB_|MONGO_)"

# 5. Reiniciar servicios de BD
docker-compose restart mysql-db mongodb
```

### 3. Error de Autenticación

**Síntomas:**
- Login falla constantemente
- Header `User-ID` no funciona
- Permisos denegados

**Causas Posibles:**
1. **Usuario mock no funciona**: Problema en `AuthApplicationService`
2. **ID de usuario inválido**: Formato incorrecto
3. **Sesión expirada**: Timeout de sesión
4. **Permisos mal configurados**: Lógica de roles incorrecta

**Soluciones:**

```bash
# 1. Verificar logs de autenticación
docker-compose logs app | grep -i "auth\|login"

# 2. Probar con diferentes IDs de usuario
# ADM001, HR001, SUP001, DOC001, NUR001, PAT001

# 3. Verificar configuración de roles
curl -X GET http://localhost:8080/api/auth/permissions?permission=PATIENT_READ \
  -H "User-ID: ADM001"

# 4. Revisar logs detallados
docker-compose logs app | grep -A 5 -B 5 "getCurrentUser"
```

### 4. Error de CORS

**Síntomas:**
- Requests desde frontend fallan
- Error "CORS policy" en navegador
- Options requests fallan

**Causas Posibles:**
1. **Orígenes no permitidos**: Frontend no está en lista blanca
2. **Headers faltantes**: Headers requeridos no configurados
3. **Métodos HTTP no permitidos**: Método no está en lista

**Soluciones:**

```bash
# 1. Verificar configuración CORS actual
curl -X OPTIONS http://localhost:8080/api/patients \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: GET"

# 2. Agregar origen faltante en .env
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,http://localhost:8080

# 3. Reiniciar aplicación
docker-compose restart app
```

---

## Problemas de Base de Datos

### MySQL

#### Problema: No se puede conectar a MySQL
```bash
# Verificar estado del contenedor
docker-compose ps mysql-db

# Ver logs de MySQL
docker-compose logs mysql-db

# Probar conexión manual
docker-compose exec mysql-db mysql -u root -p

# Si el contenedor no inicia, recrearlo
docker-compose down mysql-db
docker-compose up -d mysql-db
```

#### Problema: Datos perdidos
```bash
# Verificar volúmenes
docker volume ls | grep clinic

# Verificar backups
ls -la backups/

# Restaurar desde backup
docker-compose exec -T mysql-db mysql -u clinic_user -p clinicdb < backup.sql
```

### MongoDB

#### Problema: No se puede conectar a MongoDB
```bash
# Verificar estado
docker-compose ps mongodb

# Ver logs
docker-compose logs mongodb

# Probar conexión
docker-compose exec mongodb mongosh --eval "db.adminCommand('ping')"
```

#### Problema: Índices no creados
```bash
# Conectar a MongoDB
docker-compose exec mongodb mongosh -u admin -p

# Crear índices manualmente
use clinical_history_db
db.clinical_history.createIndex({ "patientIdCard": 1 })
db.clinical_history.createIndex({ "entryDate": 1 })
```

---

## Problemas de Despliegue

### Docker

#### Problema: Contenedores no inician
```bash
# Verificar recursos del sistema
docker system df

# Verificar logs de Docker
docker-compose logs

# Limpiar contenedores huérfanos
docker-compose down --remove-orphans

# Reconstruir contenedores
docker-compose build --no-cache
docker-compose up -d
```

#### Problema: Puerto ocupado
```bash
# Encontrar proceso usando el puerto
netstat -ano | findstr :8080

# Matar proceso (reemplazar PID)
taskkill /PID <PID> /F

# O cambiar puerto en .env
SERVER_PORT=8081
```

#### Problema: Memoria insuficiente
```bash
# Verificar uso de memoria
docker stats

# Ajustar límites en docker-compose.yml
app:
  deploy:
    resources:
      limits:
        memory: 1G
      reservations:
        memory: 512M
```

### Maven

#### Problema: Build falla
```bash
# Limpiar cache de Maven
./mvnw clean

# Descargar dependencias
./mvnw dependency:resolve

# Compilar con debug
./mvnw compile -X

# Verificar versión de Java
java -version
```

#### Problema: Tests fallan
```bash
# Ejecutar tests específicos
./mvnw test -Dtest=PatientServiceTest

# Ver reporte de tests
./mvnw surefire-report:report

# Ejecutar con debug
./mvnw test -DforkCount=0 -Ddebug
```

---

## Problemas de Performance

### Aplicación Lenta

#### Diagnóstico:
```bash
# Ver métricas de la aplicación
curl http://localhost:8080/api/actuator/metrics

# Ver health check detallado
curl http://localhost:8080/api/actuator/health

# Ver threads de la aplicación
jstack <PID> > thread-dump.txt
```

#### Optimizaciones:
```bash
# Configurar pool de conexiones
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Habilitar caché
spring.cache.type=redis

# Configurar timeouts
spring.datasource.hikari.connection-timeout=30000
```

### Base de Datos Lenta

#### MySQL:
```bash
# Verificar queries lentas
docker-compose exec mysql-db mysql -u clinic_user -p -e "SHOW PROCESSLIST;"

# Verificar índices
docker-compose exec mysql-db mysql -u clinic_user -p -e "SHOW INDEX FROM patients;"

# Analizar query lenta
EXPLAIN SELECT * FROM patients WHERE id_card = '12345678'\G
```

#### MongoDB:
```bash
# Verificar performance
docker-compose exec mongodb mongosh -u admin -p
db.serverStatus()

# Ver queries actuales
db.currentOp()
```

---

## Logs y Debugging

### Configuración de Logs

#### Para Desarrollo (DEBUG):
```properties
# En application-dev.properties
logging.level.app=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

#### Para Producción (INFO):
```properties
# En application-prod.properties
logging.level.app=INFO
logging.level.root=WARN
```

### Ubicación de Logs

```bash
# En Docker
docker-compose logs -f app > app.log

# En sistema local
tail -f logs/clinic-app.log

# Logs de todos los servicios
docker-compose logs > all-logs.log
```

### Análisis de Logs

```bash
# Buscar errores
docker-compose logs app | grep -i error

# Buscar por usuario específico
docker-compose logs app | grep "ADM001"

# Buscar por fecha
docker-compose logs app | grep "2024-01"

# Contar errores por tipo
docker-compose logs app | grep -o "ERROR.*" | sort | uniq -c
```

---

## Herramientas de Diagnóstico

### 1. Health Checks

```bash
# Health check general
curl http://localhost:8080/api/actuator/health

# Health check detallado
curl http://localhost:8080/api/actuator/health?details=true

# Métricas de la aplicación
curl http://localhost:8080/api/actuator/metrics/http.server.requests

# Información del sistema
curl http://localhost:8080/api/actuator/info
```

### 2. Database Tools

#### MySQL:
```bash
# Acceso directo a MySQL
docker-compose exec mysql-db mysql -u clinic_user -p clinicdb

# Ver tablas
SHOW TABLES;

# Ver estructura de tabla
DESCRIBE patients;

# Ver estadísticas
SHOW TABLE STATUS;
```

#### MongoDB:
```bash
# Acceso directo a MongoDB
docker-compose exec mongodb mongosh -u admin -p

# Ver bases de datos
show dbs

# Ver colecciones
use clinical_history_db
show collections

# Ver documentos
db.clinical_history.find().limit(5)
```

### 3. Network Tools

```bash
# Ver redes Docker
docker network ls

# Inspeccionar red de la aplicación
docker network inspect clinic-network_clinic-network

# Probar conectividad entre contenedores
docker-compose exec app ping mysql-db
docker-compose exec app nslookup mongodb
```

### 4. System Monitoring

```bash
# Recursos del sistema
docker stats

# Uso de disco
docker system df

# Información del sistema Docker
docker system info

# Procesos del sistema
top
htop
```

---

## Procedimientos de Emergencia

### 1. Backup de Bases de Datos

```bash
# Crear backup completo
./backup-all-databases.sh

# Backup manual MySQL
docker-compose exec mysql-db mysqldump -u clinic_user -p clinicdb > backup_mysql_$(date +%Y%m%d_%H%M%S).sql

# Backup manual MongoDB
docker-compose exec mongodb mongodump --out /data/backup/$(date +%Y%m%d_%H%M%S)
```

### 2. Restauración de Bases de Datos

```bash
# Restaurar MySQL
docker-compose exec -T mysql-db mysql -u clinic_user -p clinicdb < backup.sql

# Restaurar MongoDB
docker-compose exec mongodb mongorestore /data/backup/20240101_120000
```

### 3. Reinicio Completo del Sistema

```bash
# Parar todos los servicios
docker-compose down

# Limpiar volúmenes (¡CUIDADO! Borra datos)
docker volume prune -f

# Limpiar contenedores
docker system prune -f

# Reiniciar desde cero
docker-compose up -d
```

---

## Contacto y Soporte

Si después de seguir esta guía sigues teniendo problemas:

1. **Revisa los logs más recientes**: `docker-compose logs -f app`
2. **Verifica la configuración**: Revisa tu archivo `.env`
3. **Prueba en modo desarrollo**: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`
4. **Contacta al equipo**: santiago.suaza@clinica-cs2.com

### Información para Reportar Errores

Cuando reportes un error, incluye:

```bash
# Información del sistema
docker --version
docker-compose --version
java -version

# Logs relevantes
docker-compose logs app | tail -50

# Configuración actual
docker-compose exec app env | grep -E "(DB_|MONGO_|SPRING_)"
```

---

**¡No desesperes! La mayoría de problemas tienen solución siguiendo estos pasos.**