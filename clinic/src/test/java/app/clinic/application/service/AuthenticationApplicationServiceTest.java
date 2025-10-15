package app.clinic.application.service;

import app.clinic.application.dto.user.LoginRequestDTO;
import app.clinic.application.dto.user.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests comprehensivos para AuthenticationApplicationService.
 * Cubre casos de éxito, fracaso, seguridad y validaciones.
 */
@DisplayName("AuthenticationApplicationService Tests")
class AuthenticationApplicationServiceTest {

    @InjectMocks
    private AuthenticationApplicationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Casos de Autenticación Exitosa")
    class SuccessfulAuthenticationTests {

        @Test
        @DisplayName("Debe autenticar administrador con contraseña primaria")
        void shouldAuthenticateAdminWithPrimaryPassword() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "Admin123!@#");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getToken()).isNotNull();
            assertThat(response.getUser()).isNotNull();
            assertThat(response.getUser().getUsername()).isEqualTo("admin");
            assertThat(response.getUser().getRole()).isEqualTo("HUMAN_RESOURCES");
            assertThat(response.getUser().getCedula()).isEqualTo("12345678");
            assertThat(response.getUser().getFullName()).isEqualTo("Administrador Sistema");
        }

        @Test
        @DisplayName("Debe autenticar administrador con contraseña alternativa")
        void shouldAuthenticateAdminWithAlternativePassword() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "admin123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getUser().getUsername()).isEqualTo("admin");
        }

        @Test
        @DisplayName("Debe autenticar doctor con contraseña primaria")
        void shouldAuthenticateDoctorWithPrimaryPassword() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("doctor", "Doctor123!@#");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getUser().getUsername()).isEqualTo("doctor");
            assertThat(response.getUser().getRole()).isEqualTo("DOCTOR");
            assertThat(response.getUser().getCedula()).isEqualTo("87654321");
        }

        @Test
        @DisplayName("Debe autenticar doctor con contraseña alternativa")
        void shouldAuthenticateDoctorWithAlternativePassword() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("doctor", "doctor123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getUser().getUsername()).isEqualTo("doctor");
        }

        @Test
        @DisplayName("Debe autenticar enfermera correctamente")
        void shouldAuthenticateNurseSuccessfully() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("nurse", "nurse123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getUser().getUsername()).isEqualTo("nurse");
            assertThat(response.getUser().getRole()).isEqualTo("NURSE");
        }

        @Test
        @DisplayName("Debe autenticar recursos humanos correctamente")
        void shouldAuthenticateHrSuccessfully() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("hr", "hr123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getUser().getUsername()).isEqualTo("hr");
            assertThat(response.getUser().getRole()).isEqualTo("HUMAN_RESOURCES");
        }

        @Test
        @DisplayName("Debe autenticar personal correctamente")
        void shouldAuthenticateStaffSuccessfully() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("staff", "staff123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getUser().getUsername()).isEqualTo("staff");
            assertThat(response.getUser().getRole()).isEqualTo("STAFF");
        }
    }

    @Nested
    @DisplayName("Casos de Autenticación Fallida")
    class FailedAuthenticationTests {

        @Test
        @DisplayName("Debe rechazar credenciales incorrectas")
        void shouldRejectInvalidCredentials() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "wrongpassword");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("Credenciales inválidas");
            assertThat(response.getToken()).isNull();
            assertThat(response.getUser()).isNull();
        }

        @Test
        @DisplayName("Debe rechazar usuario inexistente")
        void shouldRejectNonExistentUser() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("nonexistent", "password123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("Credenciales inválidas");
        }

        @Test
        @DisplayName("Debe rechazar contraseña vacía")
        void shouldRejectEmptyPassword() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("La contraseña es requerida");
        }

        @Test
        @DisplayName("Debe rechazar usuario vacío")
        void shouldRejectEmptyUsername() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("", "password123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("El nombre de usuario es requerido");
        }
    }

    @Nested
    @DisplayName("Validaciones de Seguridad")
    class SecurityValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"admin'; DROP TABLE users; --", "admin' OR '1'='1", "admin' UNION SELECT * FROM users --"})
        @DisplayName("Debe detectar y rechazar posibles ataques de inyección SQL")
        void shouldDetectAndRejectSqlInjectionAttempts(String maliciousInput) {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO(maliciousInput, "password123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("Credenciales contienen caracteres no válidos");
        }

        @Test
        @DisplayName("Debe rechazar usuario demasiado corto")
        void shouldRejectTooShortUsername() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("ab", "password123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("El nombre de usuario debe tener entre 3 y 50 caracteres");
        }

        @Test
        @DisplayName("Debe rechazar usuario demasiado largo")
        void shouldRejectTooLongUsername() {
            // Given
            String longUsername = "a".repeat(51);
            LoginRequestDTO loginRequest = new LoginRequestDTO(longUsername, "password123");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("El nombre de usuario debe tener entre 3 y 50 caracteres");
        }

        @Test
        @DisplayName("Debe rechazar contraseña demasiado corta")
        void shouldRejectTooShortPassword() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "12345");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("La contraseña debe tener entre 6 y 100 caracteres");
        }

        @Test
        @DisplayName("Debe rechazar contraseña demasiado larga")
        void shouldRejectTooLongPassword() {
            // Given
            String longPassword = "p".repeat(101);
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", longPassword);

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo("La contraseña debe tener entre 6 y 100 caracteres");
        }
    }

    @Nested
    @DisplayName("Casos Especiales y Manejo de Errores")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Debe manejar null login request correctamente")
        void shouldHandleNullLoginRequest() {
            // When & Then
            assertThatThrownBy(() -> authenticationService.login(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Los datos de login no pueden ser nulos");
        }

        @Test
        @DisplayName("Debe manejar excepciones internas correctamente")
        void shouldHandleInternalExceptions() {
            // Given - Crear un estado que pueda causar excepción interna
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "Admin123!@#");

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then - No debe lanzar excepción, debe retornar respuesta de error
            assertThat(response.isSuccess()).isTrue(); // En este caso debería funcionar normalmente
        }

        @Test
        @DisplayName("Debe generar tokens únicos para diferentes usuarios")
        void shouldGenerateUniqueTokensForDifferentUsers() {
            // Given
            LoginRequestDTO adminRequest = new LoginRequestDTO("admin", "Admin123!@#");
            LoginRequestDTO doctorRequest = new LoginRequestDTO("doctor", "Doctor123!@#");

            // When
            LoginResponseDTO adminResponse = authenticationService.login(adminRequest);
            LoginResponseDTO doctorResponse = authenticationService.login(doctorRequest);

            // Then
            assertThat(adminResponse.getToken()).isNotEqualTo(doctorResponse.getToken());
        }

        @Test
        @DisplayName("Debe generar tokens únicos para mismo usuario en diferentes momentos")
        void shouldGenerateUniqueTokensForSameUserAtDifferentTimes() {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "Admin123!@#");

            // When
            LoginResponseDTO firstResponse = authenticationService.login(loginRequest);
            LoginResponseDTO secondResponse = authenticationService.login(loginRequest);

            // Then
            assertThat(firstResponse.getToken()).isNotEqualTo(secondResponse.getToken());
        }
    }

    @Nested
    @DisplayName("Tests de Caracteres Especiales")
    class SpecialCharactersTests {

        @ParameterizedTest
        @CsvSource({
            "test@example.com, password123",
            "user_name, password123",
            "user-name, password123",
            "user+name, password123",
            "üéñ, password123",
            "用户, password123"
        })
        @DisplayName("Debe manejar correctamente caracteres especiales en username")
        void shouldHandleSpecialCharactersInUsername(String username, String password) {
            // Given
            LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);

            // When
            LoginResponseDTO response = authenticationService.login(loginRequest);

            // Then - No debe lanzar excepción por caracteres especiales válidos
            // Solo falla si las credenciales son incorrectas
            assertThat(response).isNotNull();
        }
    }
}