# Sistema de Gestión de Clínica IPS 2024

Aplicación completa de gestión de información para la Clínica IPS 2024, desarrollada con arquitectura hexagonal, principios SOLID y código limpio.

## 🚀 Características Principales

- **Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- **Autenticación JWT**: Control de acceso basado en roles
- **Base de Datos Dual**: PostgreSQL para datos relacionales, MongoDB para registros médicos
- **Validaciones Completas**: Según especificaciones del documento de requisitos
- **API REST**: Endpoints separados por roles con documentación Swagger
- **Migraciones Flyway**: Gestión automática de esquema de base de datos

## 👥 Roles del Sistema

- **Recursos Humanos**: Gestión de usuarios
- **Personal Administrativo**: Gestión de pacientes y facturación
- **Soporte de Información**: Gestión de inventarios
- **Enfermeras**: Registro de signos vitales y administración de medicamentos
- **Médicos**: Creación de órdenes y gestión de registros médicos

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Security** (JWT)
- **Spring Data JPA** (PostgreSQL)
- **Spring Data MongoDB**
- **Flyway** (Migraciones)
- **Lombok**

### Base de Datos
- **H2 In-Memory**: Base de datos embebida para desarrollo y portabilidad (sin necesidad de PostgreSQL/MongoDB)
- **Compatible con PostgreSQL**: Fácil migración a producción
- **Datos de prueba incluidos**: Usuarios, pacientes, medicamentos, procedimientos y registros médicos de ejemplo

### Documentación
- **SpringDoc OpenAPI** (Swagger)

## 📋 Requisitos del Sistema

- Java 17+
- Maven 3.8+
- **No requiere PostgreSQL ni MongoDB** - Usa H2 embebida por defecto
- Para producción: PostgreSQL 12+ (opcional)

## 🚀 Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd clinic
```

### 2. Ejecutar la aplicación (sin configuración adicional)

La aplicación usa H2 embebida por defecto, no requiere configuración de bases de datos externas.

### 3. Acceder a la aplicación

Una vez ejecutada, la aplicación estará disponible en:
- **Aplicación**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console` (usuario: `sa`, sin contraseña)

### 4. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📚 Documentación API

Una vez ejecutada la aplicación, acceder a:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

## 🔐 Credenciales de Prueba

La aplicación incluye datos de prueba. Puedes iniciar sesión con:

- **Recursos Humanos**: `admin_hr` / `password`
- **Personal Administrativo**: `admin_staff` / `password`
- **Soporte IT**: `soporte_it` / `password`
- **Enfermera**: `nurse_ana` / `password`
- **Médico**: `dr_perez` / `password`

## 🔐 Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Inicio de sesión

### Recursos Humanos
- `POST /api/users` - Crear usuario
- `DELETE /api/users/{id}` - Eliminar usuario

### Personal Administrativo
- `POST /api/patients` - Registrar paciente
- `PUT /api/patients/{id}` - Actualizar paciente
- `POST /api/appointments` - Programar cita
- `POST /api/billing/generate` - Generar factura

### Soporte de Información
- `PUT /api/inventory/medications/{id}` - Actualizar medicamento
- `PUT /api/inventory/procedures/{id}` - Actualizar procedimiento
- `PUT /api/inventory/diagnostic-aids/{id}` - Actualizar ayuda diagnóstica

### Enfermeras
- `POST /api/vitals` - Registrar signos vitales
- `POST /api/administration/medication` - Registrar administración de medicamento
- `POST /api/administration/procedure` - Registrar realización de procedimiento

### Médicos
- `POST /api/medical-records` - Crear registro médico
- `GET /api/medical-records/{patientId}` - Consultar historia clínica
- `POST /api/orders/medication` - Crear orden de medicamento
- `POST /api/orders/procedure` - Crear orden de procedimiento
- `POST /api/orders/diagnostic-aid` - Crear orden de ayuda diagnóstica

## 🧪 Pruebas

Ejecutar pruebas unitarias:
```bash
mvn test
```

Ejecutar pruebas de integración:
```bash
mvn verify
```

## 📁 Estructura del Proyecto

```
clinic/
├── src/main/java/app/clinic/
│   ├── domain/                    # Capa de dominio
│   │   ├── model/
│   │   │   ├── entities/         # Entidades del dominio
│   │   │   ├── valueobject/      # Objetos de valor
│   │   │   └── DomainException.java
│   │   ├── repository/           # Puertos de repositorio
│   │   └── service/              # Servicios de dominio
│   ├── application/              # Capa de aplicación
│   │   ├── mapper/               # Mappers DTO-Dominio
│   │   └── usecase/              # Casos de uso
│   └── infrastructure/           # Capa de infraestructura
│       ├── config/               # Configuraciones
│       ├── controller/           # Controladores REST
│       ├── dto/                  # Objetos de transferencia
│       ├── exception/            # Manejo de excepciones
│       └── persistence/          # Adaptadores de persistencia
├── src/main/resources/
│   ├── db/migration/             # Migraciones Flyway
│   └── application.properties    # Configuración
└── src/test/                     # Pruebas
```

## 🔒 Validaciones Implementadas

### Usuarios
- Cédula única
- Correo válido con dominio
- Teléfono: 1-10 dígitos
- Fecha nacimiento: máximo 150 años
- Dirección: máximo 30 caracteres
- Rol: selección entre opciones válidas

### Pacientes
- Usuario único, máximo 15 caracteres
- Contraseña: mínimo 8 caracteres, mayúscula, número, carácter especial
- Cédula única
- Teléfono: 10 dígitos
- Fecha nacimiento: máximo 150 años

### Facturación
- Copago $50.000 para póliza activa
- Cobertura total si copago anual > $1.000.000
- Pago total para póliza inactiva

### Órdenes
- Número único de 6 dígitos
- Ítems únicos por orden
- Separación de medicamentos, procedimientos y ayudas diagnósticas

## 🤝 Contribución

1. Fork el proyecto
2. Crear rama para feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

## 📞 Soporte

Para soporte técnico, contactar al equipo de desarrollo.