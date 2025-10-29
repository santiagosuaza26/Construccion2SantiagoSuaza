package app.clinic.domain.model.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    void constructor_WithValidPassword_ShouldCreatePassword() {
        // Arrange
        String validPassword = "ValidPass123!";

        // Act
        Password password = new Password(validPassword);

        // Assert
        assertNotNull(password);
        assertTrue(password.matches(validPassword));
    }

    @Test
    void constructor_WithNullPassword_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password(null)
        );
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyPassword_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("")
        );
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithWhitespaceOnlyPassword_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("   ")
        );
        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithTooShortPassword_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("Short1!")
        );
        assertTrue(exception.getMessage().contains("must be at least 8 characters"));
    }

    @Test
    void constructor_WithNoUppercase_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("lowercase123!")
        );
        assertTrue(exception.getMessage().contains("must be at least 8 characters"));
    }

    @Test
    void constructor_WithNoLowercase_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("UPPERCASE123!")
        );
        assertTrue(exception.getMessage().contains("must be at least 8 characters"));
    }

    @Test
    void constructor_WithNoDigit_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("NoDigits!")
        );
        assertTrue(exception.getMessage().contains("must be at least 8 characters"));
    }

    @Test
    void constructor_WithNoSpecialCharacter_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password("NoSpecial123")
        );
        assertTrue(exception.getMessage().contains("must be at least 8 characters"));
    }

    @Test
    void matches_WithCorrectPassword_ShouldReturnTrue() {
        // Arrange
        String plainPassword = "TestPass123!";
        Password password = new Password(plainPassword);

        // Act
        boolean result = password.matches(plainPassword);

        // Assert
        assertTrue(result);
    }

    @Test
    void matches_WithIncorrectPassword_ShouldReturnFalse() {
        // Arrange
        Password password = new Password("TestPass123!");

        // Act
        boolean result = password.matches("WrongPass123!");

        // Assert
        assertFalse(result);
    }

    @Test
    void matches_WithNullPassword_ShouldThrowException() {
        // Arrange
        Password password = new Password("TestPass123!");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            password.matches(null);
        });
    }

    @Test
    void constructor_WithHashedPassword_ShouldCreatePassword() {
        // Arrange
        String plainPassword = "TestPass123!";
        Password originalPassword = new Password(plainPassword);
        String hashedValue = originalPassword.getValue();

        // Act
        Password password = new Password(hashedValue, true);

        // Assert
        assertNotNull(password);
        assertEquals(hashedValue, password.getValue());
        assertTrue(password.matches(plainPassword));
    }

    @Test
    void constructor_WithHashedPasswordAndIsHashedFalse_ShouldThrowException() {
        // Arrange
        String hashedValue = "$2a$10$someHashedValue";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Password(hashedValue, false)
        );
        assertEquals("Use the plain password constructor", exception.getMessage());
    }

    @Test
    void equals_WithSamePassword_ShouldReturnTrue() {
        // Arrange
        String plainPassword = "TestPass123!";
        Password password1 = new Password(plainPassword);
        Password password2 = new Password(plainPassword);

        // Act
        boolean result = password1.equals(password2);

        // Assert
        // Note: Two different Password objects with same plain text will have different hashes
        // So equals should return false for security reasons
        assertFalse(result);
    }

    @Test
    void equals_WithDifferentPasswords_ShouldReturnFalse() {
        // Arrange
        Password password1 = new Password("TestPass123!");
        Password password2 = new Password("DifferentPass123!");

        // Act
        boolean result = password1.equals(password2);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCode_WithSamePassword_ShouldReturnDifferentHashCodes() {
        // Arrange
        String plainPassword = "TestPass123!";
        Password password1 = new Password(plainPassword);
        Password password2 = new Password(plainPassword);

        // Act
        int hashCode1 = password1.hashCode();
        int hashCode2 = password2.hashCode();

        // Assert
        // Hash codes should be different for security (different salt)
        assertNotEquals(hashCode1, hashCode2, "Hash codes should be different due to salting");
    }

    @Test
    void toString_ShouldReturnMaskedPassword() {
        // Arrange
        Password password = new Password("TestPass123!");

        // Act
        String result = password.toString();

        // Assert
        assertEquals("********", result);
    }
}