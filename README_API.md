# 📋 Documentación de la API - Sistema de Gestión Médica CS2

## 📋 Resumen

**Sistema de Gestión Médica** desarrollado por **Santiago Suaza Cardona** para la Clínica CS2. Es una aplicación Java Spring Boot que proporciona una API REST completa para la gestión integral de una clínica médica.

## 🏗️ Arquitectura

- **Backend**: Java 17 + Spring Boot 3.x
- **Base de Datos**: H2 (desarrollo) + MongoDB (historia clínica)
- **Puerto**: 8081
- **Context Path**: `/api`
- **URL Base**: `http://localhost:8081/api`

## 🗄️ Bases de Datos

### H2 Database (SQL)
- **Uso**: Datos estructurados (pacientes, usuarios, órdenes, facturación)
- **Consola**: http://localhost:8081/api/h2-console
- **Credenciales**: usuario `sa`, contraseña `(vacía)`

### MongoDB (NoSQL)
- **Uso**: Historia clínica y documentos médicos
- **Host**: localhost:27017
- **Base de datos**: clinical_history_db

## 🚀 Inicio Rápido

### 1. Prerrequisitos
- Java 17+
- Maven 3.6+
- MongoDB (ejecutándose en puerto 27017)

### 2. Ejecutar la aplicación
```bash
cd Construccion2SantiagoSuaza/ProyectoCS2
mvn spring-boot:run
```

### 3. Verificar estado
- Aplicación: http://localhost:8081/api/actuator/health
- H2 Console: http://localhost:8081/api/h2-console

## 📚 Endpoints Principales

### 🔐 Autenticación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/logout` | Cerrar sesión |
| GET | `/api/auth/me` | Información del usuario actual |
| GET | `/api/auth/permissions` | Verificar permisos |
| GET | `/api/auth/access` | Verificar acceso a recursos |

### 👥 Gestión de Pacientes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/patients` | Listar todos los pacientes |
| GET | `/api/patients/{idCard}` | Obtener paciente por cédula |
| POST | `/api/patients` | Registrar nuevo paciente |
| PUT | `/api/patients/{idCard}` | Actualizar paciente |
| DELETE | `/api/patients/{idCard}` | Eliminar paciente |

### ⚕️ Servicios Médicos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/medical/orders` | Crear orden médica |
| GET | `/api/medical/orders/{orderNumber}` | Obtener orden médica |
| POST | `/api/medical/clinical-history` | Actualizar historia clínica |
| GET | `/api/medical/clinical-history/{patientIdCard}` | Obtener historia clínica |
| POST | `/api/medical/vital-signs` | Registrar signos vitales |
| POST | `/api/medical/follow-up` | Procesar consulta de seguimiento |

### 💰 Facturación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/billing/invoices` | Generar factura |
| GET | `/api/billing/invoices/{invoiceId}` | Obtener factura |
| GET | `/api/billing/invoices/patient/{patientIdCard}` | Facturas por paciente |
| GET | `/api/billing/invoices/order/{orderNumber}` | Factura por orden |
| GET | `/api/billing/reports/patient/{patientIdCard}` | Reporte por paciente |
| GET | `/api/billing/reports/period` | Reporte por período |
| GET | `/api/billing/calculate-copay` | Calcular copago |

### 📦 Gestión de Inventario
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/inventory/medications` | Listar medicamentos |
| GET | `/api/inventory/procedures` | Listar procedimientos |
| GET | `/api/inventory/diagnostics` | Listar ayudas diagnósticas |
| POST | `/api/inventory/medications` | Crear medicamento |
| POST | `/api/inventory/procedures` | Crear procedimiento |
| POST | `/api/inventory/diagnostics` | Crear ayuda diagnóstica |
| PUT | `/api/inventory/medications/stock` | Actualizar stock |
| DELETE | `/api/inventory/medications/{id}` | Eliminar medicamento |

### 👤 Gestión de Usuarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/users` | Listar usuarios |
| GET | `/api/users/{idCard}` | Obtener usuario |
| POST | `/api/users` | Crear usuario |
| PUT | `/api/users/{idCard}` | Actualizar usuario |
| DELETE | `/api/users/{idCard}` | Eliminar usuario |

## 🔑 Autenticación

La mayoría de los endpoints requieren autenticación mediante el header `User-ID`:

```
User-ID: 12345678
```

## 📝 Ejemplos de Uso

### 1. Crear un Paciente
```bash
curl -X POST http://localhost:8081/api/patients \
  -H "Content-Type: application/json" \
  -H "User-ID: 12345678" \
  -d '{
    "idCard": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@email.com",
    "phone": "+57 300 123 4567",
    "dateOfBirth": "1990-01-15",
    "gender": "M",
    "address": "Calle 123 #45-67, Bogotá",
    "emergencyContact": {
      "name": "María Pérez",
      "phone": "+57 300 987 6543",
      "relationship": "Esposa"
    },
    "insurancePolicy": {
      "provider": "Sura",
      "policyNumber": "POL123456",
      "coverage": 80.0
    }
  }'
```

### 2. Crear una Orden Médica
```bash
curl -X POST http://localhost:8081/api/medical/orders \
  -H "Content-Type: application/json" \
  -H "User-ID: 87654321" \
  -d '{
    "patientIdCard": "12345678",
    "doctorIdCard": "87654321",
    "orderType": "MEDICATION",
    "items": [
      {
        "type": "MEDICATION",
        "medicationId": "MED001",
        "quantity": 30,
        "frequency": "Cada 8 horas",
        "duration": "10 días"
      }
    ],
    "diagnosis": "Hipertensión arterial",
    "notes": "Control mensual requerido"
  }'
```

### 3. Registrar Signos Vitales
```bash
curl -X POST http://localhost:8081/api/medical/vital-signs \
  -H "Content-Type: application/json" \
  -H "User-ID: 87654321" \
  -d '{
    "patientIdCard": "12345678",
    "bloodPressureSystolic": 120,
    "bloodPressureDiastolic": 80,
    "heartRate": 72,
    "temperature": 36.5,
    "respiratoryRate": 16,
    "oxygenSaturation": 98,
    "weight": 70.5,
    "height": 175,
    "notes": "Paciente estable, sin signos de distress"
  }'
```

## 🧪 Pruebas con Postman

1. Importar el archivo `ProyectoCS2 - API Collection.postman_collection.json`
2. Configurar la variable `baseUrl`: `http://localhost:8081/api`
3. Configurar la variable `userId`: `12345678` (o el ID del usuario autenticado)
4. Ejecutar las requests en orden recomendado

## 📊 Modelos de Datos

### Paciente
```json
{
  "idCard": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phone": "string",
  "dateOfBirth": "date",
  "gender": "string",
  "address": "string",
  "emergencyContact": {
    "name": "string",
    "phone": "string",
    "relationship": "string"
  },
  "insurancePolicy": {
    "provider": "string",
    "policyNumber": "string",
    "coverage": "number"
  }
}
```

### Orden Médica
```json
{
  "patientIdCard": "string",
  "doctorIdCard": "string",
  "orderType": "MEDICATION|PROCEDURE|DIAGNOSTIC",
  "items": [
    {
      "type": "string",
      "medicationId": "string",
      "quantity": "number",
      "frequency": "string",
      "duration": "string"
    }
  ],
  "diagnosis": "string",
  "notes": "string"
}
```

### Signos Vitales
```json
{
  "patientIdCard": "string",
  "bloodPressureSystolic": "number",
  "bloodPressureDiastolic": "number",
  "heartRate": "number",
  "temperature": "number",
  "respiratoryRate": "number",
  "oxygenSaturation": "number",
  "weight": "number",
  "height": "number",
  "notes": "string"
}
```

## 🔧 Configuración

### application.properties
```properties
# Servidor
server.port=8081
server.servlet.context-path=/api

# H2 Database
spring.datasource.url=jdbc:h2:mem:clinicdb
spring.jpa.hibernate.ddl-auto=create-drop

# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=clinical_history_db

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## 🚨 Notas Importantes

1. **Autenticación**: La mayoría de endpoints requieren el header `User-ID`
2. **CORS**: Configurado para `http://localhost:3000` y `http://localhost:4200`
3. **Base de datos**: Se recrea automáticamente en cada inicio (modo desarrollo)
4. **MongoDB**: Asegúrate de que esté ejecutándose antes de usar funcionalidades de historia clínica

## 📞 Soporte

**Desarrollador**: Santiago Suaza Cardona
**Proyecto**: Construcción 2 - Clínica CS2
**Fecha**: 2025
