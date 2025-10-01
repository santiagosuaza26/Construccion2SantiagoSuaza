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

        private PatientValidationService validationService;

        public BasicPatientValidationTests() {
            this.validationService = new PatientValidationService(patientRepository);
        }

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
    @DisplayName("Validaciones de formato de datos")
    class FormatValidationTests {

        @Test
        @DisplayName("Debe rechazar teléfono con formato inválido")
        void shouldRejectInvalidPhoneFormat() {
            // Given
            Patient patientWithInvalidPhone = createPatientWithInvalidPhone();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithInvalidPhone)
            );

            assertEquals("Patient phone must have exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar email con formato inválido")
        void shouldRejectInvalidEmailFormat() {
            // Given
            Patient patientWithInvalidEmail = createPatientWithInvalidEmail();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithInvalidEmail)
            );

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar dirección muy larga")
        void shouldRejectAddressTooLong() {
            // Given
            Patient patientWithLongAddress = createPatientWithLongAddress();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithLongAddress)
            );

            assertEquals("Patient address must be maximum 30 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar email válido")
        void shouldAcceptValidEmail() {
            // Given
            Patient patientWithValidEmail = createPatientWithValidEmail();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(patientWithValidEmail));
        }

        @Test
        @DisplayName("Debe aceptar teléfono válido")
        void shouldAcceptValidPhone() {
            // Given
            Patient patientWithValidPhone = createPatientWithValidPhone();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(patientWithValidPhone));
        }
    }

    @Nested
    @DisplayName("Validaciones de actualización")
    class UpdateValidationTests {

        @Test
        @DisplayName("Debe validar paciente para actualización correctamente")
        void shouldValidatePatientForUpdateSuccessfully() {
            // Given
            Patient existingPatient = createValidPatient();
            Patient updatedPatient = createValidUpdatedPatient();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForUpdate(existingPatient, updatedPatient));
        }

        @Test
        @DisplayName("Debe rechazar cambio de ID card durante actualización")
        void shouldRejectIdCardChangeDuringUpdate() {
            // Given
            Patient existingPatient = createValidPatient();
            Patient updatedPatient = createPatientWithDifferentIdCard();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForUpdate(existingPatient, updatedPatient)
            );

            assertEquals("Cannot change patient ID card during update", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar unicidad para actualización correctamente")
        void shouldValidateUniquenessForUpdateSuccessfully() {
            // Given
            String currentIdCard = "12345678";
            String newIdCard = "87654321";
            String username = "newuser";
            when(patientRepository.existsByIdCard(newIdCard)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientUniquenessForUpdate(currentIdCard, newIdCard, username));
        }

        @Test
        @DisplayName("Debe rechazar ID card duplicado durante actualización")
        void shouldRejectDuplicateIdCardDuringUpdate() {
            // Given
            String currentIdCard = "12345678";
            String newIdCard = "87654321";
            String username = "newuser";
            when(patientRepository.existsByIdCard(newIdCard)).thenReturn(true);

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientUniquenessForUpdate(currentIdCard, newIdCard, username)
            );

            assertEquals("Patient with ID card " + newIdCard + " already exists", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de eliminación")
    class RemovalValidationTests {

        @Test
        @DisplayName("Debe permitir eliminación cuando usuario tiene permisos")
        void shouldAllowRemovalWhenUserHasPermissions() {
            // Given
            Patient patient = createValidPatient();
            AuthenticatedUser authorizedUser = new AuthenticatedUser(
                "admin001",
                "Administrador",
                Role.ADMINISTRATIVE,
                true
            );

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientCanBeRemoved(patient, authorizedUser));
        }

        @Test
        @DisplayName("Debe rechazar eliminación cuando usuario no tiene permisos")
        void shouldRejectRemovalWhenUserHasNoPermissions() {
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
                () -> validationService.validatePatientCanBeRemoved(patient, unauthorizedUser)
            );

            assertEquals("User hr001 is not authorized to remove patients", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Casos extremos y edge cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Debe manejar edad mínima válida (recién nacido)")
        void shouldHandleMinimumValidAge() {
            // Given
            Patient newbornPatient = createNewbornPatient();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(newbornPatient));
        }

        @Test
        @DisplayName("Debe manejar edad máxima válida (150 años)")
        void shouldHandleMaximumValidAge() {
            // Given
            Patient elderlyPatient = createElderlyPatient();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(elderlyPatient));
        }

        @Test
        @DisplayName("Debe manejar dirección null")
        void shouldHandleNullAddress() {
            // Given
            Patient patientWithNullAddress = createPatientWithNullAddress();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(patientWithNullAddress));
        }

        @Test
        @DisplayName("Debe manejar email con caracteres especiales")
        void shouldHandleEmailWithSpecialCharacters() {
            // Given
            Patient patientWithSpecialEmail = createPatientWithSpecialEmail();

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePatientForRegistration(patientWithSpecialEmail));
        }

        @Test
        @DisplayName("Debe rechazar teléfono con letras")
        void shouldRejectPhoneWithLetters() {
            // Given
            Patient patientWithInvalidPhone = createPatientWithPhoneWithLetters();

            // When & Then
            PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> validationService.validatePatientForRegistration(patientWithInvalidPhone)
            );

            assertEquals("Patient phone must have exactly 10 digits", exception.getMessage());
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

            assertEquals("User hr001 is not authorized to access patient data", exception.getMessage());
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
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.of(2025, 12, 31))
        );
    }

    private Patient createPatientWithoutIdCard() {
        return new Patient(
            null, // ID card nulo para probar validación de requerido
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "Password123!"),
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
            new Credentials("jperez", "Password123!"),
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
            new Credentials("jperez", "Password123!"),
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
            new Credentials("jperez", "Password123!"),
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
            new Credentials("jperez", "Password123!"),
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
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithInvalidGender() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "desconocido", // Género no permitido (no es masculino, femenino, ni otro)
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "Password123!"),
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
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    // Métodos auxiliares para validaciones de formato

    private Patient createPatientWithInvalidPhone() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "123456789", // Teléfono inválido (9 dígitos)
            "juan.perez@email.com",
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithInvalidEmail() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@invalid", // Email inválido (sin dominio completo)
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithLongAddress() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Esta es una dirección muy larga que excede los 30 caracteres permitidos", // Dirección muy larga
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithValidEmail() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "test.email+tag@example-domain.co.uk", // Email válido complejo
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithValidPhone() {
        return new Patient(
            "12345678",
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "9876543210", // Teléfono válido diferente
            "juan.perez@email.com",
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    // Métodos auxiliares para validaciones de actualización

    private Patient createValidUpdatedPatient() {
        return new Patient(
            "12345678", // Mismo ID card
            "Juan Pérez García López", // Nombre ligeramente diferente
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67 Apto 101", // Dirección ligeramente diferente
            "1234567890",
            "juan.perez@gmail.com", // Email diferente
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    private Patient createPatientWithDifferentIdCard() {
        return new Patient(
            "87654321", // ID card diferente
            "Juan Pérez García",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "juan.perez@email.com",
            new Credentials("jperez", "Password123!"),
            new EmergencyContact("María", "Pérez", "Hermana", "0987654321"),
            null
        );
    }

    // Métodos auxiliares para casos extremos

    private Patient createNewbornPatient() {
        return new Patient(
            "12345678",
            "Bebé Recién Nacido",
            LocalDate.now().minusDays(1), // Nacido ayer
            "otro",
            "Hospital Clínica CS2",
            "1234567890",
            "bebe@test.com",
            new Credentials("bebe123", "Password123!"),
            new EmergencyContact("María", "Madre", "Madre", "0987654321"),
            null
        );
    }

    private Patient createElderlyPatient() {
        return new Patient(
            "12345678",
            "Paciente Centenario",
            LocalDate.now().minusYears(150), // Exactamente 150 años
            "femenino",
            "Calle 123 #45-67",
            "1234567890",
            "centenario@test.com",
            new Credentials("centenario", "Password123!"),
            new EmergencyContact("Familia", "Contacto", "Familiar", "0987654321"),
            null
        );
    }

    private Patient createPatientWithNullAddress() {
        return new Patient(
            "12345678",
            "Paciente Sin Dirección",
            LocalDate.of(1990, 5, 15),
            "masculino",
            null, // Dirección null
            "1234567890",
            "sindireccion@test.com",
            new Credentials("sindir", "Password123!"),
            new EmergencyContact("Contacto", "Emergencia", "Familiar", "0987654321"),
            null
        );
    }

    private Patient createPatientWithSpecialEmail() {
        return new Patient(
            "12345678",
            "Paciente Email Especial",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "1234567890",
            "test_email+special@domain-test.co.uk", // Email con caracteres especiales
            new Credentials("special", "Password123!"),
            new EmergencyContact("Contacto", "Especial", "Familiar", "0987654321"),
            null
        );
    }

    private Patient createPatientWithPhoneWithLetters() {
        return new Patient(
            "12345678",
            "Paciente Teléfono Inválido",
            LocalDate.of(1990, 5, 15),
            "masculino",
            "Calle 123 #45-67",
            "123456789A", // Teléfono con letra
            "telefono_invalido@test.com",
            new Credentials("telefono", "Password123!"),
            new EmergencyContact("Contacto", "Teléfono", "Familiar", "0987654321"),
            null
        );
    }
}