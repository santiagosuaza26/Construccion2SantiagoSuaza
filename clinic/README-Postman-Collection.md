# 📋 Guía de Uso - Colección de Postman

## 🚀 Inicio Rápido

### 1. Importar la Colección
1. Abre **Postman**
2. Haz clic en **Import**
3. Selecciona **File**
4. Busca y selecciona `clinic-api-collection.postman_collection.json`
5. La colección aparecerá en tu workspace

### 2. Configurar Variables de Entorno
La colección usa las siguientes variables:

| Variable | Valor por Defecto | Descripción |
|----------|------------------|-------------|
| `baseUrl` | `http://localhost:8080` | URL base del backend |
| `jwt_token` | *(vacío)* | Token JWT después del login |
| `username` | `admin` | Usuario para login |
| `password` | `Admin123!@#` | Contraseña para login |

### 3. Iniciar Servicios
Asegúrate de que ambos servicios estén corriendo:

```bash
# Terminal 1 - Backend
cd clinic
mvn spring-boot:run

# Terminal 2 - Frontend (opcional para pruebas API)
cd frontend
python -m http.server 8000
```

## 🔐 Flujo de Autenticación

### Paso 1: Verificar Estado del Servidor
1. Ejecuta **"Health Check"** en la carpeta **"🏠 Información Pública"**
2. Deberías recibir una respuesta con `status: "UP"`

### Paso 2: Iniciar Sesión
1. Ve a la carpeta **"🔐 Autenticación"**
2. Ejecuta **"Iniciar Sesión"**
3. El script automáticamente guardará el token JWT en la variable `jwt_token`

**Credenciales de Prueba:**
- **Usuario:** `admin` | **Contraseña:** `Admin123!@#`
- **Usuario:** `doctor` | **Contraseña:** `Doctor123!@#`

### Paso 3: Usar Endpoints Protegidos
Una vez autenticado, puedes usar cualquier endpoint de:
- 👥 Gestión de Usuarios
- 🏥 Gestión de Pacientes
- 📅 Gestión de Citas
- 🏥 Gestión de Visitas
- 📋 Gestión de Órdenes Médicas
- 📋 Gestión de Registros Médicos
- 💊 Gestión de Inventario
- 💰 Gestión de Facturación

## 🛠️ Características Avanzadas

### Scripts Automáticos
La colección incluye scripts que:

- **Pre-request Scripts:** Verifican autenticación antes de cada request
- **Test Scripts:** Validan respuestas y guardan datos automáticamente
- **Event Listeners:** Manejan tokens y errores

### Validaciones Automáticas
Cada request incluye pruebas que verifican:
- ✅ Código de estado HTTP correcto
- ✅ Tiempo de respuesta < 5 segundos
- ✅ Estructura de respuesta válida
- ✅ Autenticación requerida donde corresponde

## 🔧 Solución de Problemas

### Error 401 (No autorizado)
1. Ejecuta **"Iniciar Sesión"** primero
2. Verifica que el token se haya guardado correctamente
3. El token podría haber expirado (usa login nuevamente)

### Error 500 (Error interno)
1. Verifica que el backend esté corriendo en el puerto 8080
2. Revisa los logs del backend para errores específicos
3. Asegúrate de que la base de datos esté inicializada

### Error de CORS
1. Verifica que el backend tenga CORS configurado para `localhost:8000`
2. Asegúrate de que ambos servicios estén corriendo

## 📊 Ejemplos de Uso

### Crear un Paciente
1. Inicia sesión como administrador
2. Ve a **"🏥 Gestión de Pacientes"**
3. Ejecuta **"Crear Paciente"**
4. Usa el ejemplo de JSON proporcionado

### Crear una Cita
1. Asegúrate de tener pacientes y doctores creados
2. Ve a **"📅 Gestión de Citas"**
3. Ejecuta **"Crear Cita"**
4. El sistema validará automáticamente la disponibilidad

### Consultar Inventario
1. Inicia sesión (cualquier rol)
2. Ve a **"💊 Gestión de Inventario"**
3. Ejecuta **"Listar Todo el Inventario"**

## 🎯 Consejos para Pruebas

1. **Siempre inicia con "Health Check"** para verificar conectividad
2. **Usa "Iniciar Sesión"** antes de endpoints protegidos
3. **Revisa la consola de Postman** para mensajes de debug
4. **Los scripts automáticos** te guiarán si algo falta

## 📚 Documentación Adicional

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **H2 Console:** `http://localhost:8080/h2-console`
- **API Docs:** `http://localhost:8080/v3/api-docs`

## 🔄 Actualizaciones

### Última Actualización: Octubre 2024
Se agregaron nuevos endpoints para completar la cobertura de la API:

#### Nuevos Endpoints Agregados:
- **🏠 Información Pública**: Endpoint raíz (`/`)
- **👥 Gestión de Usuarios**: ID, activos, permisos, activar/desactivar usuarios
- **🏥 Gestión de Pacientes**: Búsqueda por username e ID, eliminación por ID
- **📅 Gestión de Citas**: Múltiples endpoints adicionales ya incluidos
- **🏥 Gestión de Visitas**: Rango de fechas, actualización, estadísticas, pendientes
- **📋 Gestión de Órdenes Médicas**: Por doctor, rango de fechas, actualización, eliminación, estadísticas
- **📋 Gestión de Registros Médicos**: Contador de registros
- **💊 Gestión de Inventario**: Ayudas diagnósticas, estadísticas, eliminación, verificación de existencia

### Procedimiento de Actualización:
1. Re-importa el archivo JSON
2. Verifica que las variables de entorno se mantengan
3. Ejecuta "Health Check" para validar compatibilidad
4. Prueba algunos de los nuevos endpoints agregados

---

**¡Listo para probar!** 🎉
La colección está configurada para proporcionarte una experiencia completa de testing de la API del Sistema de Gestión Clínica.