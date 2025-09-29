# Construccion2SantiagoSuaza

## Integrantes
Santiago Suaza Cardona (Martes - Jueves 8-10)
## Tencnologias
Java 
SpringBoot
MongoDb
Mysql
Maven
Docker


# 🏥 Sistema de Gestión Médica - Clínica CS2

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

## 📋 Descripción del Proyecto

Sistema integral de gestión médica desarrollado con **Spring Boot 3.5.4** y arquitectura de microservicios. Diseñado para clínicas y centros médicos que requieren una solución robusta para la gestión de pacientes, personal médico, inventarios, facturación y registros clínicos.

### ✨ Características Principales

- **👥 Gestión de Pacientes**: Registro, actualización y seguimiento completo
- **👨‍⚕️ Módulo Médico**: Órdenes médicas, historia clínica, signos vitales
- **💊 Control de Inventarios**: Medicamentos, procedimientos y ayudas diagnósticas
- **💰 Sistema de Facturación**: Facturas, copagos y reportes financieros
- **📊 Reportes y Estadísticas**: Dashboards y análisis de datos
- **🔒 Autenticación y Autorización**: Control de acceso granular por roles
- **📱 API RESTful**: Interfaz completa para integración con frontend
- **🐳 Despliegue con Docker**: Configuración completa para producción

## 👥 Equipo de Desarrollo

**Desarrollador Principal:**
- **Santiago Suaza Cardona**
- Horario: Martes - Jueves 8:00-10:00

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.5.4** - Framework principal
- **Spring Data JPA** - Persistencia de datos SQL
- **Spring Data MongoDB** - Persistencia NoSQL
- **Spring Security** - Autenticación y autorización
- **Hibernate** - ORM para MySQL
- **Maven** - Gestión de dependencias

### Bases de Datos
- **MySQL 8.0** - Base de datos relacional (producción)
- **H2 Database** - Base de datos en memoria (desarrollo)
- **MongoDB 7.0** - Base de datos NoSQL para historia clínica

### DevOps
- **Docker** - Containerización
- **Docker Compose** - Orquestación de servicios
- **Git** - Control de versiones

## 🚀 Inicio Rápido

### Prerrequisitos

- **Java 17** o superior
- **Maven 3.6+**
- **Git**
- **Docker y Docker Compose** (opcional, para producción)

### Instalación y Ejecución

#### Opción 1: Desarrollo Local (H2)

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd ProyectoCS2

# 2. Ejecutar con perfil de desarrollo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 3. Acceder a la aplicación
# API: http://localhost:8081/api
# H2 Console: http://localhost:8081/api/h2-console
```

#### Opción 2: Producción con Docker (MySQL)

```bash
# 1. Configurar variables de entorno
cp .env.example .env
# Editar .env con tus configuraciones

# 2. Levantar servicios con Docker Compose
docker-compose up -d

# 3. Ver logs en tiempo real
docker-compose logs -f app

# 4. Acceder a la aplicación
# API: http://localhost:8080/api
# phpMyAdmin: http://localhost:8081
# Mongo Express: http://localhost:8082
```

## 📡 Endpoints de la API

### Autenticación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/logout` | Cerrar sesión |
| GET | `/api/auth/me` | Información del usuario actual |
| GET | `/api/auth/permissions` | Verificar permisos |

### Gestión de Pacientes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/patients` | Listar pacientes |
| GET | `/api/patients/{idCard}` | Obtener paciente por ID |
| POST | `/api/patients` | Registrar nuevo paciente |
| PUT | `/api/patients/{idCard}` | Actualizar paciente |
| DELETE | `/api/patients/{idCard}` | Eliminar paciente |

### Funcionalidades Médicas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/medical/orders` | Crear orden médica |
| GET | `/api/medical/orders/{orderNumber}` | Obtener orden por número |
| POST | `/api/medical/clinical-history` | Actualizar historia clínica |
| GET | `/api/medical/clinical-history/{patientIdCard}` | Obtener historia clínica |
| POST | `/api/medical/vital-signs` | Registrar signos vitales |
| POST | `/api/medical/follow-up` | Procesar consulta de seguimiento |

### Control de Inventarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/inventory/medications` | Listar medicamentos |
| GET | `/api/inventory/procedures` | Listar procedimientos |
| GET | `/api/inventory/diagnostics` | Listar ayudas diagnósticas |
| POST | `/api/inventory/medications` | Agregar medicamento |
| PUT | `/api/inventory/medications/stock` | Actualizar stock |
| DELETE | `/api/inventory/medications/{medicationId}` | Eliminar medicamento |

### Sistema de Facturación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/billing/invoices` | Crear factura |
| GET | `/api/billing/invoices/{invoiceId}` | Obtener factura |
| GET | `/api/billing/invoices/patient/{patientIdCard}` | Facturas por paciente |
| GET | `/api/billing/reports/period` | Reporte por período |
| GET | `/api/billing/calculate-copay` | Calcular copago |

### Recursos Humanos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/hr/users` | Listar usuarios |
| GET | `/api/hr/users/{idCard}` | Obtener usuario |
| POST | `/api/hr/users` | Crear usuario |
| PUT | `/api/hr/users/{idCard}` | Actualizar usuario |
| DELETE | `/api/hr/users/{idCard}` | Eliminar usuario |
| GET | `/api/hr/roles` | Listar roles disponibles |

### Reportes y Estadísticas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/reports/billing/period` | Reporte de facturación |
| GET | `/api/reports/patients/registrations` | Registro de pacientes |
| GET | `/api/reports/inventory` | Estado de inventario |
| GET | `/api/reports/medical` | Reportes médicos |
| GET | `/api/reports/statistics` | Estadísticas generales |

## 🔐 Roles y Permisos

### 👨‍💼 Administrativo
- Gestión completa de pacientes
- Creación y gestión de citas
- Facturación y cobros
- Reportes administrativos

### 👨‍⚕️ Doctor
- Acceso completo a información médica
- Creación de órdenes médicas
- Gestión de historia clínica
- Prescripción de medicamentos

### 👩‍⚕️ Enfermera
- Registro de signos vitales
- Ejecución de procedimientos
- Aplicación de medicamentos
- Lectura de historia clínica

### 👥 Recursos Humanos
- Gestión de usuarios del sistema
- Asignación de roles y permisos
- Administración de personal

### 🛠️ Soporte
- Gestión completa de inventarios
- Control de medicamentos
- Gestión de procedimientos
- Mantenimiento de especialidades

### 👤 Paciente
- Acceso a su propia historia clínica
- Consulta de sus facturas
- Visualización de sus órdenes médicas

## 🧪 Testing y Pruebas

### Usuarios de Prueba

Para testing, usa estos IDs de usuario con los prefijos correspondientes:

```bash
# Administrador
ADM001

# Recursos Humanos
HR001

# Soporte
SUP001

# Doctor
DOC001

# Enfermera
NUR001

# Paciente
PAT001
```

### Ejemplos de Requests

#### 1. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin001",
    "password": "password123"
  }'
```

#### 2. Crear Paciente
```bash
curl -X POST http://localhost:8081/api/patients \
  -H "Content-Type: application/json" \
  -H "User-ID: ADM001" \
  -d '{
    "idCard": "12345678",
    "fullName": "Juan Pérez",
    "email": "juan.perez@email.com",
    "phone": "3001234567",
    "birthDate": "1990-01-15",
    "address": "Calle 123 #45-67",
    "emergencyContact": {
      "name": "María Pérez",
      "phone": "3019876543",
      "relationship": "Hermana"
    }
  }'
```

## 📊 Monitoreo y Salud

### Health Checks
- **API Health**: `GET /api/actuator/health`
- **Base de Datos**: `GET /api/actuator/health/db`
- **MongoDB**: `GET /api/actuator/health/mongo`
- **Métricas**: `GET /api/actuator/metrics`

### Logs
- **Ubicación**: `logs/clinic-app.log`
- **Rotación**: 10MB por archivo, máximo 100MB total
- **Retención**: 30 días

## 🐳 Despliegue con Docker

### Servicios Incluidos

1. **MySQL 8.0** - Base de datos principal
2. **MongoDB 7.0** - Historia clínica
3. **Redis** - Caché (opcional)
4. **Aplicación Spring Boot** - API principal
5. **phpMyAdmin** - Administración MySQL
6. **Mongo Express** - Administración MongoDB

### Comandos Útiles

```bash
# Levantar todos los servicios
docker-compose up -d

# Ver logs de la aplicación
docker-compose logs -f app

# Ver estado de servicios
docker-compose ps

# Reiniciar aplicación
docker-compose restart app

# Actualizar aplicación
docker-compose build app
docker-compose up -d app

# Hacer backup de bases de datos
docker-compose exec mysql-db mysqldump -u clinic_user -p clinicdb > backup.sql
```

## 🔧 Configuración

### Perfiles de Spring

- **`dev`** (por defecto): H2 + MongoDB local
- **`prod`**: MySQL + MongoDB en Docker

### Variables de Entorno

Ver archivo `.env.example` para todas las variables disponibles.

## 📚 Documentación Completa

### Guías de Instalación y Configuración
- [📖 **Guía de Instalación Detallada**](docs/INSTALLATION.md) - Pasos completos para instalar y configurar
- [🐳 **Docker Setup**](docker-compose.yml) - Configuración completa de contenedores
- [⚙️ **Configuración de Perfiles**](src/main/resources/) - Archivos de configuración para desarrollo y producción
- [🔧 **Variables de Entorno**](.env.example) - Todas las variables necesarias

### Documentación de la API
- [🔌 **Referencia Completa de la API**](docs/API_REFERENCE.md) - Todos los endpoints documentados
- [📋 **Ejemplos de Requests**](docs/API_REFERENCE.md#ejemplos-de-requests) - Casos de uso prácticos
- [🚦 **Códigos de Error**](docs/API_REFERENCE.md#códigos-de-error) - Lista completa de errores
- [🧪 **Testing con Postman**](README.md#testing-y-pruebas) - Guía para probar la API

### Soporte y Troubleshooting
- [🐛 **Guía de Troubleshooting**](docs/TROUBLESHOOTING.md) - Solución de problemas comunes
- [💾 **Backup y Restauración**](scripts/backup-databases.sh) - Script automatizado de backups
- [📊 **Monitoreo y Logs**](docs/TROUBLESHOOTING.md#logs-y-debugging) - Cómo diagnosticar problemas
- [🔍 **Health Checks**](README.md#monitoreo-y-salud) - Verificar estado del sistema

### Arquitectura y Desarrollo
- [🏗️ **Arquitectura del Sistema**](README.md#características-principales) - Descripción técnica
- [👥 **Roles y Permisos**](README.md#roles-y-permisos) - Control de acceso
- [🛠️ **Tecnologías Utilizadas**](README.md#tecnologías-utilizadas) - Stack tecnológico
- [🤝 **Contribución**](README.md#contribución) - Cómo contribuir al proyecto

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto es desarrollado para fines educativos y de demostración.

## 📞 Soporte

Para soporte técnico o consultas:
- **Email**: santiago.suaza@correo.tdea.edu.co
- **Horario**: Martes - Jueves 8:00-10:00

---

**🏥 Clínica CS2 - Transformando la atención médica con tecnología**
