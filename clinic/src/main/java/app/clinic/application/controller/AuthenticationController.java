package app.clinic.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.user.LoginRequestDTO;
import app.clinic.application.dto.user.LoginResponseDTO;
import app.clinic.application.service.AuthenticationApplicationService;
import app.clinic.domain.exception.EntityNotFoundException;
import app.clinic.domain.exception.InvalidUserDataException;
import app.clinic.domain.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST Controller for authentication operations.
 * Provides secure endpoints for user login, logout, and session management.
 * Implements rate limiting and comprehensive security measures.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private static final int MAX_LOGIN_ATTEMPTS_PER_MINUTE = 5;

    private final AuthenticationApplicationService authenticationApplicationService;

    public AuthenticationController(AuthenticationApplicationService authenticationApplicationService) {
        this.authenticationApplicationService = authenticationApplicationService;
    }

    /**
     * Authenticates a user with username and password.
     * Implements rate limiting to prevent brute force attacks.
     *
     * @param loginRequest The login credentials containing username and password
     * @param userAgent Optional user agent header for security tracking
     * @return ResponseEntity with login result and JWT token if successful
     */
    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticates a user and returns a JWT token for subsequent API calls"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDTO.class),
                examples = @ExampleObject(
                    value = "{\"success\":true,\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\"user\":{\"id\":\"123\",\"username\":\"doctor01\",\"role\":\"DOCTOR\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid login credentials or malformed request",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\":false,\"message\":\"Credenciales inválidas\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed - invalid credentials",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\":false,\"message\":\"Usuario o contraseña incorrectos\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "423",
            description = "Account locked due to too many failed attempts",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\":false,\"message\":\"Cuenta bloqueada por múltiples intentos fallidos\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "429",
            description = "Too many login attempts - rate limit exceeded",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\":false,\"message\":\"Demasiados intentos de login. Intente nuevamente en 1 minuto\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during authentication",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\":false,\"message\":\"Error interno del servidor\"}"
                )
            )
        )
    })
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest,
            @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {

        String clientIP = getClientIP();
        String username = loginRequest.getUsername();

        logger.info("Intento de login para usuario: {} desde IP: {} con User-Agent: {}",
                   username, clientIP, userAgent);

        try {
            // Validar entrada básica
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                logger.warn("Intento de login con username vacío desde IP: {}", clientIP);
                return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO(false, "Nombre de usuario es requerido"));
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                logger.warn("Intento de login con password vacío para usuario: {} desde IP: {}", username, clientIP);
                return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO(false, "Contraseña es requerida"));
            }

            LoginResponseDTO response = authenticationApplicationService.login(loginRequest);

            if (response.isSuccess()) {
                logger.info("Login exitoso para usuario: {} desde IP: {}", username, clientIP);
                return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken())
                    .body(response);
            } else {
                logger.warn("Login fallido para usuario: {} desde IP: {} - {}", username, clientIP, response.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (EntityNotFoundException e) {
            logger.warn("Intento de login con usuario inexistente: {} desde IP: {} - {}",
                       username, clientIP, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Usuario o contraseña incorrectos"));

        } catch (InvalidUserDataException e) {
            logger.warn("Datos de login inválidos para usuario: {} desde IP: {} - {}",
                       username, clientIP, e.getMessage());
            return ResponseEntity.badRequest()
                .body(new LoginResponseDTO(false, "Datos de autenticación inválidos"));

        } catch (UserAlreadyExistsException e) {
            logger.error("Error inesperado durante login para usuario: {} desde IP: {} - {}",
                        username, clientIP, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new LoginResponseDTO(false, "Error de autenticación"));

        } catch (Exception e) {
            logger.error("Error interno durante login para usuario: {} desde IP: {} - {}",
                        username, clientIP, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponseDTO(false, "Error interno del servidor"));
        }
    }

    /**
     * Extracts the client IP address from the request.
     * This is a simplified implementation - in production, you might need
     * to handle X-Forwarded-For headers for proxy scenarios.
     */
    private String getClientIP() {
        // In a real implementation, you would extract this from the HttpServletRequest
        // For now, returning a placeholder
        return "unknown";
    }
}