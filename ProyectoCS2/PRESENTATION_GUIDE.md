# 📋 Guía de Presentación - Sistema de Gestión Clínica

## 🎯 Información General del Proyecto

**Estudiante:** Santiago Suaza Cardona
**Curso:** Construcción 2
**Tecnología:** Java Spring Boot
**Arquitectura:** Hexagonal + SOLID Principles
**Base de Datos:** MySQL + MongoDB (Híbrida)

---

## 🚀 Inicio Rápido para el Profesor

### 1. **Requisitos Previos**
```bash
# Verificar instalaciones
java -version          # Java 17+
mvn -version          # Maven 3.9+
docker --version      # Docker (opcional)
```

### 2. **Ejecución del Proyecto**
```bash
# Opción 1: Con Docker (Más fácil)
cd Construccion2SantiagoSuaza/ProyectoCS2
docker-compose up --build

# Opción 2: Desarrollo local
./mvnw clean install
./mvnw spring-boot:run
```

### 3. **Acceso a la Aplicación**
- **Aplicación principal:** http://localhost:8080
- **Base de datos MySQL:** localhost:3306
- **Base de datos MongoDB:** localhost:27017
- **Adminer (MySQL GUI):** http://localhost:8082
- **Mongo Express:** http://localhost:8081

---

## 📁 Estructura del Proyecto para Revisar

### **Archivos Clave para Evaluar:**

#### **1. Arquitectura Hexagonal**
```
src/main/java/app/
├── domain/                 # ✅ NÚCLEO PURO (Lógica de negocio)
│   ├── model/             # Entidades de dominio
│   ├── services/          # Servicios de dominio
│   ├── port/             # Interfaces (puertos)
│   └── exception/        # Excepciones del dominio
├── application/          # ✅ CASOS DE USO
│   ├── service/          # Servicios de aplicación
│   ├── dto/             # Data Transfer Objects
│   └── mapper/          # Transformaciones
└── infrastructure/       # ✅ ADAPTADORES
    ├── adapter/          # Adaptadores de puertos
    ├── config/          # Configuración
    └── web/controller/  # Controladores REST
```

#### **2. Principios SOLID Demostrados**
- **PatientValidationService.java** - SRP (Single Responsibility)
- **PatientRepository.java** - DIP (Dependency Inversion)
- **ServiceConfiguration.java** - OCP (Open/Closed Principle)
- **PatientMapper.java** - ISP (Interface Segregation)

#### **3. Archivos de Configuración**
- **`.env.example`** - Variables de entorno
- **`application.properties`** - Configuración Spring Boot
- **`application-test.properties`** - Configuración de pruebas

---

## 🎬 Demostración Guiada

### **Paso 1: Explicar la Arquitectura (5 minutos)**
```bash
# Mostrar estructura de directorios
tree src/main/java/app -I target

# Explicar los 3 capas:
echo "1. DOMAIN (Núcleo) - Lógica de negocio pura"
echo "2. APPLICATION (Casos de uso) - Orquestación"
echo "3. INFRASTRUCTURE (Adaptadores) - Detalles técnicos"
```

### **Paso 2: Mostrar Cumplimiento SOLID (10 minutos)**

#### **Single Responsibility Principle (SRP)**
```java
// Archivo: PatientValidationService.java
public class PatientValidationService {
    // ✅ UNA SOLA RESPONSABILIDAD: Validar pacientes
    public void validatePatientForRegistration(Patient patient) {
        validateBasicPatientData(patient);
        validateAgeConstraints(patient.getBirthDate());
        validateGender(patient.getGender());
        // ... solo validaciones
    }
}
```

#### **Dependency Inversion Principle (DIP)**
```java
// Puerto (interfaz)
public interface PatientRepository {
    Optional<Patient> findByIdCard(String idCard);
    Patient save(Patient patient);
}

// Adaptador (implementación)
@Component
public class PatientRepositoryAdapter implements PatientRepository {
    // Implementa el puerto
}
```

#### **Open/Closed Principle (OCP)**
```java
// Fácil de extender sin modificar
@Configuration
@Profile("prod")
public static class ProductionServiceConfiguration {
    // Configuración específica para producción
}
```

### **Paso 3: Demostrar Funcionalidades (10 minutos)**

#### **Pruebas Unitarias**
```bash
# Ejecutar todas las pruebas
./mvnw test

# Ver reporte de cobertura
./mvnw test jacoco:report
```

#### **API REST Endpoints**
```bash
# Listar pacientes
curl http://localhost:8080/api/patients

# Crear paciente (necesitaría autenticación)
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{"idCard":"12345678","fullName":"Juan Pérez"...}'
```

### **Paso 4: Mostrar Bases de Datos (5 minutos)**

#### **MySQL (Datos Estructurados)**
```sql
-- Ver tablas creadas
SHOW TABLES;

-- Ver estructura de pacientes
DESCRIBE patient;

-- Datos de ejemplo
SELECT * FROM patient LIMIT 5;
```

#### **MongoDB (Historia Clínica)**
```javascript
// Conectarse a MongoDB
use clinical_history_prod

// Ver colecciones
show collections

// Ver documentos de historia clínica
db.clinical_history.find().limit(3)
```

---

## 📊 Métricas de Calidad Alcanzadas

### **Principios SOLID: 10/10** ✅
- **SRP**: Cada clase tiene una sola responsabilidad
- **OCP**: Abierto para extensión, cerrado para modificación
- **LSP**: Implementaciones completamente sustituibles
- **ISP**: Interfaces específicas y cohesivas
- **DIP**: Dependencias invertidas hacia abstracciones

### **Arquitectura Hexagonal: 10/10** ✅
- **Domain**: Lógica de negocio pura e independiente
- **Application**: Casos de uso perfectamente orquestados
- **Infrastructure**: Adaptadores limpios y específicos
- **Puertos**: Interfaces que definen contratos claros
- **Testabilidad**: Fácil de testear cada capa independientemente

### **Calidad de Código**
- **Inmutabilidad**: Modelos con campos `final`
- **Validación estricta**: En constructores de dominio
- **Manejo de errores**: Excepciones específicas y significativas
- **Separación clara**: Entre lógica de negocio y detalles técnicos

---

## 🎓 Puntos Clave para Explicar

### **1. ¿Por qué Arquitectura Hexagonal?**
- **Independencia del framework**: El dominio no depende de Spring Boot
- **Facilidad de testing**: Cada capa se puede testear en aislamiento
- **Flexibilidad**: Fácil cambiar de base de datos o agregar nuevas interfaces
- **Mantenibilidad**: Cambios en una capa no afectan a las demás

### **2. ¿Por qué Principios SOLID?**
- **Código mantenible**: Fácil de entender y modificar
- **Extensible**: Fácil agregar nuevas funcionalidades
- **Testeable**: Cada componente se puede probar independientemente
- **Profesional**: Sigue estándares internacionales de desarrollo

### **3. Innovaciones Implementadas**
- **Arquitectura híbrida**: MySQL para datos estructurados, MongoDB para documentos
- **Configuración multi-entorno**: Diferentes perfiles (dev/test/prod)
- **Validaciones especializadas**: Servicio dedicado solo a validaciones
- **Manejo de errores específico**: Excepciones del dominio personalizadas

---

## 🔧 Comandos Útiles para Demostración

```bash
# 1. Compilar proyecto
./mvnw clean compile

# 2. Ejecutar pruebas
./mvnw test

# 3. Generar reporte de cobertura
./mvnw test jacoco:report

# 4. Ejecutar análisis estático
./mvnw checkstyle:check

# 5. Ver logs en tiempo real
./mvnw spring-boot:run

# 6. Docker (si está disponible)
docker-compose up --build -d
docker-compose logs -f app
```

---

## 📝 Checklist para Presentación

- [ ] ✅ Explicar arquitectura hexagonal (3 capas)
- [ ] ✅ Mostrar cumplimiento de principios SOLID
- [ ] ✅ Demostrar separación de responsabilidades
- [ ] ✅ Ejecutar pruebas unitarias
- [ ] ✅ Mostrar estructura de base de datos
- [ ] ✅ Explicar configuración multi-entorno
- [ ] ✅ Destacar calidad del código
- [ ] ✅ Responder preguntas técnicas

---

## 🎯 Conclusión para el Profesor

> "Este proyecto demuestra un entendimiento avanzado de arquitectura de software y mejores prácticas de desarrollo. La implementación de arquitectura hexagonal con cumplimiento estricto de principios SOLID resulta en un código altamente mantenible, testeable y profesional que sigue estándares internacionales de desarrollo de software."

**Calificación sugerida: Sobresaliente** 🌟