# Sistema de Gestión Médica - Clínica CS2

Aplicación Spring Boot para gestión integral de una clínica médica, incluyendo pacientes, usuarios, facturación y operaciones médicas.

## 🚀 Inicio Rápido

### Prerrequisitos

- **Java 17** o superior
- **Maven 3.6+**
- **MySQL 8.0** (opcional, para producción)
- **Git**

### 1. Clonar el Repositorio

```bash
git clone <tu-repositorio>
cd ProyectoCS2
```

### 2. Compilar el Proyecto

```bash
# Compilar y descargar dependencias
mvn clean compile

# Ejecutar tests (si existen)
mvn test

# Empaquetar la aplicación
mvn package
```

### 3. Ejecutar la Aplicación

#### Modo Desarrollo (Recomendado para pruebas)
```bash
# Con H2 Database (base de datos en memoria)
java -jar -Dspring-boot.run.profiles=dev target/ProyectoCS2-0.0.1-SNAPSHOT.jar

# O usando Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Modo Producción
```bash
# Con MySQL (requiere configuración de base de datos)
java -jar -Dspring-boot.run.profiles=prod \
  -DDB_PASSWORD=tu_password_seguro \
  target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

### 4. Verificar que Funciona

Una vez iniciada la aplicación, verifica que responde:

```bash
# Verificar estado de la aplicación
curl http://localhost:8080/api/actuator/health

# Debería retornar: {"status":"UP"}
```

## 🏥 Acceso a la Aplicación

### URLs Importantes

- **API Base**: http://localhost:8080/api
- **H2 Console** (solo desarrollo): http://localhost:8080/api/h2-console
- **Actuator** (monitoreo): http://localhost:8080/api/actuator/health

### Endpoints Principales

#### Autenticación
```bash
# Login (sin autenticación por ahora)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

#### Usuarios
```bash
# Listar usuarios
curl http://localhost:8080/api/users

# Crear usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "idCard": "12345678",
    "fullName": "Juan Pérez",
    "email": "juan.perez@clinica.com",
    "phone": "3001234567",
    "birthDate": "1980-01-15",
    "address": "Calle 123 #45-67",
    "role": "ADMINISTRATIVE",
    "username": "jperez",
    "password": "password123"
  }'
```

## 🗄️ Configuración de Base de Datos

### Desarrollo (H2)
- **Automático**: Se crea en memoria al iniciar
- **Consola H2**: http://localhost:8080/api/h2-console
- **Credenciales**: usuario `sa`, sin contraseña

### Producción (MySQL)
1. Crear base de datos:
```sql
CREATE DATABASE clinicdb_prod CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Configurar variables de entorno:
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=clinicdb_prod
export DB_USERNAME=clinic_user
export DB_PASSWORD=tu_password_seguro
```

3. Ejecutar aplicación:
```bash
java -jar -Dspring-boot.run.profiles=prod \
  -DDB_PASSWORD=$DB_PASSWORD \
  target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

## 🧪 Testing de la API

### Crear Usuario de Prueba
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "idCard": "12345678",
    "fullName": "María González",
    "email": "maria.gonzalez@clinica.com",
    "phone": "3012345678",
    "birthDate": "1985-03-20",
    "address": "Av. Principal 123",
    "role": "ADMINISTRATIVE",
    "username": "mgonzalez",
    "password": "admin123"
  }'
```

### Listar Usuarios
```bash
curl http://localhost:8080/api/users
```

### Ver Logs
```bash
# Ver logs en tiempo real
tail -f logs/clinic-app.log

# Ver solo errores
tail -f logs/clinic-app-error.log
```

## 🛠️ Desarrollo

### Estructura del Proyecto
```
src/main/java/app/
├── application/          # Servicios de aplicación
│   ├── dto/             # Data Transfer Objects
│   ├── mapper/          # Mapeadores
│   └── service/         # Servicios de negocio
├── domain/              # Lógica de dominio
│   ├── model/          # Entidades de dominio
│   ├── port/           # Puertos (interfaces)
│   └── services/       # Servicios de dominio
├── infrastructure/      # Capa de infraestructura
│   ├── adapter/        # Adaptadores
│   └── config/         # Configuraciones
└── presentation/        # Controladores REST
    └── controller/      # Endpoints API
```

### Agregar Nuevas Funcionalidades

1. **Crear DTOs** en `application/dto/`
2. **Implementar Servicio** en `application/service/`
3. **Crear Controlador** en `presentation/controller/`
4. **Agregar Mapeadores** en `application/mapper/`

## 📊 Monitoreo

### Health Check
```bash
curl http://localhost:8080/api/actuator/health
```

### Información de la Aplicación
```bash
curl http://localhost:8080/api/actuator/info
```

### Métricas
```bash
curl http://localhost:8080/api/actuator/metrics
```

## 🚨 Solución de Problemas

### La aplicación no inicia
```bash
# Verificar logs detallados
java -jar -Dlogging.level.app=DEBUG \
  -Dspring-boot.run.profiles=dev \
  target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

### Error de base de datos
```bash
# Verificar conexión MySQL
mysql -h localhost -P 3306 -u root -p

# Crear base de datos si no existe
CREATE DATABASE clinicdb_prod;
```

### Puerto ocupado
```bash
# Buscar proceso usando el puerto
lsof -i :8080

# O usar otro puerto
java -jar -Dserver.port=8081 \
  -Dspring-boot.run.profiles=dev \
  target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

## 📝 Notas Importantes

- **Sin Autenticación**: Actualmente los endpoints no requieren autenticación
- **Base de Datos**: Se crea automáticamente en modo desarrollo
- **Logs**: Se generan en la carpeta `logs/`
- **Configuración**: Usa variables de entorno para datos sensibles

## 🤝 Contribución

1. Crear rama para nueva funcionalidad
2. Implementar cambios
3. Agregar tests
4. Crear Pull Request

---

**¡La aplicación está lista para usar!** 🎉