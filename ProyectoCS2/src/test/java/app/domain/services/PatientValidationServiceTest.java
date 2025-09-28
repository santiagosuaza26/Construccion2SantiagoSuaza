package app.domain.services;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import app.domain.exception.PatientValidationException;
import app.domain.model.Credentials;
import app.domain.model.EmergencyContact;
import app.domain.model.InsurancePolicy;
import app.domain.model.Patient;
import app.domain.model.Role;
import app.domain.port.PatientRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Pruebas unitarias para PatientValidationService
 * Demuestra el cumplimiento de principios SOLID:
 * - SRP: Una sola responsabilidad (validar pacientes)
 * - OCP: Abierto para extensión, cerrado para modificación
 * - LSP: Las excepciones pueden ser sustituidas por su clase base
 * - ISP: Interfaces específicas para diferentes validaciones
 * - DIP: Depende de abstracciones (PatientRepository interface)
 */
@ExtendWith(MockitoExtension.class)
class PatientValidationServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new PatientValidationService(patientRepository);
    }

    @Nested
    @DisplayName("Validaciones de datos básicos del paciente")
    class BasicPatientValidationTests {

        @Test
        @DisplayName("Debe validar paciente correctamente cuando todos los datos son válidos")
        void shouldValidatePatientSuccessfully() {
            // Given
            Patient validPatient = createValidPatient();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(validPatient));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el paciente es null")
        void shouldThrowExceptionWhenPatientIsNull() {
            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(null)
            );

            assertEquals("Patient cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar ID card requerido")
        void shouldValidateIdCardRequired() {
            // Given
            Patient patientWithoutIdCard = createPatientWithoutIdCard();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithoutIdCard)
            );

            assertEquals("Patient ID card is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar nombre completo requerido")
        void shouldValidateFullNameRequired() {
            // Given
            Patient patientWithoutName = createPatientWithoutName();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithoutName)
            );

            assertEquals("Patient full name is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar fecha de nacimiento requerida")
        void shouldValidateBirthDateRequired() {
            // Given
            Patient patientWithoutBirthDate = createPatientWithoutBirthDate();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithoutBirthDate)
            );

            assertEquals("Patient birth date is required", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de edad")
    class AgeValidationTests {

        @Test
        @DisplayName("Debe rechazar fecha de nacimiento futura")
        void shouldRejectFutureBirthDate() {
            // Given
            Patient patientWithFutureBirthDate = createPatientWithFutureBirthDate();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithFutureBirthDate)
            );

            assertEquals("Birth date cannot be in the future", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar edad mayor a 150 años")
        void shouldRejectAgeOver150() {
            // Given
            Patient patientWithExcessiveAge = createPatientWithExcessiveAge();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithExcessiveAge)
            );

            assertEquals("Patient age cannot exceed 150 years", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de género")
    class GenderValidationTests {

        @Test
        @DisplayName("Debe rechazar género vacío")
        void shouldRejectEmptyGender() {
            // Given
            Patient patientWithEmptyGender = createPatientWithEmptyGender();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithEmptyGender)
            );

            assertEquals("Patient gender is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar género no permitido")
        void shouldRejectInvalidGender() {
            // Given
            Patient patientWithInvalidGender = createPatientWithInvalidGender();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithInvalidGender)
            );

            assertEquals("Gender must be: masculino, femenino, or otro", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar género válido en mayúsculas")
        void shouldAcceptValidGenderInUpperCase() {
            // Given
            Patient patientWithValidGender = createPatientWithValidGenderUpperCase();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(patientWithValidGender));
        }
    }

    @Nested
    @DisplayName("Validaciones de unicidad")
    class UniquenessValidationTests {

        @Test
        @DisplayName("Debe detectar ID card duplicado")
        void shouldDetectDuplicateIdCard() {
            // Given
            String idCard = "12345678";
            String username = "testuser";
            when(patientRepository.existsByIdCard(idCard)).thenReturn(true);

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientUniqueness(idCard, username)
            );

            assertEquals("Patient with ID card " + idCard + " already exists", exception.getMessage());
        }

        @Test
        @DisplayName("Debe detectar username duplicado")
        void shouldDetectDuplicateUsername() {
            // Given
            String idCard = "12345678";
            String username = "testuser";
            when(patientRepository.existsByIdCard(idCard)).thenReturn(false);
            when(patientRepository.existsByUsername(username)).thenReturn(true);

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientUniqueness(idCard, username)
            );

            assertEquals("Username " + username + " already exists", exception.getMessage());
        }

        @Test
        @DisplayName("Debe pasar validación cuando no hay duplicados")
        void shouldPassValidationWhenNoDuplicates() {
            // Given
            String idCard = "12345678";
            String username = "testuser";
            when(patientRepository.existsByIdCard(idCard)).thenReturn(false);
            when(patientRepository.existsByUsername(username)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientUniqueness(idCard, username));
        }
    }

    @Nested
    @DisplayName("Validaciones de acceso y permisos")
    class AccessValidationTests {

        @Test
        @DisplayName("Debe permitir acceso cuando usuario tiene permisos")
        void shouldAllowAccessWhenUserHasPermissions() {
            // Given
            Patient patient = createValidPatient();
            AuthenticatedUser authorizedUser = new AuthenticatedUser(
                "admin001",
                "Administrador",
                Role.ADMINISTRATIVE,
                true
            );

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientAccess(patient, authorizedUser));
        }

        @Test
        @DisplayName("Debe rechazar acceso cuando usuario no tiene permisos")
        void shouldRejectAccessWhenUserHasNoPermissions() {
            // Given
            Patient patient = createValidPatient();
            AuthenticatedUser unauthorizedUser = new AuthenticatedUser(
                "hr001",
                "Recursos Humanos",
                Role.HUMAN_RESOURCES,
                true
            );

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientAccess(patient, unauthorizedUser)
            );

            assertEquals("User does not have permission to access patient data", exception.getMessage());
        }

        @Test
        @DisplayName("Debe permitir acceso de paciente a su propia información")
        void shouldAllowPatientAccessToOwnData() {
            // Given
            Patient patient = createValidPatient();
            AuthenticatedUser patientUser = new AuthenticatedUser(
                patient.getIdCard(),
                patient.getFullName(),
                Role.PATIENT,
                false
            );

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientAccess(patient, patientUser));
        }
    }

    // Métodos auxiliares para crear objetos de prueba

    private Patient createValidPatient() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.of(2025, 12, 31))
        );
    }

    private Patient createPatientWithoutIdCard() {
        return new Patient(
            null, // ID card nulo
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithoutName() {
        return new Patient(
            "12345678",
            null, // Nombre nulo
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithoutBirthDate() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            null, // Fecha de nacimiento nula
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithFutureBirthDate() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.now().plusDays(1), // Fecha futura
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithExcessiveAge() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1800, 1, 1), // Edad > 150 años
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithEmptyGender() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "", // Género vacío
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithInvalidGender() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "invalid_gender", // Género no permitido
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithValidGenderUpperCase() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "MASCULINO", // Género válido en mayúsculas
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "password123"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }
}