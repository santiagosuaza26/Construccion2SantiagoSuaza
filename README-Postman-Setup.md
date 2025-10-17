# 📋 Configuración de Postman - Sistema de Gestión Clínica

## 🎯 Archivos Disponibles

### 1. **Clinica-Management-API-Postman-Collection.json**
Archivo principal de la colección con **todos los endpoints** organizados por módulos y roles.

### 2. **Clinica-Management-Environment.json**
Entorno de desarrollo configurado con variables predefinidas.

### 3. **Clinica-Management-Production-Environment.json**
Entorno de producción para testing en ambiente real.

## 🚀 Inicio Rápido

### Paso 1: Importar Environment
1. **Abrir Postman**
2. **Ir a:** Environments → Import
3. **Seleccionar** `Clinica-Management-Environment.json`
4. **Verificar** que se importó correctamente

### Paso 2: Importar Colección
1. **Ir a:** Collections → Import
2. **Seleccionar** `Clinica-Management-API-Postman-Collection.json`
3. **Verificar** estructura de carpetas

### Paso 3: Configurar Backend
1. **Asegurar** que el backend esté corriendo en `http://localhost:8080`
2. **Ejecutar** "Health Check" para verificar conectividad
3. **Si no responde**, verificar que el backend Spring Boot esté iniciado

## 📋 Flujo de Testing Completo

### 1. Verificar Conectividad
```http
GET {{baseUrl}}/public/health
```

**Debe retornar:**
```json
{
  "status": "UP",
  "timestamp": "2024-12-20T10:30:00",
  "version": "1.0.0",
  "database": "CONNECTED"
}
```

### 2. Obtener Token de Autenticación
```http
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "username": "rrhh",
  "password": "rrhh123"
}
```

**Guardar el token** en la variable `{{token}}` para usar en requests posteriores.

### 3. Crear Usuario Médico (RRHH)
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
  "password": "Doctor123!"
}
```

### 4. Registrar Paciente (Administrativo)
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
  "email": "maria.rodriguez@email.com"
}
```

### 5. Crear Cita Médica
```http
POST {{baseUrl}}/appointments
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "patientCedula": "1122334455",
  "doctorCedula": "87654321",
  "appointmentDateTime": "2024-12-20T10:30:00",
  "status": "PROGRAMADA",
  "reason": "Consulta general de control"
}
```

### 6. Crear Consulta Médica (Médico)
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
  "medicationOrders": [
    {
      "medicationName": "Ibuprofeno",
      "dosage": "400mg",
      "frequency": "Cada 8 horas por 5 días"
    }
  ]
}
```

### 7. Crear Ítem de Inventario (Soporte TI)
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
  "location": "Farmacia - Estante A1"
}
```

## 🔧 Variables Importantes

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `baseUrl` | `http://localhost:8080/api` | URL base del backend |
| `token` | `{{auth_token}}` | Token JWT (se actualiza automáticamente) |
| `testPatientCedula` | `1122334455` | Cédula del paciente de prueba |
| `testDoctorCedula` | `87654321` | Cédula del médico de prueba |
| `testNurseCedula` | `34567890` | Cédula de la enfermera de prueba |
| `testSupportCedula` | `45678901` | Cédula del soporte TI de prueba |

## 📊 Tests Automatizados

Cada request incluye **tests automáticos** que verifican:

- ✅ **Código de estado HTTP** correcto
- ✅ **Tiempo de respuesta** < 3 segundos
- ✅ **Content-Type** application/json
- ✅ **Estructura de respuesta** válida
- ✅ **Campos requeridos** presentes
- ✅ **Guardado automático** de tokens

## 🚨 Solución de Problemas

### Error: "No se puede conectar con el servidor"
1. **Verificar** que el backend esté corriendo en puerto 8080
2. **Ejecutar** `java -jar clinica-backend.jar` o equivalente
3. **Verificar** configuración de firewall
4. **Probar** con `curl http://localhost:8080/api/public/health`

### Error: "Unauthorized" (401)
1. **Ejecutar** primero el request de "Login"
2. **Copiar** el token de la respuesta
3. **Pegar** el token en la variable `{{token}}`
4. **Verificar** que el token no haya expirado

### Error: "Forbidden" (403)
1. **Verificar** que el usuario tenga permisos para esa operación
2. **Usar** credenciales de RRHH para operaciones administrativas
3. **Revisar** matriz de permisos en la documentación

### Error: "Unprocessable Entity" (422)
1. **Verificar** formato de datos JSON
2. **Validar** campos requeridos
3. **Revisar** tipos de datos (números, fechas, etc.)

## 📈 Ejecutar Tests Completos

### Usando Postman Runner
1. **Seleccionar** Environment "Clinica-Development"
2. **Ir a** Collections → "Sistema de Gestión Clínica - API Collection"
3. **Hacer clic** en "Run"
4. **Seleccionar** requests a ejecutar
5. **Iniciar** ejecución

### Tests Incluidos
- **Autenticación** completa
- **CRUD de usuarios**
- **Registro de pacientes**
- **Gestión de citas**
- **Historias clínicas**
- **Gestión de inventario**
- **Cálculos de facturación**

## 🔄 Automatización

### Script de Pre-request
Cada request ejecuta automáticamente:
- Configuración de headers
- Variables de entorno
- Logs de debugging

### Script de Test
Cada respuesta es validada automáticamente:
- Status codes correctos
- Estructura JSON válida
- Campos requeridos presentes
- Performance aceptable

## 📋 Checklist de Verificación

- [ ] ✅ Backend corriendo en puerto 8080
- [ ] ✅ Health check responde correctamente
- [ ] ✅ Environment configurado correctamente
- [ ] ✅ Token obtenido y configurado
- [ ] ✅ Tests individuales funcionan
- [ ] ✅ Tests completos ejecutan correctamente

## 🎯 Próximos Pasos

1. **Ejecutar tests** con datos reales
2. **Verificar respuestas** del backend
3. **Ajustar datos** según necesidades específicas
4. **Crear tests personalizados** para casos específicos
5. **Documentar hallazgos** y problemas encontrados

---

<div align="center">

**🏥 Sistema de Gestión Clínica - Postman Setup**

*Configuración completa para testing de APIs*

[📧 Soporte](mailto:dev@clinica.com) • [🐛 Reportar Issues](https://github.com/clinica/issues)

</div>