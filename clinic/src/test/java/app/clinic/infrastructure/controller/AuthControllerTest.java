package app.clinic.infrastructure.controller;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.clinic.application.usecase.AuthenticateUserUseCase;
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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private org.springframework.data.redis.core.ValueOperations<String, String> valueOperations;

    @Mock
    private app.clinic.infrastructure.service.JwtService jwtService;

    private AuthController authController;

    private User testUser;
    private AuthController.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticateUserUseCase, jwtService, redisTemplate);

        // Initialize mocks
        Mockito.lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);

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
        when(authenticateUserUseCase.execute("testuser", "TestPass123!")).thenReturn(testUser);
        when(jwtService.generateSessionId()).thenReturn("test-session-id");
        when(jwtService.generateToken(testUser, "test-session-id")).thenReturn("test-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(86400000L);

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
        assertEquals("test-jwt-token", response.getBody().getToken());
        assertEquals(86400000L, response.getBody().getExpiresIn());

        verify(authenticateUserUseCase).execute("testuser", "TestPass123!");
        verify(jwtService).generateSessionId();
        verify(jwtService).generateToken(testUser, "test-session-id");
        verify(jwtService, Mockito.times(2)).getExpirationTime();
        verify(redisTemplate.opsForValue()).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void login_WithInvalidCredentials_ShouldThrowException() {
        // Arrange
        when(authenticateUserUseCase.execute("testuser", "wrongpassword"))
            .thenThrow(new IllegalArgumentException("Invalid username or password"));

        loginRequest.password = "wrongpassword";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authenticateUserUseCase).execute("testuser", "wrongpassword");
        verify(redisTemplate.opsForValue(), never()).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void login_WithNonExistentUser_ShouldThrowException() {
        // Arrange
        when(authenticateUserUseCase.execute("nonexistent", "TestPass123!"))
            .thenThrow(new IllegalArgumentException("Invalid username or password"));

        loginRequest.username = "nonexistent";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.login(loginRequest);
        });
        assertNotNull(exception);

        verify(authenticateUserUseCase).execute("nonexistent", "TestPass123!");
        verify(redisTemplate.opsForValue(), never()).set(anyString(), anyString(), anyLong(), any());
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

        verify(authenticateUserUseCase, never()).execute(anyString(), anyString());
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

        verify(authenticateUserUseCase, never()).execute(anyString(), anyString());
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

        verify(authenticateUserUseCase, never()).execute(anyString(), anyString());
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

        verify(authenticateUserUseCase, never()).execute(anyString(), anyString());
    }

    @Test
    void logout_WithValidToken_ShouldReturnOk() {
        // Arrange
        String validToken = "Bearer " + Jwts.builder()
            .subject("testuser")
            .claim("role", "MEDICO")
            .claim("sessionId", "1234567890")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(Keys.hmacShaKeyFor("test-secret-key-with-sufficient-length-for-hmac-sha256-algorithm".getBytes()), Jwts.SIG.HS256)
            .compact();

        when(jwtService.getSecretKey()).thenReturn("test-secret-key-with-sufficient-length-for-hmac-sha256-algorithm");
        when(redisTemplate.delete(anyString())).thenReturn(true);

        // Act
        ResponseEntity<Void> response = authController.logout(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(redisTemplate).delete("session:1234567890");
    }

    @Test
    void logout_WithInvalidToken_ShouldReturnOk() {
        // Arrange
        String invalidToken = "Bearer invalid-token";
        when(jwtService.getSecretKey()).thenReturn("test-secret-key-with-sufficient-length-for-hmac-sha256-algorithm");

        // Act
        ResponseEntity<Void> response = authController.logout(invalidToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void logout_WithNullToken_ShouldReturnOk() {
        // Act
        ResponseEntity<Void> response = authController.logout(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void logout_WithNoBearerPrefix_ShouldReturnOk() {
        // Arrange
        String tokenWithoutBearer = "eyJhbGciOiJIUzI1NiJ9.invalid";

        // Act
        ResponseEntity<Void> response = authController.logout(tokenWithoutBearer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(redisTemplate, never()).delete(anyString());
    }
}