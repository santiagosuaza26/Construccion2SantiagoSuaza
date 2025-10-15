# 🏥 Sistema de Gestión Clínica - Backend

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Tabla de Contenidos

- [🏥 Sistema de Gestión Clínica - Backend](#-sistema-de-gestión-clínica---backend)
  - [📋 Tabla de Contenidos](#-tabla-de-contenidos)
  - [🎯 Visión General](#-visión-general)
  - [🏗️ Arquitectura](#️-arquitectura)
  - [✨ Características Principales](#-características-principales)
  - [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
  - [🚀 Inicio Rápido](#-inicio-rápido)
  - [📁 Estructura del Proyecto](#-estructura-del-proyecto)
  - [🔧 Configuración](#-configuración)
  - [🗄️ Base de Datos](#️-base-de-datos)
  - [🔐 Seguridad](#-seguridad)
  - [🧪 Pruebas](#-pruebas)
  - [📊 Monitoreo](#-monitoreo)
  - [🚢 Despliegue](#-despliegue)
  - [👥 Equipo de Desarrollo](#-equipo-de-desarrollo)
  - [📚 Documentación API](#-documentación-api)
  - [🤝 Contribución](#-contribución)
  - [📄 Licencia](#-licencia)

## 🎯 Visión General

El backend del Sistema de Gestión Clínica es una aplicación robusta desarrollada con **Spring Boot 3** que proporciona servicios RESTful completos para la gestión integral de clínicas y centros médicos. Implementa una arquitectura de microservicios con separación clara de responsabilidades y sigue los principios de **Domain-Driven Design (DDD)**.

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                        Sistema de Gestión Clínica                │
├─────────────────────────────────────────────────────────────────┤
│  🌐 Presentation Layer (Controllers REST)                       │
│  📝 Application Layer (Services & DTOs)                         │
│  🎯 Domain Layer (Business Logic & Entities)                    │
│  💾 Infrastructure Layer (Repositories & External Services)     │
├─────────────────────────────────────────────────────────────────┤
│  🗄️ PostgreSQL (Datos Relacionales)                            │
│  🍃 MongoDB (Historia Clínica)                                  │
│  🔄 Redis (Cache)                                               │
└─────────────────────────────────────────────────────────────────┘
```

### **Patrón Arquitectónico**
- **Hexagonal Architecture** (Ports & Adapters)
- **Domain-Driven Design** (DDD)
- **CQRS** para operaciones complejas
- **Event Sourcing** para auditoría

## ✨ Características Principales

### 👥 Gestión de Usuarios
- ✅ Autenticación JWT segura
- ✅ Autorización basada en roles
- ✅ Gestión completa de usuarios (CRUD)
- ✅ Validación estricta de datos
- ✅ Encriptación de contraseñas

### 🏥 Gestión de Pacientes
- ✅ Registro completo de pacientes
- ✅ Información de contacto de emergencia
- ✅ Políticas de seguros médicos
- ✅ Validación de datos médicos

### 📅 Gestión de Citas
- ✅ Programación de citas médicas
- ✅ Gestión de disponibilidad de doctores
- ✅ Estados de citas (Programada, Confirmada, Cancelada, Completada)
- ✅ Recordatorios automáticos

### 💊 Gestión de Medicamentos e Inventario
- ✅ Control de inventario médico
- ✅ Gestión de medicamentos
- ✅ Alertas de vencimiento
- ✅ Órdenes de reposición

### 💰 Facturación y Cobros
- ✅ Cálculo automático de copagos
- ✅ Gestión de pólizas de seguros
- ✅ Facturación electrónica
- ✅ Reportes financieros

### 📋 Historia Clínica Digital
- ✅ Registros médicos digitales
- ✅ Signos vitales
- ✅ Diagnósticos y tratamientos
- ✅ Evolución del paciente

## 🛠️ Tecnologías Utilizadas

### **Backend Core**
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.0** - Framework principal
- **Spring Security** - Seguridad y autenticación
- **Spring Data JPA** - Persistencia de datos
- **Spring Validation** - Validación de datos

### **Bases de Datos**
- **PostgreSQL 15** - Base de datos relacional principal
- **MongoDB 7** - Base de datos NoSQL para historia clínica
- **Redis 7** - Sistema de caché distribuido

### **DevOps & Herramientas**
- **Docker & Docker Compose** - Containerización
- **Maven** - Gestión de dependencias
- **Git** - Control de versiones
- **JUnit 5** - Framework de pruebas
- **Mockito** - Mocking para pruebas

### **Documentación y API**
- **Swagger/OpenAPI 3** - Documentación de APIs
- **SpringDoc OpenAPI** - Generación automática de documentación

## 🚀 Inicio Rápido

### **Prerrequisitos**
- Docker y Docker Compose
- Java 17 o superior
- Maven 3.6+
- Git

### **Instalación con Docker (Recomendado)**

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd clinica/backend

# 2. Iniciar servicios con Docker Compose
docker-compose up -d

# 3. Verificar estado de servicios
docker-compose ps

# 4. Acceder a la aplicación
# API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# H2 Console (dev): http://localhost:8080/h2-console
```

### **Instalación Manual**

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd clinica/backend

# 2. Configurar bases de datos
# PostgreSQL: Crear base de datos 'clinic_management'
# MongoDB: Crear base de datos 'clinic_history'

# 3. Compilar aplicación
mvn clean compile

# 4. Ejecutar aplicación
mvn spring-boot:run

# 5. Ejecutar pruebas
mvn test
```

## 📁 Estructura del Proyecto

```
clinic/
├── src/
│   ├── main/
│   │   ├── java/app/clinic/
│   │   │   ├── application/           # Capa de aplicación
│   │   │   │   ├── controller/        # Controladores REST
│   │   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── mapper/           # Mapeadores DTO-Entidad
│   │   │   │   └── service/          # Servicios de aplicación
│   │   │   ├── domain/               # Capa de dominio
│   │   │   │   ├── model/            # Entidades de dominio
│   │   │   │   ├── port/             # Puertos (interfaces)
│   │   │   │   └── service/          # Servicios de dominio
│   │   │   └── infrastructure/       # Capa de infraestructura
│   │   │       ├── adapter/          # Adaptadores de repositorio
│   │   │       ├── entity/           # Entidades JPA
│   │   │       └── repository/       # Repositorios JPA
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── init-scripts/         # Scripts de inicialización
│   └── test/                         # Pruebas automatizadas
├── docker-compose.yml                # Configuración Docker
├── Dockerfile                       # Imagen de aplicación
├── pom.xml                         # Dependencias Maven
└── README.md                       # Esta documentación
```

## 🔧 Configuración

### **Perfiles de Spring**

| Perfil | Descripción | Puerto | Base de Datos |
|--------|-------------|--------|---------------|
| `dev` | Desarrollo | 8080 | H2 In-Memory |
| `prod` | Producción | 8080 | PostgreSQL + MongoDB |

### **Variables de Entorno Importantes**

```bash
# Base de datos PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/clinic_management
SPRING_DATASOURCE_USERNAME=clinic_user
SPRING_DATASOURCE_PASSWORD=clinic_password_2024

# Base de datos MongoDB
SPRING_DATA_MONGODB_HOST=localhost
SPRING_DATA_MONGODB_PORT=27017
SPRING_DATA_MONGODB_DATABASE=clinic_history

# JWT Security
APP_SECURITY_JWT_SECRET=your-secret-key-here
APP_SECURITY_JWT_EXPIRATION=86400000
```

## 🗄️ Base de Datos

### **PostgreSQL - Datos Relacionales**
```sql
-- Usuarios del sistema
CREATE TABLE users (
    cedula VARCHAR(20) PRIMARY KEY,
    username VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN DEFAULT true
);

-- Pacientes
CREATE TABLE patients (
    cedula VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    emergency_contact VARCHAR(100),
    insurance_policy VARCHAR(50)
);
```

### **MongoDB - Historia Clínica**
```javascript
// Colección de registros clínicos
db.clinical_records.insertOne({
    patientNationalId: "CC-12345678",
    records: [
        {
            date: "2024-01-15T10:30:00Z",
            diagnosis: "Hipertensión arterial",
            vitalSigns: {
                bloodPressureSystolic: 140,
                bloodPressureDiastolic: 90
            }
        }
    ]
});
```

## 🔐 Seguridad

### **Características de Seguridad**
- ✅ **Autenticación JWT** con tokens seguros
- ✅ **Autorización basada en roles** (RBAC)
- ✅ **Encriptación de contraseñas** (bcrypt)
- ✅ **Validación estricta de entrada**
- ✅ **Protección contra ataques comunes** (SQL Injection, XSS, CSRF)
- ✅ **Rate limiting** para prevenir ataques de fuerza bruta
- ✅ **Logging de seguridad** completo

### **Roles Disponibles**
| Rol | Descripción | Permisos |
|-----|-------------|----------|
| `ADMIN` | Administrador completo | Todos los permisos |
| `HUMAN_RESOURCES` | Recursos Humanos | Gestión de usuarios |
| `ADMINISTRATIVE_STAFF` | Personal Administrativo | Gestión de pacientes |
| `SUPPORT_STAFF` | Personal de Soporte | Gestión de inventario |
| `DOCTOR` | Médico | Crear historias clínicas |
| `NURSE` | Enfermera | Registrar signos vitales |

## 🧪 Pruebas

### **Ejecutar Todas las Pruebas**
```bash
# Pruebas unitarias
mvn test

# Pruebas de integración
mvn verify

# Cobertura de pruebas
mvn test jacoco:report
```

### **Tipos de Pruebas Implementadas**
- ✅ **Pruebas Unitarias** - Servicios individuales
- ✅ **Pruebas de Integración** - Flujos completos
- ✅ **Pruebas de Repositorio** - Acceso a datos
- ✅ **Pruebas de Seguridad** - Autenticación y autorización
- ✅ **Pruebas de Performance** - Rendimiento bajo carga

## 📊 Monitoreo

### **Métricas Disponibles**
- Health checks automáticos
- Métricas de JVM
- Métricas de base de datos
- Métricas de caché Redis
- Métricas personalizadas de negocio

### **Acceso a Métricas**
```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Métricas generales
curl http://localhost:8080/actuator/metrics

# Información de la aplicación
curl http://localhost:8080/actuator/info
```

## 🚢 Despliegue

### **Producción con Docker**
```bash
# Construir imagen
docker build -t clinica-backend:latest .

# Desplegar con docker-compose
docker-compose -f docker-compose.prod.yml up -d

# Actualizar aplicación
docker-compose -f docker-compose.prod.yml pull
docker-compose -f docker-compose.prod.yml up -d --no-deps app
```

### **Despliegue en Cloud**
- ✅ **Docker Hub** - Imágenes oficiales
- ✅ **AWS ECS** - Amazon Container Service
- ✅ **Google Cloud Run** - Serverless deployment
- ✅ **Azure Container Instances** - Microsoft Azure

## 👥 Equipo de Desarrollo

| Rol | Nombre | Contacto |
|-----|--------|----------|
| **Arquitecto Principal** | Santiago Suaza | santiago.suaza@clinica.com |
| **Desarrollador Backend** | Equipo Backend | backend@clinica.com |
| **DevOps Engineer** | Equipo DevOps | devops@clinica.com |
| **QA Engineer** | Equipo QA | qa@clinica.com |

## 📚 Documentación API

### **Swagger UI**
- **URL**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **Formato**: OpenAPI 3.0

### **Endpoints Principales**

#### **Autenticación**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin123!@#"
}
```

#### **Usuarios**
```http
GET /api/users/role/DOCTOR
Authorization: Bearer <token>

POST /api/users
Authorization: Bearer <token>
Content-Type: application/json

{
  "cedula": "12345678",
  "username": "doctor01",
  "password": "Doctor123!@#",
  "fullName": "Dr. Juan Pérez",
  "role": "DOCTOR"
}
```

## 🤝 Contribución

### **Guías para Contribuidores**
1. Fork el repositorio
2. Crear rama para nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

### **Estándares de Código**
- **Java Code Style** - Google Java Style Guide
- **Commits** - Conventional Commits
- **Pull Requests** - Template estructurado
- **Revisión de Código** - Al menos 2 aprobaciones

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

<div align="center">

**🏥 Sistema de Gestión Clínica - Backend**

*Desarrollado con ❤️ para mejorar la atención médica*

[📧 Contacto](mailto:info@clinica.com) • [🐛 Reportar Bug](https://github.com/clinica/backend/issues) • [📖 Wiki](https://github.com/clinica/backend/wiki)

</div>