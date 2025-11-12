# Sistema de Gestión de Clínica Médica

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)](https://spring.io/projects/spring-boot)
[![H2 Database](https://img.shields.io/badge/H2-Database-blue)](https://www.h2database.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Sistema integral para la gestión de clínicas médicas desarrollado con arquitectura limpia. Trabajo final del semestre para la clase **Construcción de Software 2**.

**Desarrollado por: Santiago Suaza Cardona**

## 🔗 Acceso Rápido a la API

La aplicación está ejecutándose en **http://localhost:8080**

- **🏠 Página Principal**: http://localhost:8080/api/public/welcome
- **📚 Documentación Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **📖 API Docs (JSON)**: http://localhost:8080/v3/api-docs
- **💾 Consola H2 Database**: http://localhost:8080/h2-console
- **❤️ Health Check**: http://localhost:8080/api/public/health
- **ℹ️ Información API**: http://localhost:8080/api/public/info

### Credenciales de Prueba
- **Usuario**: jperez (jperez)
- **Contraseña**: Password123!

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Despliegue](#-instalación-y-despliegue)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Documentation](#-api-documentation)
- [Desarrollo](#-desarrollo)
- [Testing](#-testing)
- [Monitoreo](#-monitoreo)
- [Solución de Problemas](#-solución-de-problemas)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

## ✨ Características

### 👥 Gestión de Usuarios y Roles

- Autenticación JWT con roles (Admin, Doctor, Enfermera, Soporte)
- Control de acceso basado en roles (RBAC)
- Gestión de perfiles de usuario
- Validación de datos de usuario

### 🏥 Gestión de Pacientes

- Registro completo de pacientes
- Historial médico electrónico
- Información de contacto de emergencia
- Seguimiento de signos vitales

### 📅 Gestión de Citas

- Programación de citas médicas
- Gestión de estados de citas
- Recordatorios automáticos
- Cancelación y reprogramación

### 📋 Registros Médicos

- Historial médico completo
- Registros de diagnósticos y tratamientos
- Seguimiento de medicamentos
- Observaciones médicas

### 💊 Gestión de Órdenes Médicas

- Órdenes de medicamentos
- Órdenes de procedimientos
- Órdenes de ayudas diagnósticas
- Seguimiento de ejecución

### 💰 Facturación y Pagos

- Generación automática de facturas
- Integración con seguros médicos
- Seguimiento de pagos
- Reportes financieros

### 🛠️ Soporte Técnico

- Sistema de tickets de soporte
- Gestión de problemas técnicos
- Seguimiento de resoluciones

## 🏗️ Arquitectura

El sistema sigue una arquitectura limpia (Clean Architecture) con separación clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend Layer                           │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  Next.js + TypeScript + Tailwind CSS                   │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                 Application Layer                           │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  Use Cases, DTOs, Mappers                              │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                  Domain Layer                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  Entities, Value Objects, Services, Repositories       │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│               Infrastructure Layer                          │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  Controllers, Persistence, External Services           │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                 External Systems                            │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  PostgreSQL, MongoDB, Redis, Docker                     │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Componentes Principales

- **Backend (Spring Boot)**: API RESTful con arquitectura hexagonal
- **Frontend (Next.js)**: Interfaz moderna y responsiva
- **PostgreSQL**: Base de datos relacional para datos estructurados
- **MongoDB**: Base de datos NoSQL para registros médicos
- **Redis**: Cache y sesiones
- **Docker**: Contenedorización completa

## 🛠️ Tecnologías

### Backend

- **Java 17** - Lenguaje principal
- **Spring Boot 3.5.7** - Framework web
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Acceso datos relacionales
- **Spring Data MongoDB** - Acceso datos NoSQL
- **Spring Data Redis** - Cache y sesiones
- **JWT** - Autenticación stateless
- **Flyway** - Migraciones BD
- **OpenAPI/Swagger** - Documentación API
- **Bucket4j** - Rate limiting
- **Lombok** - Reducción boilerplate

### Frontend

- **Next.js 16.0.1** - Framework React
- **TypeScript 5** - Tipado estático
- **React 19.2.0** - Biblioteca UI
- **Tailwind CSS** - Framework CSS
- **ESLint** - Linting

### Bases de Datos y Cache

- **PostgreSQL 15** - BD relacional
- **MongoDB 7** - BD documentos
- **Redis 7** - Cache y sesiones
- **H2 Database** - BD embebida desarrollo

### DevOps y Testing

- **Docker & Docker Compose** - Contenedorización
- **JUnit 5** - Testing unitario
- **Testcontainers** - Testing integración
- **Mockito** - Mocking
- **JaCoCo** - Cobertura código
- **Maven** - Gestión dependencias

## 📋 Requisitos Previos

- **Sistema Operativo**: Windows 10/11, macOS, Linux
- **Java**: JDK 17 o superior
- **Node.js**: Versión 18 o superior (para desarrollo frontend)
- **Docker**: Versión 20.10 o superior
- **Docker Compose**: Versión 2.0 o superior
- **RAM**: Mínimo 4GB disponible
- **Espacio en Disco**: 2GB libres
- **Puertos Disponibles**: 3000, 8080, 5432, 27017, 6379

## 🚀 Instalación y Despliegue

### 🚀 Inicio Rápido (Desarrollo Local)

1. **Ejecutar la aplicación**:

   ```bash
   cd Construccion2SantiagoSuaza/clinic
   mvn spring-boot:run
   ```

2. **Acceder a la aplicación**:
   - **API Base**: http://localhost:8080
   - **Documentación Swagger**: http://localhost:8080/swagger-ui/index.html
   - **Consola H2 Database**: http://localhost:8080/h2-console

### 📋 Configuración de Base de Datos

La aplicación utiliza **H2 Database** embebida para desarrollo:
- **URL**: `jdbc:h2:file:../clinic_db`
- **Usuario**: `sa`
- **Contraseña**: *(vacía)*
- **Migraciones**: Automáticas con Flyway

### 🔧 Desarrollo Completo

Para desarrollo con todas las bases de datos (PostgreSQL, MongoDB, Redis):

```bash
# Configurar variables de entorno
cp .env.example .env

# Ejecutar con Docker Compose
docker-compose up -d
```

## ⚙️ Configuración

### Variables de Entorno (.env)

```env
# Base de Datos PostgreSQL
POSTGRES_DB=clinic_management
POSTGRES_USER=clinic_user
POSTGRES_PASSWORD=clinic_password_2024

# MongoDB
MONGO_INITDB_DATABASE=clinic_medical_records
MONGO_INITDB_ROOT_USERNAME=clinic_admin
MONGO_INITDB_ROOT_PASSWORD=clinic_password_2024

# JWT
JWT_SECRET=clinic_jwt_secret_key_minimum_256_bits_long_for_security_2025

# CORS
CLINIC_CORS_ALLOWED_ORIGINS=http://localhost:3000

# Perfiles Spring Boot
SPRING_PROFILES_ACTIVE=docker
```

### Perfiles de Spring Boot

- **`dev`**: Desarrollo local con H2 Database
- **`docker`**: Producción con PostgreSQL y MongoDB
- **`prod`**: Producción optimizada
- **`test`**: Testing con bases de datos embebidas

## 📖 Uso

### Acceso al Sistema

La aplicación incluye datos de prueba precargados. Credenciales disponibles:

- **Usuario**: jperez (Médico)
  - Email: juan.perez@clinica.com
  - Contraseña: Password123!

- **Usuario**: mgonzalez (Enfermera)
  - Email: maria.gonzalez@clinica.com
  - Contraseña: Password123!

- **Usuario**: crodriguez (Administrativo)
  - Email: carlos.rodriguez@clinica.com
  - Contraseña: Password123!

### Endpoints Disponibles

- `POST /api/auth/login` - Autenticación
- `GET /api/patients` - Gestión de pacientes
- `GET /api/appointments` - Gestión de citas
- `GET /api/medical-records` - Registros médicos
- `GET /api/public/health` - Estado del sistema

### Funcionalidades Principales

#### Gestión de Pacientes

- Registrar nuevos pacientes
- Actualizar información del paciente
- Consultar historial médico
- Gestionar contactos de emergencia

#### Programación de Citas

- Crear citas médicas
- Modificar estados de citas
- Cancelar citas
- Ver calendario de citas

#### Registros Médicos

- Crear registros médicos
- Agregar diagnósticos
- Prescribir medicamentos
- Registrar procedimientos

#### Facturación

- Generar facturas automáticamente
- Gestionar pagos
- Integración con seguros
- Reportes financieros

## 📚 API Documentation

La documentación completa de la API está disponible en:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Consola H2 Database**: http://localhost:8080/h2-console

### Endpoints Principales

#### Autenticación

- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/logout` - Cerrar sesión

#### Pacientes

- `GET /api/patients` - Listar pacientes
- `POST /api/patients` - Crear paciente
- `GET /api/patients/{id}` - Obtener paciente
- `PUT /api/patients/{id}` - Actualizar paciente

#### Citas

- `GET /api/appointments` - Listar citas
- `POST /api/appointments` - Crear cita
- `PUT /api/appointments/{id}/status` - Cambiar estado

#### Registros Médicos

- `GET /api/medical-records/{patientId}` - Obtener historial
- `POST /api/medical-records` - Crear registro médico

## 💻 Desarrollo

### Estructura del Proyecto

```
Construccion2SantiagoSuaza/
├── clinic/                          # Backend Spring Boot
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/app/clinic/
│       │   ├── application/         # Casos de uso, DTOs
│       │   ├── domain/              # Entidades, servicios de dominio
│       │   ├── infrastructure/      # Controladores, persistencia
│       │   └── Cs2Application.java  # Clase principal
│       └── test/                    # Tests
├── frontend/                        # Frontend Next.js
│   ├── Dockerfile
│   ├── package.json
│   └── src/
│       ├── app/                     # Páginas Next.js
│       ├── components/              # Componentes React
│       └── lib/                     # Utilidades
├── docker-compose.yml               # Configuración Docker
├── .env                             # Variables de entorno
└── README.md
```

### Comandos de Desarrollo

#### Backend

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar con perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Generar cobertura de código
mvn jacoco:report
```

#### Frontend

```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev

# Construir para producción
npm run build

# Ejecutar tests
npm test

# Linting
npm run lint
```

## 🧪 Testing

### Backend Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests de integración
mvn verify -Dspring.profiles.active=test

# Ejecutar con cobertura
mvn test jacoco:report

# Ejecutar tests específicos
mvn test -Dtest=UserServiceTest
```

### Tipos de Tests

- **Unit Tests**: Pruebas de unidades individuales
- **Integration Tests**: Pruebas de integración con Testcontainers
- **Performance Tests**: Pruebas de rendimiento
- **Security Tests**: Pruebas de seguridad

### Cobertura de Código

La cobertura mínima requerida es del 80%. Para ver el reporte:

```bash
mvn jacoco:report
# Reporte disponible en: target/site/jacoco/index.html
```

## 📊 Monitoreo

### Health Checks y Estado

- **Health Check**: http://localhost:8080/api/public/health
- **Información API**: http://localhost:8080/api/public/info
- **Página de Bienvenida**: http://localhost:8080/api/public/welcome
- **Actuator Health**: http://localhost:8080/actuator/health
- **Actuator Info**: http://localhost:8080/actuator/info

### Logs

Los logs se configuran por perfil:

- **dev**: Nivel DEBUG para aplicación
- **docker/prod**: Nivel INFO/WARN para producción

### Métricas

- **JVM Metrics**: Memoria, CPU, GC
- **HTTP Metrics**: Requests, responses, errores
- **Database Metrics**: Conexiones, queries
- **Custom Metrics**: Métricas de negocio

## 🔧 Solución de Problemas

### Problemas Comunes

#### Error de Dependencias Maven

```bash
# Limpiar caché de Maven
mvn dependency:purge-local-repository

# Reconstruir sin caché
docker-compose build --no-cache backend
```

#### Puertos Ocupados

```bash
# Ver procesos usando puertos
netstat -tulpn | grep :8080
netstat -tulpn | grep :3000

# Cambiar puertos en docker-compose.yml o application.properties
```

#### Problemas de Base de Datos

```bash
# Acceder a PostgreSQL
docker-compose exec postgres psql -U clinic_user -d clinic_management

# Acceder a MongoDB
docker-compose exec mongodb mongo -u clinic_admin -p clinic_password_2024 --authenticationDatabase admin clinic_medical_records

# Acceder a Redis
docker-compose exec redis redis-cli
```

#### Problemas de Memoria

```bash
# Verificar uso de memoria
docker stats

# Aumentar límite de memoria en Docker Desktop
# Settings > Resources > Memory > 4GB mínimo
```

#### Problemas de Conexión

```bash
# Verificar conectividad entre servicios
docker-compose exec backend curl -f http://postgres:5432 || echo "PostgreSQL no responde"
docker-compose exec backend curl -f http://mongodb:27017 || echo "MongoDB no responde"
docker-compose exec backend curl -f http://redis:6379 || echo "Redis no responde"
```

### Logs de Debugging

```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend

# Ver logs con timestamps
docker-compose logs -f --timestamps
```

### Reset del Sistema

```bash
# Detener y eliminar contenedores
docker-compose down -v

# Limpiar imágenes no utilizadas
docker system prune -f

# Reiniciar desde cero
docker-compose up --build -d
```

## 🤝 Contribución

1. **Fork** el proyecto
2. **Crear** una rama para tu funcionalidad (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abrir** un Pull Request

### Guías de Contribución

- Seguir convenciones de código
- Escribir tests para nuevas funcionalidades
- Actualizar documentación según sea necesario
- Mantener compatibilidad con versiones anteriores

### Estándares de Código

- **Java**: Google Java Style Guide
- **TypeScript**: Airbnb TypeScript Style Guide
- **Commits**: Conventional Commits
- **Branches**: Git Flow

### Reporte de Problemas

Para reportar bugs o solicitar funcionalidades:

1. Verificar que no exista un issue similar
2. Crear un nuevo issue con descripción detallada
3. Incluir pasos para reproducir el problema
4. Agregar información del entorno (SO, versión, etc.)

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 📞 Contacto

**Santiago Suaza Cardona** - Estudiante de Ingeniería de Sistemas

- **Institución**: Institución Universitaria Tecnológico de Antioquia (TdeA)
- **Programa**: Ingeniería de Sistemas
- **Curso**: Construcción de Software 2
- **Proyecto**: Trabajo Final de Semestre

### Soporte

Para soporte técnico del proyecto:

1. Revisar la documentación de la API
2. Consultar los logs de la aplicación
3. Contactar al desarrollador

---

⭐ **Proyecto desarrollado como trabajo final de semestre para la clase Construcción de Software 2**

Última actualización: Noviembre 2025
