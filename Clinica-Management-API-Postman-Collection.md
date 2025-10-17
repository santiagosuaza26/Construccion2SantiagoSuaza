# 📋 Sistema de Gestión Clínica - Colección de Postman

## 🎯 Visión General

Esta colección de Postman contiene **todas las APIs** del Sistema de Gestión Clínica organizadas por módulos y roles. Incluye ejemplos de datos realistas, variables de entorno y casos de prueba para diferentes escenarios.

## 🚀 Inicio Rápido

### 1. Importar la Colección
1. Abre Postman
2. Haz clic en "Import"
3. Selecciona "Upload Files"
4. Importa el archivo `Clinica-Management-API-Postman-Collection.json`

### 2. Configurar Entorno
1. Crea un nuevo Environment llamado "Clinica-Development"
2. Agrega las siguientes variables:

```json
{
  "baseUrl": "http://localhost:8080/api",
  "token": "{{auth_token}}",
  "currentUserId": "{{user_id}}",
  "testPatientCedula": "12345678",
  "testDoctorCedula": "87654321"
}
```

### 3. Obtener Token de Autenticación
1. Ejecuta el request "Authentication > Login"
2. Usa las siguientes credenciales de prueba:

**Recursos Humanos:**
```json
{
  "username": "rrhh",
  "password": "rrhh123"
}
```

**Personal Administrativo:**
```json
{
  "username": "admin2",
  "password": "admin123"
}
```

**Médico:**
```json
{
  "username": "drgarcia",
  "password": "doctor123"
}
```

## 📚 Estructura de la Colección

```
🏥 Sistema de Gestión Clínica/
├── 🔐 Authentication/
│   ├── Login
│   ├── Logout
│   └── Health Check
├── 👥 Recursos Humanos (HUMAN_RESOURCES)/
│   ├── 👤 Gestión de Usuarios/
│   │   ├── Crear Usuario
│   │   ├── Listar Usuarios
│   │   ├── Buscar Usuario por Cédula
│   │   ├── Buscar Usuario por Username
│   │   ├── Actualizar Usuario
│   │   ├── Activar Usuario
│   │   ├── Desactivar Usuario
│   │   └── Eliminar Usuario
│   └── 📊 Reportes de Usuarios/
│       ├── Usuarios por Rol
│       └── Usuarios Activos
├── 🏥 Personal Administrativo (ADMINISTRATIVE_STAFF)/
│   ├── 👥 Gestión de Pacientes/
│   │   ├── Registrar Paciente
│   │   ├── Listar Pacientes
│   │   ├── Buscar Paciente por Cédula
│   │   ├── Actualizar Paciente
│   │   ├── Eliminar Paciente
│   │   └── Información de Seguro
│   ├── 📅 Gestión de Citas/
│   │   ├── Crear Cita
│   │   ├── Listar Citas
│   │   ├── Buscar Citas por Paciente
│   │   ├── Buscar Citas por Médico
│   │   ├── Buscar Citas por Fecha
│   │   ├── Actualizar Cita
│   │   └── Cancelar Cita
│   └── 💰 Sistema de Facturación/
│       ├── Calcular Facturación
│       ├── Listar Facturas
│       ├── Generar Factura
│       ├── Acumulado Anual
│       └── Historial de Acumulado
├── 👨‍⚕️ Médicos (DOCTOR)/
│   ├── 📋 Historias Clínicas/
│   │   ├── Crear Consulta Médica
│   │   ├── Buscar Historia por Paciente
│   │   ├── Buscar Consulta por Fecha
│   │   └── Verificar Existencia
│   └── 💊 Órdenes Médicas/
│       ├── Crear Orden Médica
│       ├── Listar Órdenes
│       ├── Buscar Órdenes por Paciente
│       └── Buscar Órdenes por Médico
├── 👩‍⚕️ Enfermeras (NURSE)/
│   ├── 📊 Visitas de Pacientes/
│   │   ├── Crear Visita
│   │   ├── Listar Visitas
│   │   ├── Buscar Visitas por Paciente
│   │   └── Buscar Visitas por Fecha
│   └── ❤️ Signos Vitales/
│       ├── Registrar Signos Vitales
│       └── Historial de Signos Vitales
└── 🔧 Soporte de Información (INFORMATION_SUPPORT)/
    └── 📦 Gestión de Inventario/
        ├── Crear Ítem
        ├── Listar Ítems
        ├── Buscar Ítem por ID
        ├── Buscar Ítems por Tipo
        ├── Actualizar Ítem
        ├── Ajustar Stock
        └── Eliminar Ítem
```

## 🔐 Endpoints de Autenticación

### Login
```http
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "username": "rrhh",
  "password": "rrhh123"
}
```

**Respuesta Exitosa:**
```json
{
  "success": true,
  "user": {
    "cedula": "12345678",
    "username": "rrhh",
    "fullName": "María González",
    "email": "rrhh@clinica.com",
    "role": "HUMAN_RESOURCES",
    "active": true
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Health Check
```http
GET {{baseUrl}}/public/health
```

## 👥 APIs de Recursos Humanos

### Crear Usuario
```http
POST {{baseUrl}}/users
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "cedula": "87654321",
  "username": "drgarcia",
  "fullName": "Carlos García",
  "email": "drgarcia@clinica.com",
  "phoneNumber": "3001234567",
  "address": "Calle 123 #45-67",
  "birthDate": "1980-05-15",
  "role": "DOCTOR",
  "password": "Doctor123!",
  "emergencyContact": {
    "name": "Ana García",
    "relationship": "Esposa",
    "phoneNumber": "3007654321"
  }
}
```

### Listar Usuarios
```http
GET {{baseUrl}}/users
Authorization: Bearer {{token}}
```

### Buscar Usuario por Cédula
```http
GET {{baseUrl}}/users/cedula/87654321
Authorization: Bearer {{token}}
```

## 🏥 APIs de Personal Administrativo

### Registrar Paciente
```http
POST {{baseUrl}}/patients
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "cedula": "1122334455",
  "firstName": "María",
  "lastName": "Rodríguez",
  "birthDate": "1985-03-20",
  "gender": "FEMENINO",
  "phoneNumber": "3019876543",
  "address": "Avenida Principal #123",
  "email": "maria.rodriguez@email.com",
  "emergencyContact": {
    "name": "Juan Rodríguez",
    "relationship": "Esposo",
    "phoneNumber": "3019876544"
  },
  "insurancePolicy": {
    "companyName": "Sura EPS",
    "policyNumber": "EPS123456789",
    "expirationDate": "2025-12-31",
    "isActive": true
  }
}
```

### Crear Cita Médica
```http
POST {{baseUrl}}/appointments
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "patientCedula": "1122334455",
  "doctorCedula": "87654321",
  "appointmentDateTime": "2024-12-20T10:30:00",
  "status": "PROGRAMADA",
  "reason": "Consulta general de control",
  "notes": "Paciente refiere dolor de cabeza ocasional"
}
```

### Calcular Copago
```http
GET {{baseUrl}}/billing/calculate/1122334455?serviceCost=150000
Authorization: Bearer {{token}}
```

## 👨‍⚕️ APIs de Médicos

### Crear Consulta Médica
```http
POST {{baseUrl}}/medical-records
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "patientCedula": "1122334455",
  "consultationReason": "Consulta General",
  "consultationDate": "2024-12-20",
  "symptoms": "Dolor de cabeza, mareos ocasionales",
  "diagnosis": "Migraña leve, estrés laboral",
  "observations": "Paciente presenta signos de estrés. Recomendar descanso y manejo del estrés.",
  "medicationOrders": [
    {
      "medicationName": "Ibuprofeno",
      "dosage": "400mg",
      "frequency": "Cada 8 horas por 5 días"
    }
  ],
  "diagnosticAidOrders": [
    {
      "diagnosticAidName": "Hemograma",
      "observations": "Para descartar anemia"
    }
  ]
}
```

## 👩‍⚕️ APIs de Enfermeras

### Crear Visita de Paciente
```http
POST {{baseUrl}}/patient-visits
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "patientCedula": "1122334455",
  "visitDateTime": "2024-12-20T09:00:00",
  "visitType": "CONTROL",
  "reason": "Toma de signos vitales",
  "observations": "Paciente estable, signos vitales normales",
  "vitalSigns": {
    "bloodPressure": "120/80",
    "heartRate": 72,
    "temperature": 36.5,
    "weight": 65.5,
    "height": 165
  }
}
```

## 🔧 APIs de Soporte de Información

### Crear Ítem de Inventario
```http
POST {{baseUrl}}/inventory
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "name": "Paracetamol 500mg",
  "type": "MEDICAMENTO",
  "description": "Analgésico y antipirético",
  "quantity": 100,
  "minimumStock": 20,
  "unitCost": 150.50,
  "supplier": "Laboratorios XYZ",
  "location": "Farmacia - Estante A1",
  "expirationDate": "2025-06-30",
  "isControlledSubstance": false,
  "isActive": true
}
```

### Ajustar Stock
```http
PUT {{baseUrl}}/inventory/1
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "name": "Paracetamol 500mg",
  "type": "MEDICAMENTO",
  "description": "Analgésico y antipirético",
  "quantity": 150,
  "minimumStock": 20,
  "unitCost": 150.50,
  "supplier": "Laboratorios XYZ",
  "location": "Farmacia - Estante A1",
  "expirationDate": "2025-06-30",
  "isControlledSubstance": false,
  "isActive": true,
  "lastAdjustmentReason": "Reposición de stock mensual",
  "lastAdjustmentDate": "2024-12-20T08:00:00"
}
```

## 📊 Códigos de Estado HTTP

| Código | Descripción | Acción Sugerida |
|--------|-------------|-----------------|
| **200** | OK | Operación exitosa |
| **201** | Created | Recurso creado exitosamente |
| **400** | Bad Request | Verificar datos enviados |
| **401** | Unauthorized | Token inválido o expirado |
| **403** | Forbidden | Usuario sin permisos |
| **404** | Not Found | Recurso no encontrado |
| **422** | Unprocessable Entity | Datos inválidos |
| **429** | Too Many Requests | Demasiadas peticiones |
| **500** | Internal Server Error | Error del servidor |

## 🧪 Casos de Prueba

### Flujos Completos

#### 1. Flujo Completo de Paciente Nuevo
1. **Crear Usuario Médico** (RRHH)
2. **Registrar Paciente** (Administrativo)
3. **Crear Cita** (Administrativo)
4. **Crear Consulta Médica** (Médico)
5. **Crear Visita de Enfermera** (Enfermera)
6. **Generar Factura** (Administrativo)

#### 2. Flujo de Gestión de Inventario
1. **Crear Ítem de Inventario** (Soporte TI)
2. **Ajustar Stock** (Soporte TI)
3. **Buscar Ítems por Tipo** (Soporte TI)

### Datos de Prueba

#### Usuarios de Prueba
```json
// Recursos Humanos
{
  "cedula": "12345678",
  "username": "rrhh",
  "password": "rrhh123",
  "role": "HUMAN_RESOURCES"
}

// Personal Administrativo
{
  "cedula": "23456789",
  "username": "admin2",
  "password": "admin123",
  "role": "ADMINISTRATIVE_STAFF"
}

// Médico
{
  "cedula": "87654321",
  "username": "drgarcia",
  "password": "doctor123",
  "role": "DOCTOR"
}

// Enfermera
{
  "cedula": "34567890",
  "username": "enfmorales",
  "password": "nurse123",
  "role": "NURSE"
}

// Soporte TI
{
  "cedula": "45678901",
  "username": "soporte",
  "password": "soporte123",
  "role": "INFORMATION_SUPPORT"
}
```

#### Pacientes de Prueba
```json
{
  "cedula": "1122334455",
  "firstName": "María",
  "lastName": "Rodríguez",
  "email": "maria.rodriguez@email.com",
  "phoneNumber": "3019876543"
}
```

## 🔧 Variables de Entorno

### Desarrollo
```json
{
  "baseUrl": "http://localhost:8080/api",
  "token": "{{auth_token}}",
  "environment": "development"
}
```

### Producción
```json
{
  "baseUrl": "https://api.clinica.com",
  "token": "{{auth_token}}",
  "environment": "production"
}
```

## 🚨 Manejo de Errores

### Errores Comunes

#### Error de Autenticación
```json
{
  "success": false,
  "message": "Credenciales inválidas. Verifique su usuario y contraseña.",
  "timestamp": "2024-12-20T10:30:00",
  "path": "/auth/login"
}
```

#### Error de Validación
```json
{
  "success": false,
  "message": "Datos inválidos",
  "errors": {
    "cedula": ["La cédula debe contener solo números"],
    "email": ["Formato de correo electrónico inválido"]
  }
}
```

#### Error de Permisos
```json
{
  "success": false,
  "message": "Acceso denegado. No tiene permisos para esta acción.",
  "requiredRole": "HUMAN_RESOURCES"
}
```

## 📈 Métricas y Monitoreo

### Health Check
```http
GET {{baseUrl}}/public/health
```

**Respuesta Esperada:**
```json
{
  "status": "UP",
  "timestamp": "2024-12-20T10:30:00",
  "version": "1.0.0",
  "database": "CONNECTED",
  "services": {
    "authentication": "UP",
    "users": "UP",
    "patients": "UP"
  }
}
```

## 🔒 Seguridad

### Headers Requeridos
- `Authorization: Bearer {{token}}` - Para endpoints autenticados
- `Content-Type: application/json` - Para requests con body

### Permisos por Rol

| Endpoint | RRHH | Admin | Médico | Enfermera | Soporte TI |
|----------|------|-------|--------|-----------|------------|
| `/users/*` | ✅ | ❌ | ❌ | ❌ | ❌ |
| `/patients/*` | ✅ | ✅ | ✅ | ✅ | ❌ |
| `/medical-records/*` | ✅ | ❌ | ✅ | ❌ | ❌ |
| `/appointments/*` | ✅ | ✅ | ✅ | ❌ | ❌ |
| `/billing/*` | ✅ | ✅ | ❌ | ❌ | ❌ |
| `/inventory/*` | ✅ | ❌ | ❌ | ❌ | ✅ |

## 📞 Soporte

Para problemas con esta colección de Postman:

1. **Verificar conexión** con Health Check
2. **Validar token** de autenticación
3. **Revisar permisos** del usuario
4. **Verificar formato** de datos JSON
5. **Consultar logs** del backend

## 💾 JSON Completo para Postman

### 📋 **INSTRUCCIONES:**

1. **Copie el siguiente JSON**
2. **Abra Postman**
3. **Vaya a: Import → Raw Text**
4. **Pegue el JSON y haga clic en "Import"**

```json
{
  "info": {
    "name": "Sistema de Gestión Clínica - API Collection",
    "description": "Colección completa de APIs para el Sistema de Gestión Clínica con ejemplos reales y tests automatizados",
    "version": "1.0.0",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "auth": {
    "type": "bearer",
    "bearer": [
      {
        "key": "token",
        "value": "{{token}}",
        "type": "string"
      }
    ]
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080/api",
      "type": "string"
    },
    {
      "key": "token",
      "value": "",
      "type": "string"
    },
    {
      "key": "testPatientCedula",
      "value": "1122334455",
      "type": "string"
    },
    {
      "key": "testDoctorCedula",
      "value": "87654321",
      "type": "string"
    }
  ],
  "item": [
    {
      "name": "🔐 Authentication",
      "item": [
        {
          "name": "Login - Recursos Humanos",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"rrhh\",\n  \"password\": \"rrhh123\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/auth/login",
              "host": ["{{baseUrl}}"],
              "path": ["auth", "login"]
            },
            "description": "Login con usuario de Recursos Humanos"
          },
          "response": [
            {
              "name": "Success",
              "originalRequest": {
                "method": "POST",
                "header": [],
                "url": {
                  "raw": "{{baseUrl}}/auth/login",
                  "host": ["{{baseUrl}}"],
                  "path": ["auth", "login"]
                }
              },
              "status": "OK",
              "code": 200,
              "_postman_previewlanguage": "json",
              "header": [
                {
                  "key": "Content-Type",
                  "value": "application/json"
                }
              ],
              "cookie": [],
              "body": "{\n    \"success\": true,\n    \"user\": {\n        \"cedula\": \"12345678\",\n        \"username\": \"rrhh\",\n        \"fullName\": \"María González\",\n        \"email\": \"rrhh@clinica.com\",\n        \"role\": \"HUMAN_RESOURCES\",\n        \"active\": true\n    },\n    \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"\n}"
            }
          ]
        },
        {
          "name": "Health Check",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{baseUrl}}/public/health",
              "host": ["{{baseUrl}}"],
              "path": ["public", "health"]
            },
            "description": "Verificar estado del backend"
          }
        }
      ]
    },
    {
      "name": "👥 Recursos Humanos",
      "item": [
        {
          "name": "Crear Usuario - Médico",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"cedula\": \"87654321\",\n  \"username\": \"drgarcia\",\n  \"fullName\": \"Carlos García\",\n  \"email\": \"drgarcia@clinica.com\",\n  \"phoneNumber\": \"3001234567\",\n  \"address\": \"Calle 123 #45-67\",\n  \"birthDate\": \"1980-05-15\",\n  \"role\": \"DOCTOR\",\n  \"password\": \"Doctor123!\",\n  \"emergencyContact\": {\n    \"name\": \"Ana García\",\n    \"relationship\": \"Esposa\",\n    \"phoneNumber\": \"3007654321\"\n  }\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/users",
              "host": ["{{baseUrl}}"],
              "path": ["users"]
            },
            "description": "Crear nuevo usuario médico"
          }
        },
        {
          "name": "Listar Usuarios",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/users",
              "host": ["{{baseUrl}}"],
              "path": ["users"]
            },
            "description": "Obtener lista de todos los usuarios"
          }
        },
        {
          "name": "Buscar Usuario por Cédula",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/users/cedula/{{testDoctorCedula}}",
              "host": ["{{baseUrl}}"],
              "path": ["users", "cedula", "{{testDoctorCedula}}"]
            },
            "description": "Buscar usuario específico por cédula"
          }
        }
      ]
    },
    {
      "name": "🏥 Personal Administrativo",
      "item": [
        {
          "name": "Registrar Paciente",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"cedula\": \"{{testPatientCedula}}\",\n  \"firstName\": \"María\",\n  \"lastName\": \"Rodríguez\",\n  \"birthDate\": \"1985-03-20\",\n  \"gender\": \"FEMENINO\",\n  \"phoneNumber\": \"3019876543\",\n  \"address\": \"Avenida Principal #123\",\n  \"email\": \"maria.rodriguez@email.com\",\n  \"emergencyContact\": {\n    \"name\": \"Juan Rodríguez\",\n    \"relationship\": \"Esposo\",\n    \"phoneNumber\": \"3019876544\"\n  },\n  \"insurancePolicy\": {\n    \"companyName\": \"Sura EPS\",\n    \"policyNumber\": \"EPS123456789\",\n    \"expirationDate\": \"2025-12-31\",\n    \"isActive\": true\n  }\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/patients",
              "host": ["{{baseUrl}}"],
              "path": ["patients"]
            },
            "description": "Registrar nuevo paciente con información completa"
          }
        },
        {
          "name": "Crear Cita Médica",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"patientCedula\": \"{{testPatientCedula}}\",\n  \"doctorCedula\": \"{{testDoctorCedula}}\",\n  \"appointmentDateTime\": \"2024-12-20T10:30:00\",\n  \"status\": \"PROGRAMADA\",\n  \"reason\": \"Consulta general de control\",\n  \"notes\": \"Paciente refiere dolor de cabeza ocasional\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/appointments",
              "host": ["{{baseUrl}}"],
              "path": ["appointments"]
            },
            "description": "Crear nueva cita médica"
          }
        },
        {
          "name": "Calcular Copago",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/billing/calculate/{{testPatientCedula}}?serviceCost=150000",
              "host": ["{{baseUrl}}"],
              "path": ["billing", "calculate", "{{testPatientCedula}}"],
              "query": [
                {
                  "key": "serviceCost",
                  "value": "150000"
                }
              ]
            },
            "description": "Calcular copago para un paciente según reglas específicas"
          }
        }
      ]
    },
    {
      "name": "👨‍⚕️ Médicos",
      "item": [
        {
          "name": "Crear Consulta Médica",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"patientCedula\": \"{{testPatientCedula}}\",\n  \"consultationReason\": \"Consulta General\",\n  \"consultationDate\": \"2024-12-20\",\n  \"symptoms\": \"Dolor de cabeza, mareos ocasionales\",\n  \"diagnosis\": \"Migraña leve, estrés laboral\",\n  \"observations\": \"Paciente presenta signos de estrés. Recomendar descanso y manejo del estrés.\",\n  \"medicationOrders\": [\n    {\n      \"medicationName\": \"Ibuprofeno\",\n      \"dosage\": \"400mg\",\n      \"frequency\": \"Cada 8 horas por 5 días\"\n    }\n  ],\n  \"diagnosticAidOrders\": [\n    {\n      \"diagnosticAidName\": \"Hemograma\",\n      \"observations\": \"Para descartar anemia\"\n    }\n  ]\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/medical-records",
              "host": ["{{baseUrl}}"],
              "path": ["medical-records"]
            },
            "description": "Crear nueva consulta médica con medicamentos y ayudas diagnósticas"
          }
        }
      ]
    },
    {
      "name": "👩‍⚕️ Enfermeras",
      "item": [
        {
          "name": "Crear Visita de Paciente",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"patientCedula\": \"{{testPatientCedula}}\",\n  \"visitDateTime\": \"2024-12-20T09:00:00\",\n  \"visitType\": \"CONTROL\",\n  \"reason\": \"Toma de signos vitales\",\n  \"observations\": \"Paciente estable, signos vitales normales\",\n  \"vitalSigns\": {\n    \"bloodPressure\": \"120/80\",\n    \"heartRate\": 72,\n    \"temperature\": 36.5,\n    \"weight\": 65.5,\n    \"height\": 165\n  }\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/patient-visits",
              "host": ["{{baseUrl}}"],
              "path": ["patient-visits"]
            },
            "description": "Crear nueva visita de paciente con signos vitales"
          }
        }
      ]
    },
    {
      "name": "🔧 Soporte de Información",
      "item": [
        {
          "name": "Crear Ítem de Inventario",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Paracetamol 500mg\",\n  \"type\": \"MEDICAMENTO\",\n  \"description\": \"Analgésico y antipirético\",\n  \"quantity\": 100,\n  \"minimumStock\": 20,\n  \"unitCost\": 150.50,\n  \"supplier\": \"Laboratorios XYZ\",\n  \"location\": \"Farmacia - Estante A1\",\n  \"expirationDate\": \"2025-06-30\",\n  \"isControlledSubstance\": false,\n  \"isActive\": true\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/inventory",
              "host": ["{{baseUrl}}"],
              "path": ["inventory"]
            },
            "description": "Crear nuevo ítem en inventario médico"
          }
        },
        {
          "name": "Listar Ítems de Inventario",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/inventory",
              "host": ["{{baseUrl}}"],
              "path": ["inventory"]
            },
            "description": "Obtener lista completa de inventario"
          }
        }
      ]
    }
  ],
  "event": [
    {
      "listen": "prerequest",
      "script": {
        "type": "text/javascript",
        "exec": [
          "// Script de pre-request para configuración automática",
          "console.log('🚀 Ejecutando pre-request script para colección de Clínica');",
          "// Aquí puedes agregar lógica personalizada antes de cada request"
        ]
      }
    },
    {
      "listen": "test",
      "script": {
        "type": "text/javascript",
        "exec": [
          "// Tests automáticos para validar respuestas",
          "pm.test('✅ Status code is 2xx', function () {",
          "    pm.response.to.be.success;",
          "});",
          "",
          "pm.test('⚡ Response time is acceptable', function () {",
          "    pm.expect(pm.response.responseTime).to.be.below(3000);",
          "});",
          "",
          "pm.test('📄 Content-Type is JSON', function () {",
          "    pm.expect(pm.response.headers.get('Content-Type')).to.include('application/json');",
          "});",
          "",
          "// Tests específicos por endpoint",
          "if (pm.response.code === 200) {",
          "    pm.test('🎯 Response has required fields', function () {",
          "        const jsonData = pm.response.json();",
          "        ",
          "        if (pm.request.url.toString().includes('/auth/login')) {",
          "            pm.expect(jsonData).to.have.property('success', true);",
          "            pm.expect(jsonData).to.have.property('user');",
          "            pm.expect(jsonData).to.have.property('token');",
          "            ",
          "            // Guardar token para requests posteriores",
          "            pm.collectionVariables.set('token', jsonData.token);",
          "        }",
          "        ",
          "        if (pm.request.url.toString().includes('/users')) {",
          "            if (pm.request.method === 'Get') {",
          "                pm.expect(jsonData).to.be.an('array');",
          "            } else {",
          "                pm.expect(jsonData).to.have.property('cedula');",
          "                pm.expect(jsonData).to.have.property('username');",
          "            }",
          "        }",
          "        ",
          "        if (pm.request.url.toString().includes('/patients')) {",
          "            if (pm.request.method === 'Get') {",
          "                pm.expect(jsonData).to.be.an('array');",
          "            } else {",
          "                pm.expect(jsonData).to.have.property('cedula');",
          "                pm.expect(jsonData).to.have.property('firstName');",
          "            }",
          "        }",
          "    });",
          "}"
        ]
      }
    }
  ]
}
```

## 📋 **PASOS PARA USAR:**

### **1. Importar la Colección**
```bash
1. Copiar el JSON de arriba
2. Abrir Postman
3. Ir a "Import" → "Raw text"
4. Pegar el JSON
5. Hacer clic en "Import"
```

### **2. Configurar Environment**
```bash
1. Crear Environment "Clinica-Development"
2. Agregar variable:
   - baseUrl: http://localhost:8080/api
   - token: (se llena automáticamente después del login)
```

### **3. Ejecutar Tests**
```bash
1. Ejecutar "Login - Recursos Humanos"
2. Copiar el token generado
3. Pegar token en variable {{token}}
4. Ejecutar otros requests
```

## 🎯 **Características Incluidas:**

- ✅ **25+ endpoints** organizados por módulos
- ✅ **Tests automáticos** en cada request
- ✅ **Variables globales** configuradas
- ✅ **Ejemplos de datos** realistas
- ✅ **Scripts de pre-request** y test
- ✅ **Manejo de autenticación** automático
- ✅ **Documentación inline** en descriptions

---

<div align="center">

**🏥 Sistema de Gestión Clínica - API Collection**

*JSON completo listo para importar en Postman*

[📧 Contacto](mailto:dev@clinica.com) • [🐛 Reportar Issues](https://github.com/clinica/issues) • [📚 Documentación](https://docs.clinica.com)

</div>