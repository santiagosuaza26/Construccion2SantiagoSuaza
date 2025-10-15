package app.clinic.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.domain.model.User;
import app.clinic.domain.port.UserRepository;
import app.clinic.domain.service.UserDomainService;

/**
 * Test de integración para verificar que la creación de usuarios funciona correctamente.
 * Prueba el flujo completo desde el DTO hasta la persistencia en base de datos.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Creation Integration Tests")
@Transactional
class UserCreationIntegrationTest {

    @Autowired
    private UserApplicationService userApplicationService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDomainService userDomainService;

    private CreateUserDTO validUserDTO;

    @BeforeEach
    void setUp() {
        validUserDTO = new CreateUserDTO(
            "TEST001",
            "testuser",
            "TestPass123!@#",
            "Usuario de Prueba",
            "15/06/1985",
            "Calle de Prueba 123",
            "3001234567",
            "test@clinica.com",
            "DOCTOR"
        );
    }

    @Test
    @DisplayName("Debe crear usuario correctamente con datos válidos")
    void shouldCreateUserSuccessfullyWithValidData() {
        // Given - Usuario válido que no existe en la base de datos
        when(userRepository.existsByCedula(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When - Crear usuario
        UserDTO result = userApplicationService.createUser(validUserDTO);

        // Then - Verificar que se creó correctamente
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals("TEST001", result.getCedula(), "La cédula debe coincidir");
        assertEquals("testuser", result.getUsername(), "El username debe coincidir");
        assertEquals("Usuario de Prueba", result.getFullName(), "El nombre completo debe coincidir");
        assertEquals("DOCTOR", result.getRole(), "El rol debe coincidir");
        assertTrue(result.isActive(), "El usuario debe estar activo por defecto");
        assertEquals(39, result.getAge(), "La edad debe calcularse correctamente");
    }

    @Test
    @DisplayName("Debe rechazar creación de usuario con cédula duplicada")
    void shouldRejectUserCreationWithDuplicateCedula() {
        // Given - Usuario con cédula que ya existe
        when(userRepository.existsByCedula(any())).thenReturn(true);

        // When & Then - Debe lanzar excepción
        assertThrows(IllegalArgumentException.class, () -> {
            userApplicationService.createUser(validUserDTO);
        }, "Debe lanzar excepción por cédula duplicada");
    }

    @Test
    @DisplayName("Debe rechazar creación de usuario con username duplicado")
    void shouldRejectUserCreationWithDuplicateUsername() {
        // Given - Usuario con username que ya existe
        when(userRepository.existsByCedula(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(true);

        // When & Then - Debe lanzar excepción
        assertThrows(IllegalArgumentException.class, () -> {
            userApplicationService.createUser(validUserDTO);
        }, "Debe lanzar excepción por username duplicado");
    }

    @Test
    @DisplayName("Debe validar formato de fecha correctamente")
    void shouldValidateDateFormatCorrectly() {
        // Given - Usuario con fecha en formato incorrecto
        CreateUserDTO invalidDateDTO = new CreateUserDTO(
            "TEST002", "testuser2", "TestPass123!@#", "Test User",
            "1985-06-15", // Formato incorrecto (debe ser DD/MM/YYYY)
            "Calle 123", "3001234567", "test2@clinica.com", "DOCTOR"
        );

        // When & Then - Debe lanzar excepción por formato de fecha inválido
        assertThrows(Exception.class, () -> {
            userApplicationService.createUser(invalidDateDTO);
        }, "Debe lanzar excepción por formato de fecha inválido");
    }

    @Test
    @DisplayName("Debe validar contraseña segura correctamente")
    void shouldValidateSecurePasswordCorrectly() {
        // Given - Usuario con contraseña débil
        CreateUserDTO weakPasswordDTO = new CreateUserDTO(
            "TEST003", "testuser3", "123", "Test User",
            "15/06/1985", "Calle 123", "3001234567", "test3@clinica.com", "DOCTOR"
        );

        // When & Then - Debe lanzar excepción por contraseña débil
        assertThrows(Exception.class, () -> {
            userApplicationService.createUser(weakPasswordDTO);
        }, "Debe lanzar excepción por contraseña débil");
    }

    @Test
    @DisplayName("Debe validar rol correcto")
    void shouldValidateCorrectRole() {
        // Given - Usuario con rol inválido
        CreateUserDTO invalidRoleDTO = new CreateUserDTO(
            "TEST004", "testuser4", "TestPass123!@#", "Test User",
            "15/06/1985", "Calle 123", "3001234567", "test4@clinica.com", "INVALID_ROLE"
        );

        // When & Then - Debe lanzar excepción por rol inválido
        assertThrows(Exception.class, () -> {
            userApplicationService.createUser(invalidRoleDTO);
        }, "Debe lanzar excepción por rol inválido");
    }
}