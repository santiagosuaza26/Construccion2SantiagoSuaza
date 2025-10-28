package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhoneValidationServiceTest {

    private PhoneValidationService phoneValidationService;

    @BeforeEach
    void setUp() {
        phoneValidationService = new PhoneValidationService();
    }

    @Test
    void shouldValidateCorrectPhone() {
        // Given
        String phone = "1234567890";

        // When & Then
        assertDoesNotThrow(() -> phoneValidationService.validatePhone(phone));
    }

    @Test
    void shouldThrowExceptionForNullPhone() {
        // Given
        String phone = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> phoneValidationService.validatePhone(phone));
    }

    @Test
    void shouldThrowExceptionForEmptyPhone() {
        // Given
        String phone = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> phoneValidationService.validatePhone(phone));
    }

    @Test
    void shouldThrowExceptionForInvalidPhoneFormat() {
        // Given
        String phone = "12345678901"; // 11 digits

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> phoneValidationService.validatePhone(phone));
    }

    @Test
    void shouldValidateSingleDigitPhone() {
        // Given
        String phone = "1";

        // When & Then
        assertDoesNotThrow(() -> phoneValidationService.validatePhone(phone));
    }

    @Test
    void shouldValidateTenDigitPhone() {
        // Given
        String phone = "1234567890";

        // When & Then
        assertDoesNotThrow(() -> phoneValidationService.validatePhone(phone));
    }
}