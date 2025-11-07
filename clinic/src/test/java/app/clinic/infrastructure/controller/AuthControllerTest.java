package app.clinic.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import app.clinic.infrastructure.dto.AuthResponseDTO;
import app.clinic.infrastructure.service.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthServiceImpl authService;


    private AuthController authController;

    private User testUser;
    private AuthController.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);

        // Create test user
        Credentials credentials = new Credentials(
            new Username("testuser"),
            new Password("TestPass123!")
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

        // Create login request
        loginRequest = new AuthController.LoginRequest();
        loginRequest.username = "testuser";
        loginRequest.password = "TestPass123!";
    }

    @Test
    void login_WithValidCredentials_ShouldReturnOkResponse() {
        // Arrange
        when(authService.authenticate("testuser", "TestPass123!")).thenReturn(new AuthResponseDTO("jwt-token", "Test User", "MEDICO", 86400000L));

        // Act
        ResponseEntity<AuthResponseDTO> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getFullName());
        assertEquals("Test User", response.getBody().getFullName());
        assertNotNull(response.getBody().getRole());
        assertEquals("MEDICO", response.getBody().getRole());
        assertNotNull(response.getBody().getToken());
        assertNotNull(response.getBody().getToken());
        assertNotNull(response.getBody().getExpiresIn());

        verify(authService).authenticate("testuser", "TestPass123!");
    }

    @Test
    void login_WithInvalidCredentials_ShouldThrowException() {
        // Arrange
        when(authService.authenticate("testuser", "wrongpassword"))
            .thenThrow(new RuntimeException("Credenciales inválidas"));

        loginRequest.password = "wrongpassword";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authService).authenticate("testuser", "wrongpassword");
    }

    @Test
    void login_WithNonExistentUser_ShouldThrowException() {
        // Arrange
        when(authService.authenticate("nonexistent", "TestPass123!"))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        loginRequest.username = "nonexistent";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authService).authenticate("nonexistent", "TestPass123!");
    }

    @Test
    void login_WithNullUsername_ShouldThrowException() {
        // Arrange
        loginRequest.username = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authService, never()).authenticate(anyString(), anyString());
    }

    @Test
    void login_WithNullPassword_ShouldThrowException() {
        // Arrange
        loginRequest.password = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authService, never()).authenticate(anyString(), anyString());
    }

    @Test
    void login_WithEmptyUsername_ShouldThrowException() {
        // Arrange
        loginRequest.username = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authService, never()).authenticate(anyString(), anyString());
    }

    @Test
    void login_WithEmptyPassword_ShouldThrowException() {
        // Arrange
        loginRequest.password = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authService, never()).authenticate(anyString(), anyString());
    }

}