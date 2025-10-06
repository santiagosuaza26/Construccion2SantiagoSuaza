# Clinic Management System (CS2)

## 📋 Descripción

Sistema integral de gestión de clínica desarrollado en Java 17 con Spring Boot, siguiendo principios de arquitectura hexagonal y mejores prácticas de desarrollo.

## 🚀 Características Principales

### 👥 Gestión de Usuarios
- **Roles**: Recursos Humanos, Personal Administrativo, Soporte de Información, Enfermeras, Médicos
- **Autenticación** y autorización basada en roles
- **Gestión de permisos** granular según responsabilidades

### 🏥 Gestión de Pacientes
- Registro completo de información personal
- Contactos de emergencia
- Pólizas de seguro médico
- Información de facturación

### 📅 Gestión de Citas
- Programación de citas médicas
- Seguimiento de estados (Programada, Confirmada, En Curso, Completada, Cancelada)
- Asignación de médicos y pacientes

### 💊 Gestión de Inventario
- **Medicamentos**: Registro, costos, disponibilidad
- **Procedimientos**: Catálogo de servicios médicos
- **Ayudas Diagnósticas**: Exámenes y estudios

### 📋 Órdenes Médicas
- Prescripciones de medicamentos
- Solicitudes de procedimientos
- Ayudas diagnósticas
- Seguimiento de órdenes por paciente

### 📊 Registros Médicos
- Historia clínica estructurada
- Almacenamiento NoSQL para flexibilidad
- Registro de consultas y tratamientos

### 👩‍⚕️ Visitas de Pacientes
- Registro de signos vitales
- Seguimiento de atención
- Notas de enfermería

## 🛠 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (En memoria para desarrollo)
- **Maven** (Gestión de dependencias)
- **JUnit 5** (Pruebas unitarias)
- **Mockito** (Mocking para pruebas)
- **OpenAPI/Swagger** (Documentación API)

## 🏗 Arquitectura

### Arquitectura Hexagonal
```
Application Layer (Casos de uso)
├── Controllers
├── DTOs
├── Services
└── Mappers

Domain Layer (Lógica de negocio)
├── Entities
├── Value Objects
├── Ports (Interfaces)
├── Services
└── Exceptions

Infrastructure Layer (Implementaciones técnicas)
├── Adapters (Implementan puertos)
├── Entities (JPA)
├── Repositories (JPA)
├── Services
└── Configuration
```

### Principios SOLID
- ✅ **S** - Single Responsibility Principle
- ✅ **O** - Open/Closed Principle
- ✅ **L** - Liskov Substitution Principle
- ✅ **I** - Interface Segregation Principle
- ✅ **D** - Dependency Inversion Principle

## 🚀 Cómo Ejecutar

### Prerrequisitos
- Java 17 o superior
- MySQL Server
- Maven 3.6+

### 1. Configuración de Base de Datos

```sql
CREATE DATABASE clinic_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'clinic_user'@'localhost' IDENTIFIED BY 'clinic_password';
GRANT ALL PRIVILEGES ON clinic_db.* TO 'clinic_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configuración de Properties

El archivo `src/main/resources/application.properties` ya está configurado con:
- Conexión a MySQL
- Configuración de JPA/Hibernate
- Configuración de seguridad
- Configuración de logging

### 3. Compilación y Ejecución

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar pruebas
mvn test

# Empaquetar la aplicación
mvn package

# Ejecutar la aplicación
mvn spring-boot:run

# O ejecutar directamente el JAR
java -jar target/clinic-0.0.1-SNAPSHOT.jar
```

### 4. Acceso a la Aplicación

- **Aplicación**: http://localhost:8080
- **Documentación API**: http://localhost:8080/swagger-ui.html
- **Consola H2** (desarrollo): http://localhost:8080/h2-console

## 🔐 Usuarios de Prueba

El sistema incluye inicialización automática de usuarios de prueba:

### Recursos Humanos
- **Usuario**: `admin`
- **Contraseña**: `Admin123!@#`
- **Rol**: HUMAN_RESOURCES

### Médico
- **Usuario**: `doctor`
- **Contraseña**: `Doctor123!@#`
- **Rol**: DOCTOR

## 📊 Base de Datos

### Tablas Principales
- `users` - Gestión de usuarios del sistema
- `patients` - Información de pacientes
- `appointments` - Citas médicas
- `inventory_items` - Inventario médico
- `orders` - Órdenes médicas principales
- `medication_orders` - Detalle de medicamentos
- `procedure_orders` - Detalle de procedimientos
- `diagnostic_aid_orders` - Detalle de ayudas diagnósticas
- `medical_records` - Registros médicos
- `patient_visits` - Visitas de pacientes
- `vital_signs` - Signos vitales
- `emergency_contacts` - Contactos de emergencia
- `insurance_policies` - Pólizas de seguro

## 🧪 Pruebas

### Ejecutar Pruebas Unitarias
```bash
mvn test
```

### Cobertura de Pruebas
- ✅ Adaptadores de repositorio
- ✅ Servicios de infraestructura
- ✅ Mapeo de objetos
- ✅ Validaciones de negocio

## 📝 API Endpoints

### Autenticación
- `POST /api/auth/login` - Inicio de sesión

### Usuarios
- `GET /api/users` - Listar usuarios (Solo RRHH)
- `POST /api/users` - Crear usuario (Solo RRHH)
- `PUT /api/users/{id}` - Actualizar usuario (Solo RRHH)
- `DELETE /api/users/{id}` - Eliminar usuario (Solo RRHH)

### Pacientes
- `GET /api/patients` - Listar pacientes (Personal Administrativo+)
- `POST /api/patients` - Registrar paciente (Personal Administrativo+)
- `GET /api/patients/{id}` - Ver paciente específico
- `PUT /api/patients/{id}` - Actualizar paciente

### Citas
- `GET /api/appointments` - Listar citas
- `POST /api/appointments` - Crear cita
- `GET /api/appointments/patient/{cedula}` - Citas por paciente
- `GET /api/appointments/doctor/{cedula}` - Citas por médico

### Inventario
- `GET /api/inventory` - Listar items de inventario
- `POST /api/inventory/medications` - Crear medicamento
- `POST /api/inventory/procedures` - Crear procedimiento
- `POST /api/inventory/diagnostic-aids` - Crear ayuda diagnóstica

## 🔒 Seguridad

### Roles y Permisos
- **HUMAN_RESOURCES**: Gestión completa de usuarios
- **ADMINISTRATIVE_STAFF**: Gestión de pacientes y facturación
- **INFORMATION_SUPPORT**: Gestión de inventario
- **NURSE**: Registro de signos vitales y visitas
- **DOCTOR**: Gestión de registros médicos y órdenes

## 📈 Monitoreo

### Logging
- Configuración avanzada en `LoggingConfig.java`
- Niveles de log configurables
- Monitoreo de operaciones críticas

### Métricas
- Endpoints de health check
- Métricas de Spring Boot Actuator

## 🔧 Desarrollo

### Estructura del Proyecto
```
src/
├── main/
│   ├── java/app/clinic/
│   │   ├── application/     # Capa de aplicación
│   │   ├── domain/          # Capa de dominio
│   │   └── infrastructure/  # Capa de infraestructura
│   └── resources/
│       └── application.properties
└── test/                    # Pruebas unitarias
```

### Comandos Útiles
```bash
# Compilar
mvn compile

# Ejecutar pruebas
mvn test

# Análisis estático
mvn checkstyle:check

# Generar documentación
mvn javadoc:javadoc

# Limpiar y empaquetar
mvn clean package
```

## 🤝 Contribución

1. Crear rama para nueva funcionalidad
2. Implementar cambios siguiendo principios SOLID
3. Agregar pruebas unitarias
4. Actualizar documentación
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

## 🆘 Soporte y Solución de Problemas

### ⚡ Inicio Rápido (Sin Configuración)
```bash
# Ejecutar con perfil de desarrollo (¡Funciona inmediatamente!)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 🔧 Solución de Problemas Comunes

#### ❌ "La aplicación no inicia"
**Problema:** La aplicación estaba configurada para MySQL pero no estaba instalado/configurado.

**✅ Solución:** Ahora usa **H2 en memoria** automáticamente:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### ❌ "Error de conexión a base de datos"
**Problema:** Configuración MySQL faltante.

**✅ Solución:** H2 se configura automáticamente:
- **URL JDBC:** `jdbc:h2:mem:clinic_db_dev`
- **Usuario:** `sa`
- **Contraseña:** (vacío)
- **Consola web:** http://localhost:8080/h2-console

#### ❌ "Dependencias faltantes"
**Problema:** Errores de compilación.

**✅ Solución:**
```bash
mvn clean install
mvn dependency:resolve
```

#### ❌ "Puerto ocupado"
**Problema:** Puerto 8080 en uso.

**✅ Solución:** Cambiar puerto en `application-dev.properties`:
```properties
server.port=8081
```

### 📊 Verificación de Funcionamiento

1. **Iniciar aplicación:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. **Verificar logs:**
   - Buscar: `Started Cs2Application`
   - Buscar: `H2 console available`

3. **Acceder a servicios:**
   - Aplicación: http://localhost:8080
   - API Docs: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### 🧪 Pruebas de Funcionalidad

Una vez iniciada la aplicación, prueba estos endpoints:

```bash
# Crear usuario (POST /api/users)
# Crear paciente (POST /api/patients)
# Crear orden médica (POST /api/orders)
# Crear registro médico (POST /api/medical-records)
```

---

**¡El sistema está listo para usar!** 🚀