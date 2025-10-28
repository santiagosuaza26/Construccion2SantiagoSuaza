package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidationServiceTest {

    private EmailValidationService emailValidationService;

    @BeforeEach
    void setUp() {
        emailValidationService = new EmailValidationService();
    }

    @Test
    void shouldValidateCorrectEmail() {
        // Given
        String email = "test@example.com";

        // When & Then
        assertDoesNotThrow(() -> emailValidationService.validateEmail(email));
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        // Given
        String email = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> emailValidationService.validateEmail(email));
    }

    @Test
    void shouldThrowExceptionForEmptyEmail() {
        // Given
        String email = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> emailValidationService.validateEmail(email));
    }

    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        // Given
        String email = "invalid-email";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> emailValidationService.validateEmail(email));
    }
}