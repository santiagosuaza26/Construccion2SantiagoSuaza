package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AuthenticateUserUseCase;
import app.clinic.infrastructure.dto.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para autenticación de usuarios")
public class AuthController {
    // Logger will be added when SLF4J dependency is available

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Permite a un usuario iniciar sesión en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas", content = @Content),
        @ApiResponse(responseCode = "401", description = "Usuario no autorizado", content = @Content)
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequest request) {
        // Validation is handled by @Valid annotation and Bean Validation
        if (request.username == null || request.username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (request.password == null || request.password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Sanitizar entrada básica
        String sanitizedUsername = request.username.trim().replaceAll("[<>\"'&]", "");
        String sanitizedPassword = request.password.trim();

        var user = authenticateUserUseCase.execute(sanitizedUsername, sanitizedPassword);

        var response = new AuthResponseDTO(
            "authenticated", // Simple token for basic auth
            user.getFullName(),
            user.getRole().toString(),
            0L // No expiration for basic auth
        );

        return ResponseEntity.ok(response);
    }


    public static class LoginRequest {
        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        @Schema(description = "Nombre de usuario", example = "johndoe", required = true)
        public String username;

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        @Schema(description = "Contraseña del usuario", example = "password123", required = true)
        public String password;
    }
}