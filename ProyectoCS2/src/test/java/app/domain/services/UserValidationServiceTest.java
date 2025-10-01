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

import app.domain.exception.DomainValidationException;
import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;
import app.domain.port.UserRepository;

/**
 * Pruebas unitarias para UserValidationService
 * Demuestra el cumplimiento de principios SOLID:
 * - SRP: Una sola responsabilidad (validar usuarios)
 * - DIP: Depende de abstracciones (UserRepository interface)
 */
@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new UserValidationService(userRepository);
    }

    @Nested
    @DisplayName("Validaciones básicas de usuario")
    class BasicUserValidationTests {

        @Test
        @DisplayName("Debe validar usuario correctamente cuando todos los datos son válidos")
        void shouldValidateUserSuccessfully() {
            // Given
            User validUser = createValidUser();

            // When & Then
            assertDoesNotThrow(() -> validationService.validateUserForRegistration(validUser));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el usuario es null")
        void shouldThrowExceptionWhenUserIsNull() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(null)
            );

            assertEquals("User cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar ID card requerido")
        void shouldValidateIdCardRequired() {
            // Given
            User userWithoutIdCard = createUserWithoutIdCard();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithoutIdCard)
            );

            assertEquals("User ID card is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar ID card con formato numérico")
        void shouldValidateIdCardNumericFormat() {
            // Given
            User userWithInvalidIdCard = createUserWithInvalidIdCard();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithInvalidIdCard)
            );

            assertEquals("User ID card must contain only numbers", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar nombre completo requerido")
        void shouldValidateFullNameRequired() {
            // Given
            User userWithoutName = createUserWithoutName();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithoutName)
            );

            assertEquals("User full name is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar fecha de nacimiento requerida")
        void shouldValidateBirthDateRequired() {
            // Given
            User userWithoutBirthDate = createUserWithoutBirthDate();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithoutBirthDate)
            );

            assertEquals("User birth date is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar credenciales requeridas")
        void shouldValidateCredentialsRequired() {
            // Given
            User userWithoutCredentials = createUserWithoutCredentials();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithoutCredentials)
            );

            assertEquals("User credentials are required", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de edad")
    class AgeValidationTests {

        @Test
        @DisplayName("Debe rechazar fecha de nacimiento futura")
        void shouldRejectFutureBirthDate() {
            // Given
            User userWithFutureBirthDate = createUserWithFutureBirthDate();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithFutureBirthDate)
            );

            assertEquals("Birth date cannot be in the future", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar edad mayor a 150 años")
        void shouldRejectAgeOver150() {
            // Given
            User userWithExcessiveAge = createUserWithExcessiveAge();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithExcessiveAge)
            );

            assertEquals("User age cannot exceed 150 years", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar edad menor a 18 años")
        void shouldRejectAgeUnder18() {
            // Given
            User userUnderAge = createUserUnderAge();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userUnderAge)
            );

            assertEquals("User must be at least 18 years old", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de formato de datos")
    class FormatValidationTests {

        @Test
        @DisplayName("Debe rechazar email con formato inválido")
        void shouldRejectInvalidEmailFormat() {
            // Given
            User userWithInvalidEmail = createUserWithInvalidEmail();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithInvalidEmail)
            );

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar teléfono con formato inválido")
        void shouldRejectInvalidPhoneFormat() {
            // Given
            User userWithInvalidPhone = createUserWithInvalidPhone();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithInvalidPhone)
            );

            assertEquals("User phone must contain between 1 and 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar dirección muy larga")
        void shouldRejectAddressTooLong() {
            // Given
            User userWithLongAddress = createUserWithLongAddress();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithLongAddress)
            );

            assertEquals("User address must be maximum 30 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar email válido")
        void shouldAcceptValidEmail() {
            // Given
            User userWithValidEmail = createUserWithValidEmail();

            // When & Then
            assertDoesNotThrow(() -> validationService.validateUserForRegistration(userWithValidEmail));
        }

        @Test
        @DisplayName("Debe aceptar teléfono válido")
        void shouldAcceptValidPhone() {
            // Given
            User userWithValidPhone = createUserWithValidPhone();

            // When & Then
            assertDoesNotThrow(() -> validationService.validateUserForRegistration(userWithValidPhone));
        }
    }

    @Nested
    @DisplayName("Validaciones de rol")
    class RoleValidationTests {

        @Test
        @DisplayName("Debe rechazar rol nulo")
        void shouldRejectNullRole() {
            // Given
            User userWithNullRole = createUserWithNullRole();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithNullRole)
            );

            assertEquals("User role is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar rol de paciente en usuario")
        void shouldRejectPatientRoleInUser() {
            // Given
            User userWithPatientRole = createUserWithPatientRole();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForRegistration(userWithPatientRole)
            );

            assertEquals("Patients should use Patient entity, not User entity", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar roles válidos del sistema")
        void shouldAcceptValidSystemRoles() {
            // Given
            User userWithValidRole = createUserWithValidRole();

            // When & Then
            assertDoesNotThrow(() -> validationService.validateUserForRegistration(userWithValidRole));
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
            when(userRepository.existsByIdCard(idCard)).thenReturn(true);

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserUniqueness(idCard, username)
            );

            assertEquals("User with ID card " + idCard + " already exists", exception.getMessage());
        }

        @Test
        @DisplayName("Debe detectar username duplicado")
        void shouldDetectDuplicateUsername() {
            // Given
            String idCard = "12345678";
            String username = "testuser";
            when(userRepository.existsByIdCard(idCard)).thenReturn(false);
            when(userRepository.existsByUsername(username)).thenReturn(true);

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserUniqueness(idCard, username)
            );

            assertEquals("Username " + username + " already exists", exception.getMessage());
        }

        @Test
        @DisplayName("Debe pasar validación cuando no hay duplicados")
        void shouldPassValidationWhenNoDuplicates() {
            // Given
            String idCard = "12345678";
            String username = "testuser";
            when(userRepository.existsByIdCard(idCard)).thenReturn(false);
            when(userRepository.existsByUsername(username)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> validationService.validateUserUniqueness(idCard, username));
        }
    }

    @Nested
    @DisplayName("Validaciones de actualización")
    class UpdateValidationTests {

        @Test
        @DisplayName("Debe validar usuario para actualización correctamente")
        void shouldValidateUserForUpdateSuccessfully() {
            // Given
            User existingUser = createValidUser();
            User updatedUser = createValidUpdatedUser();

            // When & Then
            assertDoesNotThrow(() -> validationService.validateUserForUpdate(existingUser, updatedUser));
        }

        @Test
        @DisplayName("Debe rechazar cambio de ID card durante actualización")
        void shouldRejectIdCardChangeDuringUpdate() {
            // Given
            User existingUser = createValidUser();
            User updatedUser = createUserWithDifferentIdCard();

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateUserForUpdate(existingUser, updatedUser)
            );

            assertEquals("Cannot change user ID card during update", exception.getMessage());
        }
    }

    // Métodos auxiliares para crear objetos de prueba

    private User createValidUser() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithoutIdCard() {
        return new User(
            "Juan Pérez García",
            null, // ID card nulo
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithInvalidIdCard() {
        return new User(
            "Juan Pérez García",
            "12345678A", // ID card con letra
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithoutName() {
        return new User(
            null, // Nombre nulo
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithoutBirthDate() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            null, // Fecha de nacimiento nula
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithoutCredentials() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            null // Credenciales nulas
        );
    }

    private User createUserWithFutureBirthDate() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.now().plusDays(1), // Fecha futura
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithExcessiveAge() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1800, 1, 1), // Edad > 150 años
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserUnderAge() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.now().minusYears(15), // Menor de 18 años
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithInvalidEmail() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@invalid", // Email inválido
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithInvalidPhone() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "12345678901", // Teléfono muy largo
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithLongAddress() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Esta es una dirección muy larga que excede los 30 caracteres permitidos", // Dirección muy larga
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithValidEmail() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "test.email+tag@example-domain.co.uk", // Email válido complejo
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithValidPhone() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "9876543210", // Teléfono válido diferente
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithNullRole() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            null, // Rol nulo
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithPatientRole() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.PATIENT, // Rol de paciente
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithValidRole() {
        return new User(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.DOCTOR, // Rol válido del sistema
            new Credentials("jperez", "Password123!")
        );
    }

    private User createValidUpdatedUser() {
        return new User(
            "Juan Pérez García López", // Nombre ligeramente diferente
            "12345678", // Mismo ID card
            "juan.perez@gmail.com", // Email diferente
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67 Apto 101", // Dirección ligeramente diferente
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }

    private User createUserWithDifferentIdCard() {
        return new User(
            "Juan Pérez García",
            "87654321", // ID card diferente
            "juan.perez@email.com",
            "1234567890",
            LocalDate.of(1990, 5, 15),
            "Calle 123 #45-67",
            Role.ADMINISTRATIVE,
            new Credentials("jperez", "Password123!")
        );
    }
}