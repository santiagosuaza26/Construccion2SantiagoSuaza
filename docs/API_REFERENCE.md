# 🔌 Referencia Completa de la API

## Índice
- [Autenticación](#autenticación)
- [Gestión de Pacientes](#gestión-de-pacientes)
- [Funcionalidades Médicas](#funcionalidades-médicas)
- [Control de Inventarios](#control-de-inventarios)
- [Sistema de Facturación](#sistema-de-facturación)
- [Recursos Humanos](#recursos-humanos)
- [Reportes y Estadísticas](#reportes-y-estadísticas)
- [Códigos de Error](#códigos-de-error)
- [Ejemplos de Requests](#ejemplos-de-requests)

---

## Autenticación

Base URL: `/api/auth`

### Iniciar Sesión
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

**Respuesta Exitosa:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "idCard": "ADM001",
    "fullName": "Administrador Sistema",
    "role": "ADMINISTRATIVE",
    "isStaff": true,
    "permissions": [
      "PATIENT_CREATE",
      "PATIENT_READ",
      "PATIENT_UPDATE"
    ],
    "sessionToken": "JWT_ADM001_1693526400000"
  }
}
```

### Cerrar Sesión
```http
POST /api/auth/logout
User-ID: ADM001
```

### Información del Usuario Actual
```http
GET /api/auth/me
User-ID: ADM001
```

### Verificar Permisos
```http
GET /api/auth/permissions?permission=PATIENT_CREATE
User-ID: ADM001
```

### Verificar Acceso a Recurso
```http
GET /api/auth/access?resourceType=PATIENT&action=CREATE
User-ID: ADM001
```

---

## Gestión de Pacientes

Base URL: `/api/patients`

### Listar Todos los Pacientes
```http
GET /api/patients
User-ID: ADM001
```

### Obtener Paciente por ID
```http
GET /api/patients/12345678
User-ID: ADM001
```

### Registrar Nuevo Paciente
```http
POST /api/patients
User-ID: ADM001
Content-Type: application/json

{
  "idCard": "12345678",
  "username": "jperez",
  "password": "password123",
  "fullName": "Juan Pérez",
  "email": "juan.perez@email.com",
  "phone": "3001234567",
  "birthDate": "1990-01-15",
  "gender": "M",
  "address": "Calle 123 #45-67",
  "bloodType": "O+",
  "emergencyContact": {
    "name": "María Pérez",
    "phone": "3019876543",
    "relationship": "Hermana"
  },
  "insurancePolicy": {
    "provider": "Sura",
    "policyNumber": "123456789",
    "coverage": "Premium"
  }
}
```

### Actualizar Paciente
```http
PUT /api/patients/12345678
User-ID: ADM001
Content-Type: application/json

{
  "idCard": "12345678",
  "username": "jperez",
  "fullName": "Juan Pérez Actualizado",
  "email": "juan.perez.nuevo@email.com",
  "phone": "3001234567",
  "birthDate": "1990-01-15",
  "address": "Calle 123 #45-67 - Apto 101"
}
```

### Eliminar Paciente
```http
DELETE /api/patients/12345678
User-ID: ADM001
```

---

## Funcionalidades Médicas

Base URL: `/api/medical`

### Crear Orden Médica
```http
POST /api/medical/orders
User-ID: DOC001
Content-Type: application/json

{
  "patientIdCard": "12345678",
  "doctorIdCard": "DOC001",
  "orderType": "MEDICATION",
  "description": "Paracetamol 500mg cada 8 horas",
  "items": [
    {
      "type": "MEDICATION",
      "medicationId": "MED001",
      "quantity": 30,
      "frequency": "8 horas",
      "duration": "10 días"
    }
  ],
  "priority": "NORMAL",
  "notes": "Paciente con fiebre y dolor de cabeza"
}
```

### Obtener Orden por Número
```http
GET /api/medical/orders/ORD001
User-ID: DOC001
```

### Actualizar Historia Clínica
```http
POST /api/medical/clinical-history
User-ID: DOC001
Content-Type: application/json

{
  "patientIdCard": "12345678",
  "doctorIdCard": "DOC001",
  "entryType": "CONSULTATION",
  "title": "Consulta General",
  "description": "Paciente presenta fiebre y cefalea",
  "diagnosis": "Infección respiratoria aguda",
  "treatment": "Reposo y medicamentos sintomáticos",
  "notes": "Control en 3 días si no mejora"
}
```

### Obtener Historia Clínica
```http
GET /api/medical/clinical-history/12345678
User-ID: DOC001
```

### Registrar Signos Vitales
```http
POST /api/medical/vital-signs
User-ID: NUR001
Content-Type: application/json

{
  "patientIdCard": "12345678",
  "recordedBy": "NUR001",
  "bloodPressureSystolic": 120,
  "bloodPressureDiastolic": 80,
  "heartRate": 75,
  "temperature": 36.5,
  "respiratoryRate": 18,
  "oxygenSaturation": 98,
  "weight": 70.5,
  "height": 175,
  "notes": "Paciente estable, signos vitales normales"
}
```

### Procesar Consulta de Seguimiento
```http
POST /api/medical/follow-up
User-ID: DOC001
Content-Type: application/x-www-form-urlencoded

patientIdCard=12345678&doctorIdCard=DOC001&diagnosis=Mejoría&relatedOrderNumbers=ORD001,ORD002
```

---

## Control de Inventarios

Base URL: `/api/inventory`

### Listar Medicamentos
```http
GET /api/inventory/medications
User-ID: SUP001
```

### Listar Procedimientos
```http
GET /api/inventory/procedures
User-ID: SUP001
```

### Listar Ayudas Diagnósticas
```http
GET /api/inventory/diagnostics
User-ID: SUP001
```

### Crear Medicamento
```http
POST /api/inventory/medications
User-ID: SUP001
Content-Type: application/json

{
  "name": "Paracetamol 500mg",
  "description": "Analgésico y antipirético",
  "activeIngredient": "Paracetamol",
  "concentration": "500mg",
  "presentation": "Tabletas",
  "stock": 100,
  "minStock": 20,
  "maxStock": 500,
  "unitPrice": 150.00,
  "supplier": "Laboratorios XYZ",
  "batchNumber": "LOT2024001",
  "expiryDate": "2025-12-31"
}
```

### Actualizar Stock de Medicamento
```http
PUT /api/inventory/medications/stock?medicationId=MED001&quantity=50&operation=ADD
User-ID: SUP001
```

### Eliminar Medicamento
```http
DELETE /api/inventory/medications/MED001
User-ID: SUP001
```

---

## Sistema de Facturación

Base URL: `/api/billing`

### Crear Factura
```http
POST /api/billing/invoices
User-ID: ADM001
Content-Type: application/json

{
  "patientIdCard": "12345678",
  "invoiceDate": "2024-01-15",
  "dueDate": "2024-02-15",
  "items": [
    {
      "description": "Consulta General",
      "quantity": 1,
      "unitPrice": 50000,
      "totalPrice": 50000
    },
    {
      "description": "Exámenes de laboratorio",
      "quantity": 1,
      "unitPrice": 80000,
      "totalPrice": 80000
    }
  ],
  "subtotal": 130000,
  "taxes": 24700,
  "total": 154700,
  "copay": 15470,
  "notes": "Factura por servicios médicos"
}
```

### Obtener Factura
```http
GET /api/billing/invoices/INV001
User-ID: ADM001
```

### Facturas por Paciente
```http
GET /api/billing/invoices/patient/12345678
User-ID: ADM001
```

### Reporte de Facturación por Período
```http
GET /api/billing/reports/period?startDate=2024-01-01&endDate=2024-01-31
User-ID: ADM001
```

### Calcular Copago
```http
GET /api/billing/calculate-copay?patientIdCard=12345678&serviceAmount=100000
User-ID: ADM001
```

---

## Recursos Humanos

Base URL: `/api/hr`

### Listar Usuarios
```http
GET /api/hr/users
User-ID: HR001
```

### Obtener Usuario
```http
GET /api/hr/users/HR001
User-ID: HR001
```

### Crear Usuario
```http
POST /api/hr/users
User-ID: HR001
Content-Type: application/json

{
  "idCard": "DOC001",
  "username": "dr.garcia",
  "password": "password123",
  "fullName": "Dr. Carlos García",
  "email": "carlos.garcia@clinica-cs2.com",
  "phone": "3009876543",
  "role": "DOCTOR",
  "specialty": "Medicina General",
  "licenseNumber": "MP001234",
  "isActive": true
}
```

### Actualizar Usuario
```http
PUT /api/hr/users/DOC001
User-ID: HR001
Content-Type: application/json

{
  "idCard": "DOC001",
  "username": "dr.garcia",
  "fullName": "Dr. Carlos García López",
  "email": "carlos.garcia@clinica-cs2.com",
  "phone": "3009876543",
  "specialty": "Medicina Interna"
}
```

### Eliminar Usuario
```http
DELETE /api/hr/users/DOC001
User-ID: HR001
```

### Listar Roles Disponibles
```http
GET /api/hr/roles
User-ID: HR001
```

---

## Reportes y Estadísticas

Base URL: `/api/reports`

### Reporte de Facturación por Período
```http
GET /api/reports/billing/period?startDate=2024-01-01&endDate=2024-01-31
User-ID: ADM001
```

### Reporte de Facturación por Paciente
```http
GET /api/reports/billing/patient/12345678
User-ID: ADM001
```

### Registro de Pacientes
```http
GET /api/reports/patients/registrations?startDate=2024-01-01&endDate=2024-01-31
User-ID: ADM001
```

### Estado de Inventario
```http
GET /api/reports/inventory?lowStock=true
User-ID: SUP001
```

### Reportes Médicos
```http
GET /api/reports/medical?type=consultations&startDate=2024-01-01&endDate=2024-01-31
User-ID: DOC001
```

### Estadísticas Generales
```http
GET /api/reports/statistics
User-ID: ADM001
```

---

## Códigos de Error

### Códigos de Autenticación (AUTH_XXX)
- `AUTH_001`: Credenciales inválidas
- `AUTH_002`: Error en validación del request
- `AUTH_003`: Error interno durante login
- `AUTH_004`: User ID requerido para logout
- `AUTH_005`: Error durante logout
- `AUTH_006`: User ID requerido
- `AUTH_007`: Permiso requerido
- `AUTH_008`: Sesión de usuario no encontrada
- `AUTH_009`: Error verificando permisos
- `AUTH_010`: User ID requerido
- `AUTH_011`: Sesión expirada o no encontrada
- `AUTH_012`: Error obteniendo información del usuario

### Códigos de Pacientes (PAT_XXX)
- `PAT_001`: Acceso denegado - Solo personal administrativo
- `PAT_002`: Error de validación
- `PAT_003`: Error interno registrando paciente
- `PAT_004`: Acceso denegado para actualizar pacientes
- `PAT_005`: ID de paciente requerido
- `PAT_006`: Paciente no encontrado
- `PAT_007`: Error de validación actualizando
- `PAT_008`: Error interno actualizando paciente

### Códigos de Inventario (MEDICATION_XXX, PROCEDURE_XXX, DIAGNOSTIC_XXX)
- `MEDICATION_ID_REQUIRED`: ID del medicamento requerido
- `QUANTITY_REQUIRED`: Cantidad debe ser positiva
- `OPERATION_REQUIRED`: Operación debe ser ADD o SUBTRACT
- `CREATE_MEDICATION_500`: Error creando medicamento
- `UPDATE_STOCK_500`: Error actualizando stock

---

## Ejemplos de Requests

### 1. Flujo Completo de Autenticación y Gestión

```bash
# 1. Login como administrador
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin001",
    "password": "password123"
  }'

# 2. Registrar un paciente
curl -X POST http://localhost:8081/api/patients \
  -H "Content-Type: application/json" \
  -H "User-ID: ADM001" \
  -d '{
    "idCard": "12345678",
    "username": "jperez",
    "password": "password123",
    "fullName": "Juan Pérez",
    "email": "juan.perez@email.com",
    "phone": "3001234567",
    "birthDate": "1990-01-15",
    "address": "Calle 123 #45-67"
  }'

# 3. Crear medicamento
curl -X POST http://localhost:8081/api/inventory/medications \
  -H "Content-Type: application/json" \
  -H "User-ID: SUP001" \
  -d '{
    "name": "Paracetamol 500mg",
    "description": "Analgésico y antipirético",
    "activeIngredient": "Paracetamol",
    "concentration": "500mg",
    "presentation": "Tabletas",
    "stock": 100,
    "minStock": 20,
    "unitPrice": 150.00
  }'

# 4. Crear orden médica
curl -X POST http://localhost:8081/api/medical/orders \
  -H "Content-Type: application/json" \
  -H "User-ID: DOC001" \
  -d '{
    "patientIdCard": "12345678",
    "doctorIdCard": "DOC001",
    "orderType": "MEDICATION",
    "description": "Paracetamol 500mg cada 8 horas por 5 días",
    "items": [
      {
        "type": "MEDICATION",
        "medicationId": "MED001",
        "quantity": 15,
        "frequency": "8 horas",
        "duration": "5 días"
      }
    ]
  }'
```

### 2. Collection de Postman

Para facilitar las pruebas, puedes importar esta colección en Postman:

```json
{
  "info": {
    "name": "Clínica CS2 API",
    "description": "Colección completa de endpoints para el Sistema de Gestión Médica"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8081/api",
      "type": "string"
    },
    {
      "key": "adminUserId",
      "value": "ADM001",
      "type": "string"
    }
  ]
}
```

---

## Notas Importantes

1. **Autenticación**: La mayoría de endpoints requieren el header `User-ID`
2. **Roles**: Cada endpoint tiene restricciones de acceso por rol
3. **Validación**: Los requests se validan automáticamente
4. **Paginación**: Los endpoints que retornan listas soportan parámetros `page` y `size`
5. **Filtros**: Usa parámetros de query para filtrar resultados
6. **CORS**: Configurado para `localhost:3000` y `localhost:4200`

## Próximas Mejoras

- [ ] Implementación de JWT real
- [ ] Paginación en todos los endpoints
- [ ] Filtros avanzados
- [ ] Exportación de reportes en PDF/Excel
- [ ] WebSockets para notificaciones en tiempo real
- [ ] API de archivos para subir documentos médicos