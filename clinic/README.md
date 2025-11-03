# 🔧 Backend - Sistema de Gestión Clínica

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8.6-red.svg)](https://maven.apache.org/)

## 📋 Descripción

El backend del **Sistema de Gestión Clínica** es una API REST robusta construida con Spring Boot, que proporciona servicios backend completos para la gestión integral de clínicas médicas. Implementa arquitectura de microservicios con Domain-Driven Design (DDD).

## ✨ Características

- 🏗️ **Arquitectura DDD**: Domain-Driven Design con capas bien definidas
- 🔐 **Seguridad JWT**: Autenticación y autorización robusta
- 📊 **Múltiples Bases de Datos**: PostgreSQL + MongoDB + Redis
- 🧪 **Testing Completo**: Cobertura >80% con JUnit y Mockito
- 📚 **API Documentada**: Swagger/OpenAPI integrada
- 🚀 **CI/CD**: GitHub Actions con calidad garantizada
- 🐳 **Container Ready**: Docker optimizado para producción
- 📈 **Monitoreo**: Actuator con métricas detalladas

## 🛠️ Tecnologías

### **Framework & Runtime**
- **Java 17**: Lenguaje principal con LTS
- **Spring Boot 3.0**: Framework de aplicaciones
- **Spring Security 6.0**: Seguridad y autenticación
- **Spring Data JPA**: Persistencia relacional
- **Spring Data MongoDB**: Persistencia NoSQL

### **Bases de Datos**
- **PostgreSQL 15**: Datos relacionales
- **MongoDB 7.0**: Historia clínica NoSQL
- **Redis 7.0**: Caché y sesiones

### **Testing & Quality**
- **JUnit 5**: Framework de testing
- **Mockito**: Mocks y stubs
- **JaCoCo**: Cobertura de código
- **Testcontainers**: Tests de integración

### **DevOps & Tools**
- **Maven**: Gestión de dependencias
- **Docker**: Containerización
- **GitHub Actions**: CI/CD
- **Swagger**: Documentación API

## 🚀 Inicio Rápido

### **Prerrequisitos**
- Java 17 JDK
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL y MongoDB (o usar Docker)

### **Instalación**

```bash
# Clonar repositorio
git clone https://github.com/santiagosuaza26/Construccion2SantiagoSuaza
cd Construccion2SantiagoSuaza/clinic

# Instalar dependencias
mvn clean install

# Iniciar con Maven (desarrollo)
mvn spring-boot:run

# O con Docker
docker-compose up -d
```

### **Verificar Instalación**

```bash
# Health check
curl http://localhost:8080/actuator/health

# API Documentation
open http://localhost:8080/swagger-ui.html

# Database status
curl http://localhost:8080/actuator/info
```

## 📁 Estructura del Proyecto

```
clinic/
├── src/main/java/app/clinic/
│   ├── application/           # 📋 Capa de Aplicación
│   │   ├── usecase/          # Casos de uso
│   │   └── mapper/           # Mapeadores DTO
│   ├── domain/               # 🎯 Capa de Dominio (DDD)
│   │   ├── model/            # Modelos de dominio
│   │   │   ├── entities/     # Entidades
│   │   │   ├── valueobject/  # Value Objects
│   │   │   └── exception/    # Excepciones de dominio
│   │   ├── repository/       # Interfaces de repositorio
│   │   └── service/          # Servicios de dominio
│   └── infrastructure/       # 🏗️ Capa de Infraestructura
│       ├── config/           # Configuraciones
│       ├── controller/       # Controladores REST
│       ├── dto/              # Data Transfer Objects
│       ├── exception/        # Manejo de excepciones
│       ├── persistence/      # Capa de persistencia
│       └── service/          # Servicios de infraestructura
├── src/test/java/            # 🧪 Tests
│   ├── application/
│   ├── domain/
│   └── infrastructure/
├── src/main/resources/       # ⚙️ Recursos
│   ├── application.properties
│   ├── data.sql              # Datos iniciales
│   ├── schema.sql            # Esquema BD
│   └── db/migration/         # Migraciones Flyway
└── pom.xml                   # 📦 Configuración Maven
```

## 🏗️ Arquitectura DDD

### **Capas del Sistema**

#### **1. Domain Layer (Dominio)**
- **Entities**: Objetos con identidad (Patient, Doctor, Appointment)
- **Value Objects**: Objetos sin identidad (Email, Phone, Address)
- **Domain Services**: Lógica de negocio compleja
- **Repositories**: Interfaces para persistencia
- **Domain Events**: Eventos del dominio

#### **2. Application Layer (Aplicación)**
- **Use Cases**: Casos de uso de la aplicación
- **DTOs**: Objetos de transferencia de datos
- **Mappers**: Conversión entre capas
- **Application Services**: Coordinación de casos de uso

#### **3. Infrastructure Layer (Infraestructura)**
- **Controllers**: Endpoints REST
- **Repositories Impl**: Implementaciones de repositorio
- **External Services**: Integraciones externas
- **Configurations**: Configuraciones técnicas

### **Principios DDD Aplicados**

- ✅ **Ubiquitous Language**: Lenguaje común con dominio médico
- ✅ **Bounded Contexts**: Contextos delimitados claros
- ✅ **Aggregates**: Agregados consistentes
- ✅ **Domain Events**: Eventos de dominio
- ✅ **Repositories**: Abstracción de persistencia

## 🔐 Seguridad

### **Autenticación JWT**

```java
// Configuración JWT
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

### **Roles y Permisos**

| Rol | Descripción | Permisos Clave |
|-----|-------------|----------------|
| **ADMIN** | Administrador | Gestión completa del sistema |
| **HUMAN_RESOURCES** | RRHH | Gestión de usuarios |
| **ADMINISTRATIVE_STAFF** | Administrativo | Gestión de pacientes |
| **SUPPORT_STAFF** | Soporte | Gestión de inventario |
| **DOCTOR** | Médico | Acceso a historias clínicas |
| **NURSE** | Enfermera | Registro de signos vitales |

## 🗄️ Base de Datos

### **Arquitectura Multi-Base de Datos**

#### **PostgreSQL - Datos Relacionales**
```sql
-- Usuarios del sistema
CREATE TABLE users (
    cedula VARCHAR(20) PRIMARY KEY,
    username VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN DEFAULT true
);

-- Pacientes
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    national_id VARCHAR(20) UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20)
);
```

#### **MongoDB - Historia Clínica**
```javascript
// Documento de historia clínica
{
  "_id": ObjectId("..."),
  "patientNationalId": "CC-12345678",
  "records": [
    {
      "date": "2024-01-15T10:30:00Z",
      "diagnosis": "Hipertensión arterial",
      "vitalSigns": {
        "bloodPressureSystolic": 140,
        "bloodPressureDiastolic": 90,
        "heartRate": 75
      },
      "medications": ["Enalapril 10mg"],
      "notes": "Paciente responde bien al tratamiento"
    }
  ]
}
```

#### **Redis - Caché y Sesiones**
```bash
# Sesiones de usuario
SET session:user:12345 "{userId:12345, role:'DOCTOR', expires:1640995200}"

# Caché de consultas frecuentes
SET cache:patients:active "[{id:1, name:'Juan Pérez'}, ...]"
```

## 🧪 Testing

### **Estrategia de Testing**

#### **1. Unit Tests**
```java
@SpringBootTest
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void shouldCreatePatientSuccessfully() {
        // Given
        var patient = new Patient("CC-123", "Juan", "Pérez");

        // When
        patientService.createPatient(patient);

        // Then
        verify(patientRepository).save(patient);
    }
}
```

#### **2. Integration Tests**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PatientControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetPatientById() {
        // Test completo con base de datos
        var response = restTemplate.getForEntity("/api/patients/123", PatientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

#### **3. Testcontainers**
```java
@Testcontainers
@SpringBootTest
class PatientRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

### **Cobertura de Código**

```bash
# Ejecutar tests con cobertura
mvn clean test jacoco:report

# Ver reporte en target/site/jacoco/index.html
```

- ✅ **Líneas**: >80%
- ✅ **Ramas**: >70%
- ✅ **Clases**: >90%

## 📚 API Documentation

### **Swagger/OpenAPI**

La documentación completa está disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### **Endpoints Principales**

#### **Autenticación**
```http
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
```

#### **Pacientes**
```http
GET    /api/patients
POST   /api/patients
GET    /api/patients/{id}
PUT    /api/patients/{id}
DELETE /api/patients/{id}
```

#### **Citas Médicas**
```http
GET    /api/appointments
POST   /api/appointments
PUT    /api/appointments/{id}/status
```

#### **Historia Clínica**
```http
GET    /api/medical-records/{patientId}
POST   /api/medical-records
PUT    /api/medical-records/{id}
```

## 🚀 Despliegue

### **Con Docker**

```dockerfile
# Dockerfile optimizado
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER appuser
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Con Docker Compose**

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - postgres
      - mongodb
      - redis

  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: clinic_management
      POSTGRES_USER: clinic_user
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}

  mongodb:
    image: mongo:7-jammy
    environment:
      MONGO_INITDB_DATABASE: clinic_history

  redis:
    image: redis:7-alpine
```

## 📊 Monitoreo

### **Spring Boot Actuator**

```bash
# Health check
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics

# Información del sistema
curl http://localhost:8080/actuator/info

# Logs
curl http://localhost:8080/actuator/loggers
```

### **Métricas Disponibles**

- **JVM**: Memoria, GC, threads
- **HTTP**: Requests, responses, errores
- **Base de datos**: Conexiones, queries
- **Negocio**: Métricas personalizadas

## 🔧 Configuración

### **application.properties**

```properties
# Base de datos PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/clinic_management
spring.datasource.username=clinic_user
spring.datasource.password=${DB_PASSWORD}

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/clinic_history

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=86400000

# Logging
logging.level.app.clinic=INFO
logging.level.org.springframework.security=DEBUG
```

### **Perfiles de Spring**

- **dev**: Desarrollo con H2 database
- **test**: Testing con configuraciones específicas
- **prod**: Producción con PostgreSQL + MongoDB

## 🐛 Troubleshooting

### **Problemas Comunes**

#### **Error de Conexión a BD**
```bash
# Verificar servicios Docker
docker-compose ps

# Ver logs de base de datos
docker-compose logs postgres

# Conectar manualmente
docker exec -it clinic-postgres psql -U clinic_user -d clinic_management
```

#### **Error de Memoria**
```bash
# Configurar JVM
java -Xmx2g -Xms1g -XX:+UseG1GC -jar app.jar

# O en Dockerfile
ENV JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC"
```

#### **Tests Fallando**
```bash
# Ejecutar tests específicos
mvn test -Dtest=PatientServiceTest

# Debug mode
mvn test -DforkCount=0 -DreuseForks=false
```

## 📈 Optimización

### **Performance Tuning**

#### **Base de Datos**
```sql
-- Índices optimizados
CREATE INDEX idx_patients_national_id ON patients(national_id);
CREATE INDEX idx_appointments_date_status ON appointments(date, status);

-- Query optimization
EXPLAIN ANALYZE SELECT * FROM patients WHERE national_id = 'CC-123';
```

#### **Aplicación**
```java
// Caché con Redis
@Cacheable("patients")
public Patient getPatientById(String id) {
    return patientRepository.findById(id).orElseThrow();
}

// Async processing
@Async
public CompletableFuture<Void> processMedicalRecord(MedicalRecord record) {
    // Procesamiento pesado
}
```

## 🤝 Contribución

### **Estándares de Código**

1. **Java Style Guide**: Google Java Style
2. **Commits**: Conventional Commits
3. **PRs**: Code review obligatorio
4. **Tests**: Cobertura >80%

### **Flujo de Desarrollo**

```bash
# 1. Crear rama
git checkout -b feature/patient-registration

# 2. Desarrollar con TDD
mvn test

# 3. Commit
git commit -m "feat: add patient registration endpoint"

# 4. Push y PR
git push origin feature/patient-registration
```

## 📞 Soporte

- **📧 Email**: backend@clinic.com
- **🐛 Issues**: [GitHub Issues](https://github.com/santiagosuaza26/Construccion2SantiagoSuaza/issues)
- **📖 Docs**: [Documentación Completa](../README.md)

---

*"Backend robusto para la transformación digital de la salud"*

🔧 Desarrollado con ❤️ por el equipo backend