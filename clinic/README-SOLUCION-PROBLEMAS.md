# 🏥 Sistema de Gestión Clínica - Guía de Inicio y Solución de Problemas

## 🚨 Problemas Comunes y Soluciones

### ❌ Error: "Docker Desktop is manually paused"
**Solución:**
1. Abre Docker Desktop
2. En el menú superior, haz clic en el icono de Docker
3. Selecciona "Resume" o desactiva la pausa
4. Espera a que termine de iniciarse completamente

### ❌ Error: "No se puede conectar con el servidor"
**Posibles causas y soluciones:**

#### 1. Servicios no iniciados
```bash
# Ejecutar el script de inicio mejorado
./start-improved.sh
```

#### 2. Puertos en uso
- **Puerto 8080 ocupado**: Cierra otras aplicaciones que usen este puerto
- **Puerto 5432 ocupado**: Cierra PostgreSQL local si está corriendo
- **Puerto 27017 ocupado**: Cierra MongoDB local si está corriendo

#### 3. Configuración CORS
El archivo `application-prod.properties` ya incluye configuración CORS:
```properties
app.cors.allowed-origins=http://localhost:3000,http://127.0.0.1:3000,http://localhost:8080,http://127.0.0.1:8080,file://
```

### ❌ Error 500: "Internal Server Error"
**Posibles causas:**

#### 1. Base de datos no inicializada
```bash
# Limpiar y reiniciar servicios
docker-compose down -v
docker-compose up --build -d
```

#### 2. Variables de entorno incorrectas
Verifica que `application-prod.properties` tenga las credenciales correctas:
```properties
spring.datasource.password=clinic_password_2024
spring.data.mongodb.password=admin_password_2024
```

#### 3. Problemas de Redis
Si Redis causa problemas, puedes comentarlo temporalmente en `docker-compose.yml`:
```yaml
# redis:
#   image: redis:7-alpine
#   ...
```

### ❌ Error: "Input validation failed"
**Problemas de validación en el formulario:**

#### 1. Campos requeridos vacíos
- Asegúrate de llenar todos los campos marcados con `*`
- Verifica que el rol esté seleccionado

#### 2. Formatos incorrectos
- **Cédula**: Solo números, 6-12 dígitos
- **Teléfono**: Exactamente 10 dígitos
- **Email**: Formato válido con dominio existente
- **Usuario**: Solo letras y números, máximo 15 caracteres
- **Contraseña**: Mínimo 8 caracteres, incluir mayúscula, número y símbolo

## 🔧 Comandos Útiles

### Inicio y parada
```bash
# Iniciar servicios
./start-improved.sh

# Detener servicios
docker-compose down -v

# Ver estado de servicios
docker-compose ps

# Ver logs del backend
docker-compose logs app

# Ver logs de todos los servicios
docker-compose logs
```

### Diagnóstico
```bash
# Ejecutar diagnóstico automático
./troubleshoot.bat

# Verificar conectividad del backend
curl http://localhost:8080/api/public/health

# Verificar puertos
netstat -an | findstr :8080
```

## 🔑 Credenciales de Acceso

### Para desarrollo y pruebas (Sincronizadas con Backend):
```
Usuario administrador:
  Usuario: admin
  Contraseña: admin123

Usuario Recursos Humanos:
  Usuario: rrhh
  Contraseña: rrhh123

Usuario Personal Administrativo:
  Usuario: admin2
  Contraseña: admin123

Paciente de prueba:
  Usuario: patient1
  Contraseña: patient123

Médico de prueba:
  Usuario: drgarcia
  Contraseña: doctor123

Enfermera de prueba:
  Usuario: enfmorales
  Contraseña: nurse123
```

## 🌐 URLs Importantes

- **Frontend**: Abre `frontend/index.html` en tu navegador
- **API Backend**: http://localhost:8080/api
- **Documentación API**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/public/health

## 📋 Pasos para Solucionar Problemas

### Paso 1: Verificar Docker
1. Abre Docker Desktop
2. Asegúrate de que esté completamente iniciado
3. Ejecuta `./troubleshoot.bat` para diagnóstico automático

### Paso 2: Iniciar servicios
```bash
# Limpiar servicios anteriores
docker-compose down -v

# Iniciar servicios
./start-improved.sh
```

### Paso 3: Verificar funcionamiento
1. Abre `frontend/index.html` en tu navegador
2. Verifica que puedas acceder al health check: http://localhost:8080/api/public/health
3. Intenta hacer login con las credenciales de prueba

### Paso 4: Si persisten los problemas
1. Revisa los logs: `docker-compose logs app`
2. Verifica que todos los contenedores estén corriendo: `docker-compose ps`
3. Reinicia servicios: `docker-compose down -v && docker-compose up --build -d`

## ⚡ Inicio Rápido (Después de Solucionar Problemas)

1. **Iniciar servicios**:
   ```bash
   ./start-improved.sh
   ```

2. **Acceder al sistema**:
    - Abre `frontend/index.html` en tu navegador
    - Usa las credenciales de Personal Administrativo:
      - Usuario: `admin2`
      - Contraseña: `admin123`
    - O usa Recursos Humanos:
      - Usuario: `rrhh`
      - Contraseña: `rrhh123`

3. **Probar funcionalidades**:
    - Gestionar pacientes (crear, editar, buscar)
    - Programar citas médicas
    - Calcular copagos y generar facturas
    - Usar calculadora de acumulado anual
    - Probar todas las validaciones del formulario

## 📞 Soporte

Si después de seguir estos pasos aún tienes problemas:

1. Ejecuta `./troubleshoot.bat` para diagnóstico automático
2. Revisa los logs del backend: `docker-compose logs app`
3. Verifica que Docker Desktop esté corriendo correctamente
4. Asegúrate de que los puertos estén disponibles

## ✅ Verificaciones Finales

- [ ] Docker Desktop está corriendo
- [ ] Servicios iniciados correctamente (`docker-compose ps`)
- [ ] Backend responde en http://localhost:8080/api/public/health
- [ ] Puedes acceder al frontend en `index.html`
- [ ] Login funciona con credenciales de prueba
- [ ] Personal Administrativo puede gestionar pacientes y citas
- [ ] La calculadora de copagos funciona correctamente
- [ ] Puedes generar e imprimir facturas

---

**🎉 ¡Una vez solucionados estos problemas, tendrás acceso completo al sistema administrativo con todas las funcionalidades implementadas!**

**Funcionalidades disponibles:**
- ✅ Gestión completa de pacientes con validaciones estrictas
- ✅ Programación de citas médicas con médicos disponibles
- ✅ Sistema de facturación con lógica de copagos colombiana
- ✅ Calculadora de acumulado anual con límites de $1.000.000 COP
- ✅ Generación e impresión de facturas profesionales
- ✅ Navegación por pestañas moderna y responsiva
- ✅ Reportes administrativos y estadísticas
- ✅ Integración completa con APIs del backend