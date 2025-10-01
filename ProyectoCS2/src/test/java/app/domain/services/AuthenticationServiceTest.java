package app.domain.services;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import app.domain.model.Credentials;
import app.domain.model.EmergencyContact;
import app.domain.model.Patient;
import app.domain.model.Role;
import app.domain.model.User;
import app.domain.port.PatientRepository;
import app.domain.port.UserRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Pruebas unitarias para AuthenticationService
 * Demuestra principios SOLID:
 * - SRP: Una sola responsabilidad (autenticación)
 * - DIP: Depende de abstracciones (repositorios)
 * - LSP: Las excepciones pueden ser sustituidas por su clase base
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, patientRepository);
    }

    @Nested
    @DisplayName("Autenticación de usuarios del sistema")
    class UserAuthenticationTests {

        @Test
        @DisplayName("Debe autenticar usuario válido correctamente")
        void shouldAuthenticateValidUserSuccessfully() {
            // Given
            String username = "testuser";
            String password = "Password123!";
            User user = createValidUser();
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            // When
            AuthenticatedUser result = authenticationService.authenticate(username, password);

            // Then
            assertNotNull(result);
            assertEquals(user.getIdCard(), result.getIdCard());
            assertEquals(user.getFullName(), result.getFullName());
            assertEquals(user.getRole(), result.getRole());
            assertTrue(result.isStaff());
        }

        @Test
        @DisplayName("Debe autenticar paciente válido correctamente")
        void shouldAuthenticateValidPatientSuccessfully() {
            // Given
            String username = "patientuser";
            String password = "Password123!";
            Patient patient = createValidPatient();
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
            when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));

            // When
            AuthenticatedUser result = authenticationService.authenticate(username, password);

            // Then
            assertNotNull(result);
            assertEquals(patient.getIdCard(), result.getIdCard());
            assertEquals(patient.getFullName(), result.getFullName());
            assertEquals(Role.PATIENT, result.getRole());
            assertFalse(result.isStaff());
        }

        @Test
        @DisplayName("Debe rechazar credenciales inválidas")
        void shouldRejectInvalidCredentials() {
            // Given
            String username = "testuser";
            String password = "wrongpassword";
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
            when(patientRepository.findByUsername(username)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(SecurityException.class,
                () -> authenticationService.authenticate(username, password));
        }

        @Test
        @DisplayName("Debe rechazar contraseña incorrecta")
        void shouldRejectIncorrectPassword() {
            // Given
            String username = "testuser";
            String password = "wrongpassword";
            User user = createValidUser();
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            // When & Then
            assertThrows(SecurityException.class,
                () -> authenticationService.authenticate(username, password));
        }
    }

    @Nested
    @DisplayName("Validaciones de permisos")
    class PermissionValidationTests {

        @Test
        @DisplayName("Recursos Humanos debe poder crear usuarios")
        void hrShouldBeAbleToCreateUsers() {
            // Given
            AuthenticatedUser hrUser = new AuthenticatedUser(
                "hr001", "HR User", Role.HUMAN_RESOURCES, true
            );

            // When & Then
            assertTrue(hrUser.canCreateUsers());
            assertFalse(hrUser.canRegisterPatients());
            assertFalse(hrUser.canAccessPatientData());
        }

        @Test
        @DisplayName("Administrativo debe poder registrar pacientes")
        void adminShouldBeAbleToRegisterPatients() {
            // Given
            AuthenticatedUser adminUser = new AuthenticatedUser(
                "admin001", "Admin User", Role.ADMINISTRATIVE, true
            );

            // When & Then
            assertTrue(adminUser.canRegisterPatients());
            assertTrue(adminUser.canAccessPatientData());
            assertTrue(adminUser.canGenerateInvoices());
        }

        @Test
        @DisplayName("Paciente solo debe acceder a su propia información")
        void patientShouldOnlyAccessOwnData() {
            // Given
            AuthenticatedUser patientUser = new AuthenticatedUser(
                "patient001", "Patient User", Role.PATIENT, false
            );

            // When & Then
            assertTrue(patientUser.canAccessOwnPatientData("patient001"));
            assertFalse(patientUser.canAccessOwnPatientData("other_patient"));
            assertFalse(patientUser.canRegisterPatients());
        }

        @Test
        @DisplayName("Médico debe poder crear órdenes")
        void doctorShouldBeAbleToCreateOrders() {
            // Given
            AuthenticatedUser doctorUser = new AuthenticatedUser(
                "doc001", "Doctor User", Role.DOCTOR, true
            );

            // When & Then
            assertTrue(doctorUser.canCreateOrders());
            assertTrue(doctorUser.canAccessClinicalHistory());
            assertTrue(doctorUser.canRecordVitalSigns());
        }
    }

    // Métodos auxiliares para crear objetos de prueba

    private User createValidUser() {
        return new User(
            "Test User",
            "12345678",
            "test@example.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("testuser", "Password123!")
        );
    }

    private Patient createValidPatient() {
        return new Patient(
            "87654321",
            "Patient User",
            LocalDate.of(1985, 3, 10),
            "femenino",
            "Calle 456 #78-90",
            "0987654321",
            "patient@example.com",
            new Credentials("patientuser", "Password123!"),
            new EmergencyContact("Contacto", "Emergencia", "Familiar", "0987654321"),
            null  // Insurance policy no requerido para este test
        );
    }
}