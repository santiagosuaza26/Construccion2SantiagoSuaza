# 🌟 GUÍA DE MEJORES PRÁCTICAS - ARQUITECTURA LIMPIA

## 🎯 **PRINCIPIOS FUNDAMENTALES**

### **🏗️ Principio de Responsabilidad Única (SRP)**
```java
// ✅ CORRECTO - Una responsabilidad por clase
public class UserDomainService {
    public User createUser(User user) {
        // Solo lógica de creación de usuarios
    }
}

// ❌ INCORRECTO - Múltiples responsabilidades
public class UserService {
    public User createUser(User user) { /* lógica de dominio */ }
    public UserDTO toDTO(User user) { /* lógica de presentación */ }
    public UserEntity toEntity(User user) { /* lógica de infraestructura */ }
}
```

### **🔒 Principio de Inversión de Dependencias (DIP)**
```java
// ✅ CORRECTO - Dependencia de abstracción
public class UserDomainService {
    private final UserRepository userRepository;  // ✅ Interfaz

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

// ❌ INCORRECTO - Dependencia de implementación
public class UserDomainService {
    private final UserJpaRepository userRepository;  // ❌ Implementación concreta

    public UserDomainService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

---

## 📝 **1. ESCRITURA DE CÓDIGO LIMPIO**

### **🏗️ Convenciones de Naming**

#### **✅ Paquetes**
```java
// ✅ CORRECTO
app.clinic.domain.model
app.clinic.domain.service
app.clinic.domain.port
app.clinic.application.service
app.clinic.application.dto
app.clinic.infrastructure.adapter
app.clinic.infrastructure.repository
```

#### **✅ Clases**
```java
// ✅ CORRECTO
public class UserDomainService      // Servicio del dominio
public class UserApplicationService // Servicio de aplicación
public class UserRepositoryAdapter  // Adaptador de repositorio
public class CreateUserUseCase      // Caso de uso
public class UserCreatedEvent       // Evento del dominio
```

#### **✅ Métodos**
```java
// ✅ CORRECTO
public User createUser(User user)
public Optional<User> findUserById(UserId userId)
public void validateUserForCreation(User user)
public boolean canUserAccessPatient(User user, Patient patient)

// ❌ INCORRECTO
public User create(User user)                    // Muy genérico
public User getUser(String id)                   // No indica origen
public void checkUser(User user)                 // No indica validación
public boolean access(User user, Patient patient) // Muy vago
```

### **📏 Longitud de Clases y Métodos**

#### **✅ Clases del Dominio**
```java
// ✅ IDEAL - Clase enfocada y pequeña
public class UserDomainService {
    private final UserRepository userRepository;

    public User createUser(User user) { /* 15 líneas */ }
    public User updateUser(User user) { /* 12 líneas */ }
    public Optional<User> findUserById(UserId userId) { /* 3 líneas */ }
    private void validateUserForCreation(User user) { /* 8 líneas */ }
}
// Total: ~40 líneas
```

#### **❌ Anti-Pattern: Clases Grandes**
```java
// ❌ PROBLEMÁTICO - Clase con demasiadas responsabilidades
public class UserService {
    // 50+ métodos públicos
    // 500+ líneas de código
    // Múltiples responsabilidades mezcladas
}
```

### **🔒 Inmutabilidad y Encapsulación**

#### **✅ Value Objects Inmutables**
```java
// ✅ CORRECTO - Value Object inmutable
public class UserCedula {
    private final String value;

    private UserCedula(String value) {
        this.value = value;
    }

    public static UserCedula of(String value) {
        return new UserCedula(value);
    }

    public String getValue() {
        return value;
    }

    // Sin setters - completamente inmutable
}
```

#### **✅ Entidades con Comportamiento**
```java
// ✅ CORRECTO - Entidad con lógica de negocio
public class User {
    public boolean canManageUsers() {
        return this.role == UserRole.HUMAN_RESOURCES;
    }

    public boolean canViewPatientInfo() {
        return this.role == UserRole.DOCTOR ||
               this.role == UserRole.NURSE ||
               this.role == UserRole.ADMINISTRATIVE_STAFF;
    }
}
```

---

## 🏗️ **2. PATRONES DE DISEÑO ESPECÍFICOS**

### **🏭 Factory Pattern para Servicios del Dominio**
```java
@Configuration
public class DomainServiceFactory {

    @Bean
    public UserDomainService userDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    @Bean
    public PatientDomainService patientDomainService(PatientRepository patientRepository) {
        return new PatientDomainService(patientRepository);
    }
}
```

### **🔌 Adapter Pattern para Repositorios**
```java
// Puerto (Interfaz del dominio)
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId userId);
}

// Adaptador (Implementación de infraestructura)
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
```

### **🎭 Strategy Pattern para Validaciones**
```java
public interface ValidationStrategy {
    void validate(User user) throws ValidationException;
}

@Component
public class UserCreationValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(User user) throws ValidationException {
        // Lógica específica de validación para creación
    }
}

@Component
public class UserUpdateValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(User user) throws ValidationException {
        // Lógica específica de validación para actualización
    }
}
```

### **👁️ Observer Pattern para Eventos del Dominio**
```java
// Evento del dominio
public class UserCreatedEvent {
    private final UserId userId;
    private final LocalDateTime occurredOn;

    public UserCreatedEvent(UserId userId) {
        this.userId = userId;
        this.occurredOn = LocalDateTime.now();
    }
}

// Publisher de eventos
public class DomainEventPublisher {
    private final List<DomainEventListener> listeners = new ArrayList<>();

    public void subscribe(DomainEventListener listener) {
        listeners.add(listener);
    }

    public void publish(DomainEvent event) {
        listeners.forEach(listener -> listener.handle(event));
    }
}
```

---

## 🧪 **3. TESTING Y CALIDAD**

### **🎯 Testing del Dominio**
```java
// ✅ Test independiente sin Spring
public class UserDomainServiceTest {

    private UserRepository userRepository;
    private UserDomainService userDomainService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDomainService = new UserDomainService(userRepository);
    }

    @Test
    void shouldCreateUserWithValidData() {
        // Given
        User user = createValidUser();

        // When
        when(userRepository.save(user)).thenReturn(user);
        User result = userDomainService.createUser(user);

        // Then
        assertNotNull(result);
        verify(userRepository).save(user);
    }
}
```

### **📊 Cobertura de Código**
```java
// ✅ Tests con alta cobertura
@Test
@DisplayName("Debe crear usuario cuando los datos son válidos")
void shouldCreateUserWhenDataIsValid() { }

@Test
@DisplayName("No debe crear usuario cuando la cédula ya existe")
void shouldNotCreateUserWhenCedulaAlreadyExists() { }

@Test
@DisplayName("Debe lanzar excepción cuando el usuario es nulo")
void shouldThrowExceptionWhenUserIsNull() { }
```

---

## 📦 **4. GESTIÓN DE ERRORES**

### **🚨 Excepciones del Dominio**
```java
// ✅ Excepciones específicas del dominio
public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(UserCedula cedula) {
        super("User with cedula " + cedula.getValue() + " already exists");
    }
}

public class InvalidUserDataException extends DomainException {
    public InvalidUserDataException(String reason) {
        super("Invalid user data: " + reason);
    }
}
```

### **🔄 Manejo de Errores en Servicios del Dominio**
```java
// ✅ Manejo de errores en dominio
public class UserDomainService {

    public User createUser(User user) {
        if (userRepository.existsByCedula(user.getCedula())) {
            throw new UserAlreadyExistsException(user.getCedula());
        }

        if (!isValidUserData(user)) {
            throw new InvalidUserDataException("Invalid user information");
        }

        return userRepository.save(user);
    }
}
```

### **🌐 Manejo de Errores en Controladores**
```java
// ✅ Controladores manejan errores de infraestructura
@RestController
public class UserController {

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO result = userApplicationService.createUser(request);
            return ResponseEntity.ok(result);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InvalidUserDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
```

---

## 🔧 **5. CONFIGURACIÓN Y DESPLIEGUE**

### **🏗️ Configuración por Entornos**
```properties
# application-dev.properties
spring.profiles.active=dev
domain.validation.enabled=true
infrastructure.cache.enabled=false
logging.level.app.clinic.domain=DEBUG

# application-prod.properties
spring.profiles.active=prod
domain.validation.enabled=true
infrastructure.cache.enabled=true
logging.level.app.clinic.domain=WARN
```

### **📦 Empaquetado Independiente**
```xml
<build>
    <plugins>
        <!-- JAR del dominio independiente -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
                <classifier>domain</classifier>
                <includes>
                    <include>app/clinic/domain/**</include>
                </includes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## 📋 **6. REGLAS DE DESARROLLO**

### **✅ Checklist para Nuevas Funcionalidades**

#### **🏗️ Antes de Codificar**
- [ ] ✅ Caso de uso claramente definido
- [ ] ✅ Interfaces de dominio diseñadas
- [ ] ✅ Value Objects identificados
- [ ] ✅ Eventos del dominio considerados

#### **💻 Durante la Codificación**
- [ ] ✅ Servicios del dominio sin anotaciones Spring
- [ ] ✅ Constructor injection únicamente
- [ ] ✅ Métodos privados para validaciones complejas
- [ ] ✅ Documentación clara de responsabilidades

#### **🧪 Antes de Commit**
- [ ] ✅ Tests unitarios del dominio creados
- [ ] ✅ Tests de integración implementados
- [ ] ✅ Cobertura > 90% en dominio
- [ ] ✅ Verificación de arquitectura con ArchUnit

### **🚨 Reglas de Oro**

#### **✅ LO QUE SIEMPRE HACER**
- ✅ Usar interfaces para definir puertos del dominio
- ✅ Mantener servicios del dominio sin anotaciones
- ✅ Usar constructor injection exclusivamente
- ✅ Crear tests independientes para el dominio
- ✅ Documentar responsabilidades de cada capa

#### **❌ LO QUE NUNCA HACER**
- ❌ Agregar `@Service` en servicios del dominio
- ❌ Importar clases de infraestructura en dominio
- ❌ Usar `@Autowired` en servicios del dominio
- ❌ Crear dependencias circulares entre capas
- ❌ Saltar tests del dominio

---

## 🎨 **7. PATRONES ESPECÍFICOS**

### **📝 Domain Model Pattern**
```java
// ✅ Value Object con validación
public class UserCedula {
    private final String value;

    private UserCedula(String value) {
        if (value == null || !isValidCedula(value)) {
            throw new IllegalArgumentException("Invalid cedula format");
        }
        this.value = value;
    }

    public static UserCedula of(String value) {
        return new UserCedula(value);
    }
}

// ✅ Entity con comportamiento
public class User {
    public boolean canManageUsers() {
        return this.role == UserRole.HUMAN_RESOURCES;
    }

    public boolean isEligibleForPromotion() {
        return this.active && this.getAge() > 21;
    }
}
```

### **🎯 Use Case Pattern**
```java
// ✅ Caso de uso coordinado
@Service
public class RegisterPatientUseCase {

    private final PatientDomainService patientDomainService;
    private final DomainEventPublisher eventPublisher;

    public PatientDTO execute(RegisterPatientRequest request) {
        Patient patient = mapToDomain(request);
        Patient registeredPatient = patientDomainService.registerPatient(patient);

        eventPublisher.publish(new PatientRegisteredEvent(registeredPatient.getId()));

        return mapToDTO(registeredPatient);
    }
}
```

### **🔌 Repository Pattern**
```java
// ✅ Puerto del dominio
public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(PatientId patientId);
    List<Patient> findByStatus(PatientStatus status);
}

// ✅ Adaptador de infraestructura
@Repository
public class PatientRepositoryAdapter implements PatientRepository {

    private final PatientJpaRepository jpaRepository;
    private final PatientEntityMapper mapper;

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = mapper.toEntity(patient);
        PatientEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
```

---

## 📊 **8. MÉTRICAS Y MONITOREO**

### **📈 Métricas de Código**
```java
// ✅ Clases pequeñas y enfocadas
public class UserDomainService {
    // < 50 líneas
    // < 10 métodos públicos
    // 1 responsabilidad clara
}

// ✅ Métodos pequeños y testeables
public User createUser(User user) {
    validateUserForCreation(user);  // < 10 líneas
    return userRepository.save(user); // 1 línea
}
```

### **🔍 Herramientas de Análisis**
```bash
# Análisis de arquitectura
mvn test -Dtest="*ArchitectureTest"

# Métricas de código
mvn sonar:sonar

# Análisis de dependencias
mvn jdepend:generate

# Cobertura de tests
mvn jacoco:report
```

---

## 🚀 **9. EVOLUCIÓN Y MANTENIMIENTO**

### **🔄 Proceso de Refactorización**
1. **Identificar** código que viola arquitectura limpia
2. **Crear** tests para preservar comportamiento
3. **Refactorizar** moviendo código a capa correcta
4. **Verificar** que tests siguen pasando
5. **Eliminar** código técnico del dominio

### **📝 Checklist de Mantenimiento**
- [ ] ✅ Cobertura de dominio > 90%
- [ ] ✅ Tiempo de tests unitarios < 5 segundos
- [ ] ✅ 0 dependencias externas en dominio
- [ ] ✅ 0 ciclos de dependencia entre capas
- [ ] ✅ Documentación actualizada

---

## 🎯 **CONCLUSIÓN**

Esta guía establece las bases para mantener un código limpio y sostenible que aprovecha la independencia del dominio lograda. Los principios clave son:

### **🏆 Principios Inquebrantables**
- **El dominio es sagrado** - Nunca agregar dependencias externas
- **Las capas tienen responsabilidades únicas** - No mezclar lógica de negocio con detalles técnicos
- **Los tests del dominio son independientes** - Siempre testeables sin frameworks
- **Las interfaces definen contratos claros** - Comunicación limpia entre capas

### **🛠️ Herramientas Esenciales**
- **Arquitectura Limpia** como guía filosófica
- **Domain-Driven Design** para modelado del negocio
- **TDD** para desarrollo guiado por tests
- **ArchUnit** para verificación automática de arquitectura

### **📊 Métricas de Éxito**
- **Dominio 100% independiente** de frameworks externos
- **Tests unitarios ejecutándose en < 5 segundos**
- **Cobertura > 90%** en lógica de negocio
- **0 ciclos de dependencia** entre capas

**Con estas prácticas, el sistema se mantendrá limpio, mantenible y preparado para evolución tecnológica sostenible.**