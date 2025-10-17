# 📦 GUÍA DE GESTIÓN DE DEPENDENCIAS - ARQUITECTURA LIMPIA

## 🎯 **PRINCIPIOS FUNDAMENTALES**

### **🔒 Regla de Dependencia (Dependency Rule)**
```
Dependencias solo pueden apuntar hacia capas internas:

    Framework → Aplicación → Dominio ← ❌ (VIOLACIÓN)
    Framework ← Aplicación ← Dominio ← ✅ (CORRECTO)
```

### **📊 Matriz de Dependencias Permitidas**

| Capa Fuente | Dominio | Aplicación | Infraestructura | Framework |
|-------------|---------|------------|-----------------|-----------|
| **Dominio** | ✅ | ❌ | ❌ | ❌ |
| **Aplicación** | ✅ | ✅ | ❌ | ❌ |
| **Infraestructura** | ✅ | ✅ | ✅ | ✅ |
| **Framework** | ✅ | ✅ | ✅ | ✅ |

---

## 📦 **1. GESTIÓN DE DEPENDENCIAS MAVEN**

### **🏗️ Estructura de Módulos Recomendada**
```xml
<project>
    <groupId>app.clinic</groupId>
    <artifactId>clinic-management</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <modules>
        <module>clinic-domain</module>
        <module>clinic-application</module>
        <module>clinic-infrastructure</module>
        <module>clinic-web</module>
    </modules>
</project>
```

### **📦 Módulo del Dominio (Independiente)**
```xml
<!-- clinic-domain/pom.xml -->
<project>
    <artifactId>clinic-domain</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- ✅ SOLO dependencias mínimas de Java -->
        <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
            <version>2.0.1.Final</version>
        </dependency>
    </dependencies>
</project>
```

### **📦 Módulo de Aplicación**
```xml
<!-- clinic-application/pom.xml -->
<project>
    <artifactId>clinic-application</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- ✅ Dependencia del dominio -->
        <dependency>
            <groupId>app.clinic</groupId>
            <artifactId>clinic-domain</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!-- ✅ Spring Framework para coordinación -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>
    </dependencies>
</project>
```

### **📦 Módulo de Infraestructura**
```xml
<!-- clinic-infrastructure/pom.xml -->
<project>
    <artifactId>clinic-infrastructure</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- ✅ Dependencias del dominio y aplicación -->
        <dependency>
            <groupId>app.clinic</groupId>
            <artifactId>clinic-domain</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>app.clinic</groupId>
            <artifactId>clinic-application</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!-- ✅ Tecnologías de infraestructura -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
    </dependencies>
</project>
```

---

## 🔧 **2. CONFIGURACIÓN POR CAPAS**

### **🏗️ Configuración del Dominio**
```java
@Configuration
@ComponentScan(
    basePackages = "app.clinic.domain",
    excludeFilters = @ComponentScan.Filter(Configuration.class)
)
public class DomainConfig {

    @Bean
    public UserDomainService userDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    @Bean
    public PatientDomainService patientDomainService(PatientRepository patientRepository) {
        return new PatientDomainService(patientRepository);
    }

    // ✅ Servicios del dominio sin anotaciones @Service
}
```

### **🔧 Configuración de Infraestructura**
```java
@Configuration
@EnableJpaRepositories(basePackages = "app.clinic.infrastructure.repository")
@EnableTransactionManagement
public class InfrastructureConfig {

    @Bean
    public DataSource dataSource() {
        // ✅ Configuración técnica de base de datos
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        // ✅ Configuración de transacciones
    }
}
```

### **🌐 Configuración Web**
```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "app.clinic.web")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // ✅ Configuración de conversores JSON, XML, etc.
    }
}
```

---

## 📋 **3. ESTRATEGIAS DE DEPENDENCY INJECTION**

### **💉 Inyección en Servicios del Dominio**
```java
// ✅ CORRECTO - Constructor injection en dominio
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### **💉 Inyección en Servicios de Aplicación**
```java
// ✅ CORRECTO - Constructor injection en aplicación
@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;

    public UserApplicationService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }
}
```

### **💉 Inyección en Adaptadores**
```java
// ✅ CORRECTO - Constructor injection en infraestructura
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
}
```

---

## 🔄 **4. GESTIÓN DE EVENTOS DEL DOMINIO**

### **📨 Eventos del Dominio (Independientes)**
```java
// ✅ Evento del dominio sin dependencias externas
public class UserCreatedEvent {

    private final UserId userId;
    private final LocalDateTime occurredOn;

    public UserCreatedEvent(UserId userId) {
        this.userId = userId;
        this.occurredOn = LocalDateTime.now();
    }

    public UserId getUserId() {
        return userId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}
```

### **📡 Publicación de Eventos**
```java
// ✅ Servicio del dominio publica eventos
public class UserDomainService {

    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    public User createUser(User user) {
        validateUserForCreation(user);
        User savedUser = userRepository.save(user);

        // ✅ Publicar evento del dominio
        UserCreatedEvent event = new UserCreatedEvent(savedUser.getId());
        eventPublisher.publish(event);

        return savedUser;
    }
}
```

### **🎧 Manejo de Eventos**
```java
// ✅ Manejador de eventos en aplicación
@Service
public class UserEventHandler {

    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        // ✅ Lógica de respuesta a evento del dominio
        sendWelcomeEmail(event.getUserId());
    }
}
```

---

## 🏭 **5. PATRÓN FACTORY PARA SERVICIOS DEL DOMINIO**

### **🏗️ Factory de Servicios del Dominio**
```java
@Configuration
public class DomainServiceFactory {

    @Bean
    public UserDomainService createUserDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    @Bean
    public PatientDomainService createPatientDomainService(PatientRepository patientRepository) {
        return new PatientDomainService(patientRepository);
    }

    @Bean
    public AppointmentDomainService createAppointmentDomainService(
            AppointmentRepository appointmentRepository) {
        return new AppointmentDomainService(appointmentRepository);
    }
}
```

### **🔧 Factory de Adaptadores**
```java
@Configuration
public class RepositoryAdapterFactory {

    @Bean
    public UserRepository createUserRepository(UserJpaRepository jpaRepository) {
        return new UserRepositoryAdapter(jpaRepository);
    }

    @Bean
    public PatientRepository createPatientRepository(PatientJpaRepository jpaRepository) {
        return new PatientRepositoryAdapter(jpaRepository);
    }
}
```

---

## 📊 **6. VALIDACIÓN DE ARQUITECTURA**

### **🔍 Herramientas de Validación**

#### **ArchUnit - Verificación de Arquitectura**
```java
class ArchitectureTest {

    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..");

    @ArchTest
    static final ArchRule domain_services_should_not_be_annotated =
        noClasses()
            .that().resideInAPackage("..domain.service..")
            .should().beAnnotatedWith(Service.class)
            .orShould().beAnnotatedWith(Component.class);
}
```

#### **JDepend - Análisis de Dependencias**
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>jdepend-maven-plugin</artifactId>
    <version>2.0</version>
</plugin>
```

### **🔍 Comandos de Verificación**
```bash
# Verificar arquitectura con ArchUnit
mvn test -Dtest="*ArchitectureTest"

# Análisis de dependencias con JDepend
mvn jdepend:generate

# Verificar dependencias manualmente
mvn dependency:tree

# Análisis de seguridad de dependencias
mvn org.owasp:dependency-check-maven:check
```

---

## 🚨 **7. DETECCIÓN DE VIOLACIONES**

### **⚠️ Señales de Alerta de Dependencias Incorrectas**

#### **❌ Anti-Pattern: Anotaciones en Dominio**
```java
// ❌ VIOLACIÓN GRAVE
@Service  // 🚨 NO PERMITIDO en dominio
public class UserDomainService {

    @Autowired  // 🚨 NO PERMITIDO en dominio
    private UserRepository userRepository;
}
```

#### **❌ Anti-Pattern: Imports Incorrectos**
```java
// ❌ VIOLACIÓN GRAVE
import org.springframework.stereotype.Service;  // 🚨 NO PERMITIDO en dominio
import org.springframework.beans.factory.annotation.Autowired;  // 🚨 NO PERMITIDO

public class UserDomainService {
    // Lógica del dominio contaminada
}
```

#### **❌ Anti-Pattern: Dependencias Circulares**
```java
// ❌ VIOLACIÓN GRAVE
public class UserDomainService {
    private final PatientDomainService patientService;  // 🚨 Dependencia circular
}
```

### **✅ Patrón Correcto**
```java
// ✅ CORRECTO
public class UserDomainService {

    private final UserRepository userRepository;  // ✅ Solo interfaz del dominio

    public UserDomainService(UserRepository userRepository) {  // ✅ Constructor injection
        this.userRepository = userRepository;
    }
}
```

---

## 📈 **8. MÉTRICAS DE DEPENDENCIAS**

### **📊 Métricas a Monitorear**

| Métrica | Objetivo | Herramienta |
|---------|----------|-------------|
| **Dependencias externas en dominio** | 0 | ArchUnit |
| **Ciclos de dependencia** | 0 | JDepend |
| **Profundidad de herencia** | < 5 | SonarQube |
| **Acoplamiento eferente** | < 10 | JDepend |
| **Estabilidad de paquetes** | > 0.8 | JDepend |

### **📊 Comandos de Análisis**
```bash
# Análisis completo de dependencias
mvn jdepend:generate

# Ver árbol de dependencias
mvn dependency:tree

# Análisis de vulnerabilidades
mvn org.owasp:dependency-check-maven:check

# Métricas de código
mvn sonar:sonar
```

---

## 🔧 **9. GESTIÓN DE VERSIONES**

### **📝 Estrategia de Versionamiento**
```xml
<properties>
    <!-- Versiones sincronizadas -->
    <clinic.domain.version>1.0.0</clinic.domain.version>
    <clinic.application.version>1.0.0</clinic.application.version>
    <clinic.infrastructure.version>1.0.0</clinic.infrastructure.version>

    <!-- Versiones de terceros -->
    <spring.boot.version>3.5.6</spring.boot.version>
    <junit.version>5.10.1</junit.version>
</properties>
```

### **🔄 Gestión de Versiones por Capa**
```xml
<dependencyManagement>
    <dependencies>
        <!-- Módulos internos -->
        <dependency>
            <groupId>app.clinic</groupId>
            <artifactId>clinic-domain</artifactId>
            <version>${clinic.domain.version}</version>
        </dependency>

        <!-- Frameworks externos -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring.boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 🚀 **10. DESPLIEGUE Y EMPAQUETADO**

### **📦 Estrategia de Empaquetado**
```xml
<build>
    <plugins>
        <!-- JAR del dominio independiente -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
                <classifier>domain</classifier>
            </configuration>
        </plugin>

        <!-- JAR de aplicación -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### **🏗️ Configuración Multi-Entorno**
```properties
# application-dev.properties
spring.profiles.active=dev
domain.services.enabled=true
infrastructure.cache.enabled=false

# application-prod.properties
spring.profiles.active=prod
domain.services.enabled=true
infrastructure.cache.enabled=true
```

---

## 🎯 **CONCLUSIÓN**

Esta guía establece las bases para una gestión sólida de dependencias que mantiene la arquitectura limpia lograda. Los puntos clave son:

### **🏆 Principios Inquebrantables**
- **El dominio NUNCA depende de frameworks externos**
- **Las dependencias SOLO apuntan hacia capas internas**
- **Cada capa tiene responsabilidades claramente definidas**
- **Las interfaces definen contratos entre capas**

### **🛠️ Herramientas Esenciales**
- **Maven:** Gestión de dependencias y módulos
- **ArchUnit:** Verificación automática de arquitectura
- **JDepend:** Análisis de métricas de dependencias
- **Spring Boot:** Framework para capas externas

### **📊 Métricas de Éxito**
- **0 dependencias externas en dominio**
- **0 ciclos de dependencia entre capas**
- **> 90% cobertura de tests del dominio**
- **< 5 segundos tiempo de tests unitarios**

**Con esta estrategia, el sistema mantendrá la independencia del dominio lograda mientras permite la evolución tecnológica y la mantenibilidad a largo plazo.**