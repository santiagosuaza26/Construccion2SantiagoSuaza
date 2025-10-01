package app.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import app.domain.exception.DomainValidationException;
import app.domain.model.Credentials;

/**
 * Pruebas unitarias para CredentialsValidationService
 * Demuestra el cumplimiento de principios SOLID:
 * - SRP: Una sola responsabilidad (validar credenciales)
 */
class CredentialsValidationServiceTest {

    private CredentialsValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new CredentialsValidationService();
    }

    @Nested
    @DisplayName("Validaciones básicas de credenciales")
    class BasicCredentialsValidationTests {

        @Test
        @DisplayName("Debe validar credenciales nulas")
        void shouldThrowExceptionWhenCredentialsIsNull() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForAuthentication(null)
            );

            assertEquals("Credentials cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar username requerido")
        void shouldValidateUsernameRequired() {
            // Given
            Credentials credentialsWithoutUsername = new Credentials(null, "Password123!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForAuthentication(credentialsWithoutUsername)
            );

            assertEquals("Username is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar password requerido")
        void shouldValidatePasswordRequired() {
            // Given
            Credentials credentialsWithoutPassword = new Credentials("testuser", null);

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForAuthentication(credentialsWithoutPassword)
            );

            assertEquals("Password must be at least 8 characters long", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar username no vacío")
        void shouldValidateUsernameNotEmpty() {
            // Given
            Credentials credentialsWithEmptyUsername = new Credentials("", "Password123!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForAuthentication(credentialsWithEmptyUsername)
            );

            assertEquals("Username is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar password mínimo 8 caracteres")
        void shouldValidatePasswordMinimumLength() {
            // Given
            Credentials credentialsWithShortPassword = new Credentials("testuser", "Short1!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForAuthentication(credentialsWithShortPassword)
            );

            assertEquals("Password must be at least 8 characters long", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de formato de username")
    class UsernameFormatValidationTests {

        @Test
        @DisplayName("Debe rechazar username muy largo")
        void shouldRejectUsernameTooLong() {
            // Given
            String longUsername = "a".repeat(16); // Más de 15 caracteres
            Credentials credentialsWithLongUsername = new Credentials(longUsername, "Password123!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(credentialsWithLongUsername)
            );

            assertEquals("Username cannot exceed 15 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar username con caracteres especiales")
        void shouldRejectUsernameWithSpecialCharacters() {
            // Given
            Credentials credentialsWithSpecialChars = new Credentials("test@user", "Password123!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(credentialsWithSpecialChars)
            );

            assertEquals("Username must contain only letters and numbers", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar username válido con letras y números")
        void shouldAcceptValidUsername() {
            // Given
            Credentials validCredentials = new Credentials("testuser123", "Password123!");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(validCredentials));
        }

        @Test
        @DisplayName("Debe aceptar username en límite de longitud")
        void shouldAcceptUsernameAtLengthLimit() {
            // Given
            String usernameAtLimit = "a".repeat(15); // Exactamente 15 caracteres
            Credentials credentialsAtLimit = new Credentials(usernameAtLimit, "Password123!");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(credentialsAtLimit));
        }
    }

    @Nested
    @DisplayName("Validaciones de fortaleza de contraseña")
    class PasswordStrengthValidationTests {

        @Test
        @DisplayName("Debe rechazar contraseña sin mayúscula")
        void shouldRejectPasswordWithoutUppercase() {
            // Given
            Credentials credentialsWithoutUppercase = new Credentials("testuser", "password123!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(credentialsWithoutUppercase)
            );

            assertEquals("Password must contain at least one uppercase letter", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar contraseña sin número")
        void shouldRejectPasswordWithoutNumber() {
            // Given
            Credentials credentialsWithoutNumber = new Credentials("testuser", "Password!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(credentialsWithoutNumber)
            );

            assertEquals("Password must contain at least one number", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar contraseña sin carácter especial")
        void shouldRejectPasswordWithoutSpecialChar() {
            // Given
            Credentials credentialsWithoutSpecial = new Credentials("testuser", "Password123");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(credentialsWithoutSpecial)
            );

            assertEquals("Password must contain at least one special character", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar contraseña con todos los requisitos")
        void shouldAcceptStrongPassword() {
            // Given
            Credentials strongCredentials = new Credentials("testuser", "Password123!");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(strongCredentials));
        }

        @Test
        @DisplayName("Debe aceptar contraseña con múltiples caracteres especiales")
        void shouldAcceptPasswordWithMultipleSpecialChars() {
            // Given
            Credentials credentialsWithMultipleSpecial = new Credentials("testuser", "P@ssw0rd!#$");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(credentialsWithMultipleSpecial));
        }
    }

    @Nested
    @DisplayName("Validaciones de cambio de contraseña")
    class PasswordChangeValidationTests {

        @Test
        @DisplayName("Debe validar contraseña actual requerida")
        void shouldValidateCurrentPasswordRequired() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePasswordChange(null, "NewPassword123!")
            );

            assertEquals("Current password is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar nueva contraseña requerida")
        void shouldValidateNewPasswordRequired() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePasswordChange("CurrentPass123!", null)
            );

            assertEquals("Password must be at least 8 characters long", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar nueva contraseña igual a la actual")
        void shouldRejectSamePassword() {
            // Given
            String currentPassword = "CurrentPassword123!";

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePasswordChange(currentPassword, currentPassword)
            );

            assertEquals("New password must be different from current password", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar cambio de contraseña válido")
        void shouldAcceptValidPasswordChange() {
            // Given
            String currentPassword = "CurrentPassword123!";
            String newPassword = "NewPassword456!";

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePasswordChange(currentPassword, newPassword));
        }
    }

    @Nested
    @DisplayName("Validaciones de confirmación de contraseña")
    class PasswordConfirmationValidationTests {

        @Test
        @DisplayName("Debe rechazar cuando password es null")
        void shouldRejectWhenPasswordIsNull() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePasswordConfirmation(null, "Password123!")
            );

            assertEquals("Both password and confirmation are required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar cuando confirmación es null")
        void shouldRejectWhenConfirmationIsNull() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePasswordConfirmation("Password123!", null)
            );

            assertEquals("Both password and confirmation are required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar cuando passwords no coinciden")
        void shouldRejectWhenPasswordsDoNotMatch() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePasswordConfirmation("Password123!", "DifferentPassword456!")
            );

            assertEquals("Password confirmation does not match", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar cuando passwords coinciden")
        void shouldAcceptWhenPasswordsMatch() {
            // Given
            String password = "Password123!";
            String confirmation = "Password123!";

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePasswordConfirmation(password, confirmation));
        }

        @Test
        @DisplayName("Debe aceptar passwords idénticos con mayúsculas diferentes")
        void shouldAcceptIdenticalPasswords() {
            // Given
            String password = "PASSWORD123!";
            String confirmation = "PASSWORD123!";

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePasswordConfirmation(password, confirmation));
        }
    }

    @Nested
    @DisplayName("Casos extremos y edge cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Debe manejar username con solo números")
        void shouldHandleNumericOnlyUsername() {
            // Given
            Credentials numericCredentials = new Credentials("123456", "Password123!");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(numericCredentials));
        }

        @Test
        @DisplayName("Debe manejar username con solo letras")
        void shouldHandleLetterOnlyUsername() {
            // Given
            Credentials letterCredentials = new Credentials("testuser", "Password123!");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(letterCredentials));
        }

        @Test
        @DisplayName("Debe manejar contraseña con caracteres especiales múltiples")
        void shouldHandlePasswordWithMultipleSpecialChars() {
            // Given
            Credentials complexCredentials = new Credentials("testuser", "P@ssw0rd!#$%^&*()");

            // When & Then
            assertDoesNotThrow(() -> validationService.validateCredentialsForRegistration(complexCredentials));
        }

        @Test
        @DisplayName("Debe rechazar username con espacios")
        void shouldRejectUsernameWithSpaces() {
            // Given
            Credentials credentialsWithSpaces = new Credentials("test user", "Password123!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(credentialsWithSpaces)
            );

            assertEquals("Username must contain only letters and numbers", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar contraseña muy corta incluso con caracteres especiales")
        void shouldRejectVeryShortPassword() {
            // Given
            Credentials shortPasswordCredentials = new Credentials("testuser", "P@1!");

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateCredentialsForRegistration(shortPasswordCredentials)
            );

            assertEquals("Password must be at least 8 characters long", exception.getMessage());
        }
    }
}