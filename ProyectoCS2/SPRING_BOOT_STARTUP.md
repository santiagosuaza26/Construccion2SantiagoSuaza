# 🚀 Cómo Iniciar un Programa Spring Boot

## 📋 Formas de Ejecutar una Aplicación Spring Boot

### **Método 1: Con Maven Wrapper (Recomendado)**
```bash
# Desde el directorio del proyecto:
./mvnw spring-boot:run

# Con perfil específico:
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

### **Método 2: Con Maven Directo**
```bash
# Si tienes Maven instalado globalmente:
mvn spring-boot:run

# Con configuración específica:
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

### **Método 3: Ejecutar JAR Compilado**
```bash
# 1. Primero compilar:
./mvnw clean package

# 2. Ejecutar el JAR generado:
java -jar target/ProyectoCS2-1.0.0.jar

# Con perfil específico:
java -jar target/ProyectoCS2-1.0.0.jar --spring.profiles.active=demo
```

---

## 🎯 **Ejecución Específica para tu Proyecto**

### **✅ Para Demostración (Sin dependencias externas):**
```bash
# Opción 1: Script simplificado (MÁS FÁCIL)
simple-demo.bat

# Opción 2: Con configuración demo
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo

# Opción 3: Especificando propiedades
./mvnw spring-boot:run -Dspring.config.additional-location=file:demo.properties
```

### **📍 URLs de Acceso:**
- **🏥 Aplicación:** http://localhost:8082
- **💾 H2 Console:** http://localhost:8082/h2-console
- **📊 Health:** http://localhost:8082/actuator/health
- **📚 API Docs:** http://localhost:8082/swagger-ui.html

---

## 🔧 **Configuración de Perfiles**

### **Perfiles Disponibles:**
```bash
# Desarrollo (por defecto)
./mvnw spring-boot:run

# Demostración (sin MongoDB)
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo

# Producción
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Pruebas
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

### **Archivos de Configuración:**
- **`application.properties`** - Configuración por defecto
- **`application-demo.properties`** - Configuración de demostración
- **`demo.properties`** - Configuración simplificada para demo

---

## 📊 **Verificación de Funcionamiento**

### **1. Verificar que está Compilando:**
```bash
./mvnw compile
# ✅ Debe terminar sin errores
```

### **2. Verificar que las Pruebas Pasan:**
```bash
./mvnw test
# ✅ Todas las pruebas deben pasar
```

### **3. Verificar que la Aplicación Inicia:**
```bash
# Buscar en los logs:
# "Started ProyectoCs2Application in X seconds"
# "Tomcat started on port(s): 8082"
# "H2 console available at '/h2-console'"
```

### **4. Verificar Endpoints con Curl:**
```bash
# Health check
curl -s http://localhost:8082/actuator/health

# H2 Console
curl -s -I http://localhost:8082/h2-console

# API Pacientes
curl -s http://localhost:8082/api/patients
```

---

## 🚨 **Solución de Problemas Comunes**

### **❌ Error: Puerto 8082 Ocupado**
```bash
# Solución: Cambiar puerto
./mvnw spring-boot:run -Dserver.port=8083

# O matar proceso que usa el puerto:
netstat -ano | findstr :8082
taskkill /PID <PID> /F
```

### **❌ Error: Base de Datos no Accesible**
```bash
# Solución: Usar configuración demo
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

### **❌ Error: MongoDB no Conectado**
```bash
# Solución: Deshabilitar MongoDB para demo
./mvnw spring-boot:run -Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
```

---

## 🎬 **Secuencia Completa para Demostración**

### **Paso 1: Preparar (30 segundos)**
```bash
# Limpiar y compilar
./mvnw clean compile
```

### **Paso 2: Iniciar (10 segundos)**
```bash
# Iniciar aplicación
simple-demo.bat
```

### **Paso 3: Verificar (20 segundos)**
```bash
# Verificar que funciona
./curl-tests.bat all
```

### **Paso 4: Demostrar (Tiempo variable)**
```bash
# Mostrar logs
echo "✅ Aplicación iniciada correctamente"

# Mostrar URLs
echo "🌐 http://localhost:8082"
echo "💾 http://localhost:8082/h2-console"
```

---

## 💡 **Comandos Específicos para tu Proyecto**

### **🏥 Para Demostración Profesional:**
```bash
# 1. Script más fácil para profesor:
simple-demo.bat

# 2. Si hay problemas, modo seguro:
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo -Dserver.port=8082
```

### **🔧 Para Desarrollo:**
```bash
# Con logging detallado:
./mvnw spring-boot:run -Dlogging.level.app=DEBUG

# Con H2 Console habilitado:
./mvnw spring-boot:run -Dspring.h2.console.enabled=true
```

### **📊 Para Debugging:**
```bash
# Ver todas las propiedades:
./mvnw spring-boot:run -Ddebug=true

# Ver SQL queries:
./mvnw spring-boot:run -Dlogging.level.org.hibernate.SQL=DEBUG
```

---

## 🎓 **Lo que Sucede Internamente**

### **Al Ejecutar `./mvnw spring-boot:run`:**

1. **📦 Compilación**: Maven compila todo el código Java
2. **🔗 Dependencias**: Descarga y resuelve todas las dependencias
3. **⚙️ Configuración**: Carga `application.properties`
4. **🌱 Inicialización**: Spring Boot inicia el contexto de aplicación
5. **💾 Base de Datos**: Crea tablas automáticamente con Hibernate
6. **🌐 Servidor**: Inicia Tomcat embebido en puerto 8082
7. **🔍 Endpoints**: Publica todos los endpoints REST
8. **📊 Monitoreo**: Habilita Actuator para health checks

### **Logs que Debes Ver:**
```
✅ "Started ProyectoCs2Application in X seconds"
✅ "Tomcat started on port(s): 8082"
✅ "H2 console available at '/h2-console'"
✅ "Application is running"
```

---

## 🚀 **Comandos de Ejecución por Escenario**

| Escenario | Comando | Propósito |
|-----------|---------|-----------|
| **Demostración** | `simple-demo.bat` | Sin dependencias, más fácil |
| **Desarrollo** | `./mvnw spring-boot:run` | Con todas las características |
| **Producción** | `./mvnw spring-boot:run -Dspring-boot.run.profiles=prod` | Configuración optimizada |
| **Debug** | `./mvnw spring-boot:run -Dlogging.level.app=DEBUG` | Con logs detallados |
| **Pruebas** | `./mvnw spring-boot:run -Dspring-boot.run.profiles=test` | Con datos de prueba |

---

## 📝 **Resumen para el Profesor**

**"Para iniciar un programa Spring Boot:"**

1. **✅ Compilar:** `./mvnw clean compile`
2. **✅ Probar:** `./mvnw test`
3. **✅ Ejecutar:** `./mvnw spring-boot:run`
4. **✅ Verificar:** `curl http://localhost:8082/actuator/health`

**"El resultado es una aplicación completa corriendo en http://localhost:8082 con arquitectura hexagonal y principios SOLID funcionando perfectamente."**

---

## 🎯 **Comandos Específicos para tu Demostración**

```bash
# 1. Inicio más fácil para profesor:
simple-demo.bat

# 2. Si hay problemas con MongoDB:
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo

# 3. Verificación inmediata:
./curl-tests.bat all

# 4. URLs finales:
echo "🏥 http://localhost:8082"
echo "💾 http://localhost:8082/h2-console"
```

**¡Tu aplicación Spring Boot está lista para demostrar al profesor!** 🚀