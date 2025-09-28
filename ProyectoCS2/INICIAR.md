# 🚀 Inicio Rápido - Clínica CS2

## ✅ Verificación de Prerrequisitos

Antes de iniciar, verifica que tienes:

- **Java 17+**: `java -version`
- **Maven 3.6+**: `mvn -version`

## 🎯 Formas de Iniciar la Aplicación

### Opción 1: Script Automático (Recomendado)

#### Linux/Mac:
```bash
./quick-start.sh
```

#### Windows:
```bash
# Método 1: Usar el archivo .bat
start-dev.bat

# Método 2: Compilar manualmente
mvn clean package
java -jar -Dspring-boot.run.profiles=dev target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

### Opción 2: Comandos Manuales

```bash
# 1. Compilar el proyecto
mvn clean package

# 2. Iniciar en modo desarrollo
java -jar -Dspring-boot.run.profiles=dev target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

### Opción 3: Desde tu IDE

1. Abrir el proyecto en VS Code, IntelliJ o Eclipse
2. Ejecutar `ProyectoCs2Application.java`
3. Configurar perfil: `dev`

## 🌐 URLs Importantes

Una vez iniciada la aplicación:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **API Base** | http://localhost:8080/api | Endpoints principales |
| **Health Check** | http://localhost:8080/api/actuator/health | Estado de la app |
| **H2 Console** | http://localhost:8080/api/h2-console | Base de datos (dev) |
| **Logs** | `logs/clinic-app.log` | Ver en tiempo real |

## 🧪 Pruebas Rápidas

### 1. Verificar que funciona
```bash
curl http://localhost:8080/api/actuator/health
# Debería retornar: {"status":"UP"}
```

### 2. Crear un usuario de prueba
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "idCard": "12345678",
    "fullName": "Dr. Juan Pérez",
    "email": "juan.perez@clinica.com",
    "phone": "3001234567",
    "birthDate": "1980-01-15",
    "address": "Calle 123 #45-67",
    "role": "ADMINISTRATIVE",
    "username": "jperez",
    "password": "admin123"
  }'
```

### 3. Listar usuarios
```bash
curl http://localhost:8080/api/users
```

## 📊 Monitoreo

### Ver logs en tiempo real:
```bash
# Linux/Mac
tail -f logs/clinic-app.log

# Windows (PowerShell)
Get-Content logs/clinic-app.log -Wait -Tail 10
```

### Ver solo errores:
```bash
tail -f logs/clinic-app-error.log
```

## 🛠️ Solución de Problemas

### ❌ La aplicación no inicia
1. Verifica que el puerto 8080 esté libre
2. Revisa los logs: `tail -f logs/clinic-app.log`
3. Verifica Java 17+: `java -version`

### ❌ Error de base de datos
1. Para desarrollo usa H2 (automático)
2. Para producción configura MySQL
3. Revisa configuración en `application-dev.properties`

### ❌ Puerto ocupado
```bash
# Usa otro puerto
java -jar -Dserver.port=8081 -Dspring-boot.run.profiles=dev target/ProyectoCS2-0.0.1-SNAPSHOT.jar
```

## 📁 Archivos Importantes

| Archivo | Descripción |
|---------|-------------|
| `README.md` | Documentación completa |
| `test-api.http` | Pruebas para la API |
| `start-dev.sh/bat` | Scripts de inicio |
| `logs/` | Directorio de logs |
| `src/main/resources/` | Configuraciones |

## 🎉 ¡Listo para usar!

Una vez que la aplicación esté funcionando:

1. ✅ Accede a http://localhost:8080/api
2. ✅ Usa el archivo `test-api.http` para pruebas
3. ✅ Revisa los logs para ver la actividad
4. ✅ Consulta el `README.md` para más detalles

**¡Tu aplicación de gestión médica está lista!** 🏥✨