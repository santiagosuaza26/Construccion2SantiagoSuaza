# 🧪 GUÍA DE TESTING PARA ARQUITECTURA LIMPIA

## 🎯 **ESTRATEGIA DE TESTING**

### **📊 Pirámide de Testing**

```
                End-to-End Tests
                       ▲
                       │
              Integration Tests
                       ▲
                       │
            Unit Tests (Domain)
                       ▲
                       │
           Domain Model Tests
```

### **⏱️ Distribución de Tiempo de Ejecución**
- **Unit Tests (Dominio):** < 5 segundos
- **Integration Tests:** 5-30 segundos
- **End-to-End Tests:** 30+ segundos

---

## 🧪 **1. TESTS DEL DOMINIO (INDEPENDIENTES)**

### **✅ Características**
- **Sin Spring Context** - Ejecución ultrarrápida
- **Sin dependencias externas** - Tests puros
- **Alta cobertura** - > 90% del código del dominio
- **Ejecución aislada** - Sin impacto de otros componentes

### **📝 Ejemplo: Test de Servicio del Dominio**
```java
public class UserDomainServiceTest {

    private UserRepository userRepository;
    private UserDomainService userDomainService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDomainService = new UserDomainService(userRepository);
    }

    @Test
    @DisplayName("Debe crear usuario con datos válidos")
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

    @Test
    @DisplayName("No debe crear usuario con cédula duplicada")
    void shouldNotCreateUserWithDuplicateCedula() {
        // Given
        User user = createValidUser();

        // When
        when(userRepository.existsByCedula(user.getCedula())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            userDomainService.createUser(user);
        });
    }
}
```

### **🏗️ Patrón de Testing: Given-When-Then**
```java
@Test
public void shouldCalculateCopaymentCorrectly() {
    // Given - Preparar datos de prueba
    PatientCedula cedula = PatientCedula.of("12345678");
    TotalCost totalCost = TotalCost.of(Money.of(BigDecimal.valueOf(100000)));

    // When - Ejecutar lógica del dominio
    BillingCalculationResult result = billingService.calculateBilling(cedula, totalCost);

    // Then - Verificar resultado
    assertNotNull(result);
    assertEquals(Money.of(BigDecimal.valueOf(50000)), result.getPatientResponsibility());
}
```

---

## 🧪 **2. TESTS DE MODELOS DEL DOMINIO**

### **📝 Tests de Value Objects**
```java
public class UserCedulaTest {

    @Test
    @DisplayName("Debe crear cédula válida")
    void shouldCreateValidCedula() {
        // When
        UserCedula cedula = UserCedula.of("12345678");

        // Then
        assertNotNull(cedula);
        assertEquals("12345678", cedula.getValue());
    }

    @Test
    @DisplayName("No debe crear cédula con formato inválido")
    void shouldNotCreateCedulaWithInvalidFormat() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            UserCedula.of("abc123");
        });
    }

    @Test
    @DisplayName("Debe ser inmutable")
    void shouldBeImmutable() {
        // Given
        UserCedula cedula = UserCedula.of("12345678");

        // When & Then
        assertThrows(Exception.class, () -> {
            // Intentar modificar (debería fallar)
        });
    }
}
```

### **📝 Tests de Entidades**
```java
public class UserTest {

    @Test
    @DisplayName("Debe crear usuario activo por defecto")
    void shouldCreateActiveUserByDefault() {
        // When
        User user = User.of(cedula, username, password, fullName,
                           birthDate, address, phoneNumber, email, role);

        // Then
        assertTrue(user.isActive());
        assertNotNull(user.getCedula());
        assertNotNull(user.getUsername());
    }

    @Test
    @DisplayName("Debe permitir activar usuario")
    void shouldAllowUserActivation() {
        // Given
        User inactiveUser = User.of(cedula, username, password, fullName,
                                   birthDate, address, phoneNumber, email, role, false);

        // When
        User activatedUser = userDomainService.activateUser(inactiveUser);

        // Then
        assertTrue(activatedUser.isActive());
    }
}
```

---

## 🧪 **3. TESTS DE INTEGRACIÓN**

### **📝 Tests de Servicios de Aplicación**
```java
@SpringBootTest
@Import(DomainConfig.class)  // Solo importa configuración del dominio
public class UserApplicationServiceIntegrationTest {

    @Autowired
    private UserApplicationService userApplicationService;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Debe crear usuario a través del servicio de aplicación")
    void shouldCreateUserThroughApplicationService() {
        // Given
        CreateUserDTO createUserDTO = createValidCreateUserDTO();

        // When
        when(userJpaRepository.save(any())).thenReturn(createUserEntity());

        // Then
        assertDoesNotThrow(() -> {
            UserDTO result = userApplicationService.createUser(createUserDTO);
            assertNotNull(result);
        });
    }
}
```

### **📝 Tests de Adaptadores**
```java
@JdbcTest
public class UserRepositoryAdapterTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Debe guardar usuario en base de datos")
    void shouldSaveUserInDatabase() {
        // Given
        User user = createValidUser();

        // When
        UserEntity savedEntity = userJpaRepository.save(mapper.toEntity(user));

        // Then
        assertNotNull(savedEntity.getId());
        assertEquals(user.getCedula().getValue(), savedEntity.getCedula());
    }
}
```

---

## 🧪 **4. TESTS END-TO-END**

### **📝 Tests de Flujos Completos**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserManagementE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Debe crear usuario completamente desde API hasta BD")
    void shouldCreateUserEndToEnd() {
        // Given
        CreateUserRequest request = createValidRequest();

        // When
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
            "/api/users", request, UserResponse.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }
}
```

---

## 🛠️ **5. HERRAMIENTAS Y FRAMEWORKS**

### **🧪 Frameworks de Testing**
```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- AssertJ -->
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- TestContainers -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### **📊 Herramientas de Cobertura**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

---

## 📋 **6. ESTRATEGIAS ESPECÍFICAS**

### **🎯 Testing del Dominio**
- **Objetivo:** > 95% cobertura
- **Tiempo máximo:** < 3 segundos
- **Mocks:** Solo repositorios
- **Asserts:** Comportamiento y estado

### **🔗 Testing de Adaptadores**
- **Objetivo:** > 80% cobertura
- **Tiempo máximo:** < 15 segundos
- **Mocks:** Dependencias externas
- **Asserts:** Mapeo correcto entidad-dominio

### **🌐 Testing de Controllers**
- **Objetivo:** > 70% cobertura
- **Tiempo máximo:** < 10 segundos
- **Mocks:** Servicios de aplicación
- **Asserts:** HTTP status y responses

### **🔄 Testing de Integración**
- **Objetivo:** > 60% cobertura
- **Tiempo máximo:** < 30 segundos
- **Mocks:** Base de datos externa
- **Asserts:** Flujos completos

---

## 🚨 **7. BUENAS PRÁCTICAS**

### **✅ Lo Que Sí Hacer**
- ✅ Usar nombres descriptivos en tests
- ✅ Seguir patrón Given-When-Then
- ✅ Mantener tests independientes
- ✅ Usar datos de prueba consistentes
- ✅ Verificar comportamiento, no implementación

### **❌ Lo Que No Hacer**
- ❌ Tests que dependen de orden de ejecución
- ❌ Tests con sleeps o waits innecesarios
- ❌ Tests que modifican estado global
- ❌ Tests con lógica compleja de setup
- ❌ Tests que verifican demasiadas cosas

### **📝 Convenciones de Naming**
```java
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

## 📊 **8. MÉTRICAS Y OBJETIVOS**

### **📈 Objetivos de Cobertura**
- **Dominio:** > 95%
- **Aplicación:** > 85%
- **Infraestructura:** > 75%
- **Total del proyecto:** > 80%

### **⏱️ Objetivos de Performance**
- **Tests unitarios:** < 5 segundos
- **Tests de integración:** < 30 segundos
- **Tests E2E:** < 2 minutos
- **Suite completa:** < 5 minutos

### **🔢 Métricas de Calidad**
- **Tests por clase:** > 3
- **Asserts por test:** 2-5
- **Líneas por test:** < 20
- **Tiempo por test:** < 1 segundo

---

## 🚀 **9. EJECUCIÓN Y REPORTING**

### **🏃 Comandos de Ejecución**
```bash
# Tests unitarios del dominio (rápidos)
mvn test -Dtest="*DomainServiceTest"

# Tests de integración
mvn test -Dtest="*IntegrationTest"

# Tests E2E
mvn test -Dtest="*E2ETest"

# Todos los tests con cobertura
mvn test jacoco:report

# Tests por perfil
mvn test -P unit-tests
mvn test -P integration-tests
```

### **📊 Reportes de Cobertura**
```bash
# Generar reporte HTML de cobertura
mvn jacoco:report

# Reporte en consola
mvn test jacoco:report
```

---

## 🎯 **CONCLUSIÓN**

Esta guía establece las bases para un testing efectivo que aprovecha la independencia del dominio lograda. Los principios clave son:

- **🧪 Tests del dominio independientes y rápidos**
- **🔗 Tests de integración enfocados en coordinación**
- **🌐 Tests E2E para validar flujos completos**
- **📊 Métricas claras y objetivos medibles**
- **🛠️ Herramientas adecuadas para cada tipo de test**

**Con esta estrategia, el sistema tendrá una base sólida de tests que garantizará la calidad y mantenibilidad del código.**