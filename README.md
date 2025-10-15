# 🏥 Sistema de Gestión Clínica

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

<div align="center">
  <h3>🏥 Gestión Integral de Clínicas y Centros Médicos</h3>
  <p>Solución completa para la administración de servicios médicos, pacientes y personal clínico</p>
</div>

---

## 📋 Tabla de Contenidos

- [🏥 Sistema de Gestión Clínica](#-sistema-de-gestión-clínica)
  - [📋 Tabla de Contenidos](#-tabla-de-contenidos)
  - [🎯 Visión General](#-visión-general)
  - [🏗️ Arquitectura del Sistema](#️-arquitectura-del-sistema)
  - [✨ Características Principales](#-características-principales)
  - [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
  - [🚀 Inicio Rápido](#-inicio-rápido)
  - [📁 Estructura del Proyecto](#-estructura-del-proyecto)
  - [🔧 Instalación y Configuración](#-instalación-y-configuración)
  - [🗄️ Base de Datos](#️-base-de-datos)
  - [🔐 Seguridad](#-seguridad)
  - [🧪 Pruebas y Calidad](#-pruebas-y-calidad)
  - [📊 Monitoreo y Logs](#-monitoreo-y-logs)
  - [🚢 Despliegue](#-despliegue)
  - [📚 Documentación](#-documentación)
  - [👥 Equipo de Desarrollo](#-equipo-de-desarrollo)
  - [🤝 Contribución](#-contribución)
  - [📄 Licencia](#-licencia)
  - [🙏 Agradecimientos](#-agradecimientos)

## 🎯 Visión General

El **Sistema de Gestión Clínica** es una solución integral y moderna para la administración completa de clínicas, centros médicos y consultorios. Desarrollado con tecnologías de vanguardia, proporciona herramientas avanzadas para:

- ✅ **Gestión integral de pacientes** con historial médico completo
- ✅ **Sistema de citas médicas** con programación inteligente
- ✅ **Control de inventario médico** y gestión de medicamentos
- ✅ **Facturación automática** con soporte para seguros médicos
- ✅ **Historia clínica digital** con almacenamiento seguro
- ✅ **Gestión de personal médico** con roles y permisos granulares

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      Sistema de Gestión Clínica                          │
├─────────────────────────────────────────────────────────────────────────┤
│  🌐 Frontend (React + TypeScript)                                      │
│  🔗 API Gateway (Spring Cloud Gateway)                                 │
│  🔐 Servicio de Autenticación (Spring Security + JWT)                   │
├─────────────────────────────────────────────────────────────────────────┤
│  📋 Servicio de Pacientes     │  📅 Servicio de Citas                   │
│  💊 Servicio de Inventario    │  💰 Servicio de Facturación             │
│  👥 Servicio de Usuarios      │  📋 Servicio de Historia Clínica        │
├─────────────────────────────────────────────────────────────────────────┤
│  🗄️ PostgreSQL (Datos Relacionales)                                    │
│  🍃 MongoDB (Historia Clínica NoSQL)                                   │
│  🔄 Redis (Cache y Sesiones)                                           │
└─────────────────────────────────────────────────────────────────────────┘
```

### **Patrones Arquitectónicos**
- **Microservicios** - Servicios independientes y escalables
- **Domain-Driven Design (DDD)** - Modelo de dominio rico
- **CQRS** - Separación de comandos y consultas
- **Event Sourcing** - Auditoría completa de cambios
- **API-First** - Diseño centrado en APIs RESTful

## ✨ Características Principales

### 👥 **Gestión Integral de Usuarios**
- **Autenticación segura** con JWT y refresh tokens
- **Autorización granular** basada en roles médicos
- **Gestión de personal** médico y administrativo
- **Perfiles especializados** para diferentes tipos de usuario

### 🏥 **Gestión Avanzada de Pacientes**
- **Registro completo** de información del paciente
- **Contactos de emergencia** y familiares
- **Pólizas de seguros médicos** con validación automática
- **Historial médico integrado** con evolución del paciente

### 📅 **Sistema Inteligente de Citas**
- **Programación automática** considerando disponibilidad
- **Recordatorios inteligentes** vía múltiples canales
- **Gestión de conflictos** de horarios médicos
- **Estados en tiempo real** de citas y consultas

### 💊 **Control de Inventario Médico**
- **Catálogo completo** de medicamentos y suministros
- **Alertas automáticas** de vencimiento y stock mínimo
- **Órdenes de reposición** automáticas
- **Trazabilidad completa** de lotes médicos

### 💰 **Facturación y Administración Financiera**
- **Cálculo automático** de copagos según pólizas
- **Soporte multi-seguro** con reglas complejas
- **Facturación electrónica** integrada
- **Reportes financieros** en tiempo real

### 📋 **Historia Clínica Digital**
- **Registros médicos** estructurados y no estructurados
- **Signos vitales** con gráficos de evolución
- **Diagnósticos y tratamientos** con seguimiento
- **Archivos adjuntos** médicos (imágenes, PDFs)

## 🛠️ Tecnologías Utilizadas

### **Backend Stack**
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 17 | Lenguaje de programación principal |
| **Spring Boot** | 3.0.0 | Framework de aplicaciones |
| **Spring Security** | 6.0 | Seguridad y autenticación |
| **PostgreSQL** | 15 | Base de datos relacional |
| **MongoDB** | 7.0 | Base de datos NoSQL |
| **Redis** | 7.0 | Sistema de caché |

### **Frontend Stack**
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **React** | 18.2 | Librería de interfaz de usuario |
| **TypeScript** | 5.0 | Tipado estático |
| **Vite** | 4.0 | Build tool moderno |
| **Tailwind CSS** | 3.0 | Framework CSS utilitario |
| **React Query** | 4.0 | Gestión de estado de servidor |

### **DevOps & Herramientas**
| Tecnología | Descripción |
|------------|-------------|
| **Docker** | Containerización |
| **Docker Compose** | Orquestación de servicios |
| **Maven** | Gestión de dependencias Java |
| **NPM** | Gestión de dependencias Node.js |
| **Git** | Control de versiones |

## 🚀 Inicio Rápido

### **1. Prerrequisitos**
- Docker y Docker Compose
- Git
- Navegador web moderno

### **2. Instalación con Docker (Recomendado)**

```bash
# Clonar el repositorio completo
git clone <repository-url>
cd clinica

# Iniciar todos los servicios
docker-compose up -d

# Verificar estado de servicios
docker-compose ps

# Acceder a la aplicación
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Documentación API: http://localhost:8080/swagger-ui.html
```

### **3. Instalación Manual**

#### **Backend**
```bash
cd clinic
mvn clean install
mvn spring-boot:run
```

#### **Frontend**
```bash
cd frontend
npm install
npm run dev
```

## 📁 Estructura del Proyecto

```
clinica/
├── clinic/                    # Backend - Spring Boot
│   ├── src/main/java/app/clinic/
│   │   ├── application/       # Capa de aplicación
│   │   ├── domain/           # Capa de dominio (DDD)
│   │   └── infrastructure/   # Capa de infraestructura
│   ├── src/test/             # Pruebas automatizadas
│   └── docker-compose.yml    # Servicios backend
├── frontend/                 # Frontend - React + TypeScript
│   ├── src/
│   │   ├── components/       # Componentes React
│   │   ├── services/         # Servicios API
│   │   └── stores/          # Gestión de estado
│   └── public/              # Archivos estáticos
├── docker-compose.yml       # Servicios completos
├── README.md               # Esta documentación
├── clinic/README.md        # Documentación backend
└── frontend/README.md      # Documentación frontend
```

## 🔧 Instalación y Configuración

### **Configuración con Docker Compose**

```yaml
version: '3.8'
services:
  # Base de datos PostgreSQL
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: clinic_management
      POSTGRES_USER: clinic_user
      POSTGRES_PASSWORD: clinic_password_2024

  # Base de datos MongoDB
  mongodb:
    image: mongo:7-jammy
    environment:
      MONGO_INITDB_DATABASE: clinic_history

  # Aplicación Backend
  app:
    build: ./clinic
    depends_on:
      - postgres
      - mongodb

  # Aplicación Frontend
  frontend:
    build: ./frontend
    ports:
      - "3000:80"
```

### **Variables de Entorno Importantes**

```bash
# Base de datos
DATABASE_URL=postgresql://clinic_user:clinic_password_2024@postgres:5432/clinic_management
MONGODB_URL=mongodb://mongodb:27017/clinic_history

# JWT Security
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRATION=86400000

# Aplicación
NODE_ENV=production
API_BASE_URL=http://localhost:8080/api
```

## 🗄️ Base de Datos

### **Arquitectura de Datos**
- **PostgreSQL** para datos relacionales estructurados
- **MongoDB** para historia clínica no estructurada
- **Redis** para caché y sesiones de usuario

### **Esquema Principal**

#### **Usuarios del Sistema**
```sql
CREATE TABLE users (
    cedula VARCHAR(20) PRIMARY KEY,
    username VARCHAR(15) UNIQUE,
    password_hash VARCHAR(255),
    full_name VARCHAR(100),
    role VARCHAR(30),
    active BOOLEAN DEFAULT true
);
```

#### **Historia Clínica (MongoDB)**
```javascript
{
    "patientNationalId": "CC-12345678",
    "records": [
        {
            "date": "2024-01-15T10:30:00Z",
            "diagnosis": "Hipertensión arterial",
            "vitalSigns": {
                "bloodPressureSystolic": 140,
                "bloodPressureDiastolic": 90
            }
        }
    ]
}
```

## 🔐 Seguridad

### **Características de Seguridad**
- **Autenticación JWT** con refresh tokens
- **Autorización RBAC** (Role-Based Access Control)
- **Encriptación AES-256** para datos sensibles
- **Validación estricta** de entrada y salida
- **Protección contra ataques comunes**:
  - SQL Injection
  - XSS (Cross-Site Scripting)
  - CSRF (Cross-Site Request Forgery)
  - Clickjacking

### **Roles y Permisos**

| Rol | Descripción | Permisos Clave |
|-----|-------------|----------------|
| **ADMIN** | Administrador completo | Todos los permisos |
| **HUMAN_RESOURCES** | Recursos Humanos | Gestión de usuarios |
| **ADMINISTRATIVE_STAFF** | Personal Administrativo | Gestión de pacientes |
| **SUPPORT_STAFF** | Personal de Soporte | Gestión de inventario |
| **DOCTOR** | Médico | Crear historias clínicas |
| **NURSE** | Enfermera | Registrar signos vitales |

## 🧪 Pruebas y Calidad

### **Cobertura de Pruebas**
- ✅ **Pruebas Unitarias** (> 85% cobertura)
- ✅ **Pruebas de Integración** (flujos completos)
- ✅ **Pruebas E2E** (escenarios reales)
- ✅ **Pruebas de Seguridad** (OWASP Top 10)
- ✅ **Pruebas de Performance** (carga y estrés)

### **Ejecutar Pruebas**

```bash
# Backend - Todas las pruebas
cd clinic && mvn test

# Frontend - Todas las pruebas
cd frontend && npm run test

# Pruebas E2E
npm run test:e2e

# Cobertura de código
mvn test jacoco:report
```

## 📊 Monitoreo y Logs

### **Métricas Disponibles**
- Health checks automáticos
- Métricas de JVM y aplicación
- Métricas de base de datos
- Métricas de negocio personalizadas
- Logs estructurados con niveles apropiados

### **Acceso a Información**

```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Métricas de aplicación
curl http://localhost:8080/actuator/metrics

# Información detallada
curl http://localhost:8080/actuator/info
```

## 🚢 Despliegue

### **Entornos Disponibles**
- **Desarrollo** (`dev`) - H2 Database, logs detallados
- **Producción** (`prod`) - PostgreSQL + MongoDB, configuración optimizada

### **Despliegue en Producción**

```bash
# Construir imágenes
docker-compose -f docker-compose.prod.yml build

# Desplegar servicios
docker-compose -f docker-compose.prod.yml up -d

# Actualizar aplicación
docker-compose -f docker-compose.prod.yml pull
docker-compose -f docker-compose.prod.yml up -d --no-deps app frontend
```

### **Plataformas Cloud Soportadas**
- ✅ **AWS** (ECS, EKS, Lambda)
- ✅ **Google Cloud** (Cloud Run, GKE)
- ✅ **Azure** (Container Instances, AKS)
- ✅ **DigitalOcean** (App Platform, Kubernetes)

## 📚 Documentación

### **Documentación Técnica**
- [📖 Guía de Arquitectura](docs/architecture.md)
- [🔧 Guía de Despliegue](docs/deployment.md)
- [🗄️ Guía de Base de Datos](docs/database.md)
- [🔐 Guía de Seguridad](docs/security.md)
- [🧪 Guía de Pruebas](docs/testing.md)

### **Documentación de APIs**
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api-docs
- **Postman Collection**: Disponible en `/docs`

## 👥 Equipo de Desarrollo

### **Arquitectura y Backend**
| Rol | Nombre | Especialidad |
|-----|--------|--------------|
| **Arquitecto Principal** | Santiago Suaza | Arquitectura, DDD, Microservicios |
| **Tech Lead Backend** | Equipo Backend | Spring Boot, Bases de Datos |
| **DevOps Engineer** | Equipo DevOps | Docker, CI/CD, Cloud |

### **Frontend y UX**
| Rol | Nombre | Especialidad |
|-----|--------|--------------|
| **Tech Lead Frontend** | Equipo Frontend | React, TypeScript, UX |
| **UI/UX Designer** | Equipo Diseño | Diseño de interfaces |
| **QA Engineer** | Equipo QA | Testing, Calidad |

## 🤝 Contribución

### **Cómo Contribuir**
1. **Fork** el repositorio
2. **Crear rama** para nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. **Desarrollar** siguiendo estándares del proyecto
4. **Probar** cambios exhaustivamente
5. **Crear Pull Request** con descripción detallada

### **Estándares de Desarrollo**
- **Commits**: [Conventional Commits](https://conventionalcommits.org/)
- **Código**: Google Java Style Guide + Airbnb JavaScript Guide
- **PRs**: Template estructurado con descripción y pruebas
- **Reviews**: Aprobación de al menos 2 desarrolladores senior

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 🙏 Agradecimientos

Agradecemos a todas las personas e instituciones que han contribuido al desarrollo de este sistema:

- **Comunidad Spring Boot** por el excelente framework
- **Comunidad React** por las herramientas de desarrollo frontend
- **Profesionales de la salud** que proporcionaron requisitos médicos reales
- **Equipo de desarrollo** por su dedicación y profesionalismo

---

<div align="center">

## 🏥 Sistema de Gestión Clínica

**Transformando la gestión médica con tecnología moderna**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)

### 📞 Contacto
- **Email**: info@clinica.com
- **Teléfono**: +57 300 123 4567
- **Dirección**: Carrera 123 #45-67, Bogotá, Colombia

### 🌐 Enlaces Importantes
- [📚 Documentación Completa](docs/)
- [🐛 Reportar Problemas](https://github.com/clinica/issues)
- [💬 Foro de la Comunidad](https://github.com/clinica/discussions)
- [📖 Wiki del Proyecto](https://github.com/clinica/wiki)

### 🎯 Estado del Proyecto
- **Versión**: 1.0.0
- **Estado**: ✅ Activo y en Desarrollo
- **Última Actualización**: Octubre 2024
- **Próxima Versión**: 1.1.0 (Diciembre 2024)

---

*"Tecnología al servicio de la salud - Desarrollado con ❤️ para mejorar vidas"*

</div>