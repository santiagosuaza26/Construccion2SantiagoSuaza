package app.clinic.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.model.valueobject.Username;
import app.clinic.domain.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    private User testUser;
    private String validUsername;
    private String validPassword;

    @BeforeEach
    void setUp() {
        validUsername = "testuser";
        validPassword = "TestPass123!";

        // Create a test user with hashed password
        Credentials credentials = new Credentials(
            new Username(validUsername),
            new Password(validPassword)
        );

        testUser = new User(
            credentials,
            "Test User",
            new Id("1234567890"),
            new Email("test@example.com"),
            new Phone("1234567890"),
            new DateOfBirth("01/01/1990"),
            new Address("Test Address"),
            Role.MEDICO
        );
    }

    @Test
    void execute_WithValidCredentials_ShouldReturnUser() {
        // Arrange
        when(userService.findUserByUsername(validUsername)).thenReturn(testUser);

        // Act
        User result = authenticateUserUseCase.execute(validUsername, validPassword);

        // Assert
        assertNotNull(result);
        assertEquals(validUsername, result.getCredentials().getUsername().getValue());
        assertEquals("Test User", result.getFullName());
        verify(userService).findUserByUsername(validUsername);
    }

    @Test
    void execute_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        String invalidUsername = "nonexistent";
        when(userService.findUserByUsername(invalidUsername)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticateUserUseCase.execute(invalidUsername, validPassword)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService).findUserByUsername(invalidUsername);
    }

    @Test
    void execute_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        String invalidPassword = "WrongPass123!";
        when(userService.findUserByUsername(validUsername)).thenReturn(testUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticateUserUseCase.execute(validUsername, invalidPassword)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService).findUserByUsername(validUsername);
    }

    @Test
    void execute_WithNullUsername_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticateUserUseCase.execute(null, validPassword)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService, never()).findUserByUsername(any());
    }

    @Test
    void execute_WithNullPassword_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticateUserUseCase.execute(validUsername, null)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService, never()).findUserByUsername(any());
    }

    @Test
    void execute_WithEmptyUsername_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticateUserUseCase.execute("", validPassword)
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService, never()).findUserByUsername(any());
    }

    @Test
    void execute_WithEmptyPassword_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authenticateUserUseCase.execute(validUsername, "")
        );
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService, never()).findUserByUsername(any());
    }
}