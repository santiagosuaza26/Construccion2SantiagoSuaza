# Sistema de Gestión de Clínica Médica

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7-green)](https://www.mongodb.com/)
[![Redis](https://img.shields.io/badge/Redis-7-red)](https://redis.io/)
[![Next.js](https://img.shields.io/badge/Next.js-16.0.1-black)](https://nextjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue)](https://www.typescriptlang.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Sistema integral para la gestión de clínicas médicas, desarrollado con arquitectura de microservicios. Optimiza la administración de pacientes, citas, registros médicos, facturación y soporte técnico.

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

### Opción 1: Despliegue con Docker (Recomendado)

1. **Clonar el repositorio**:

   ```bash
   git clone <repository-url>
   cd Construccion2SantiagoSuaza
   ```

2. **Configurar variables de entorno** (opcional):

   ```bash
   cp .env.example .env
   # Editar .env con sus valores personalizados
   ```

3. **Desplegar con Docker Compose**:

   ```bash
   docker-compose up -d
   ```

4. **Verificar el despliegue**:

   ```bash
   docker-compose ps
   ```

5. **Acceder a la aplicación**:
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:8081
   - **Documentación API**: http://localhost:8081/swagger-ui.html
   - **Consola H2** (desarrollo): http://localhost:8081/h2-console

### Opción 2: Desarrollo Local

#### Backend

```bash
cd clinic
mvn clean install
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
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

1. **Usuario Administrador**:
   - Email: admin@clinic.com
   - Contraseña: admin123

2. **Usuario Doctor**:
   - Email: doctor@clinic.com
   - Contraseña: doctor123

3. **Usuario Enfermera**:
   - Email: nurse@clinic.com
   - Contraseña: nurse123

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

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs

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

### Health Checks

- **Health Endpoint**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Info**: http://localhost:8080/actuator/info

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

**Santiago Suaza Cardona**

- Email: santiago.suaza@correo.tdea.edu.co
- LinkedIn: [Tu LinkedIn]
- GitHub: [Tu GitHub]

### Soporte

Para soporte técnico:

1. Revisar la documentación
2. Crear un issue en GitHub
3. Contactar al desarrollador

---

⭐ Si este proyecto te resulta útil, ¡dale una estrella en GitHub!

Última actualización: Noviembre 2025
