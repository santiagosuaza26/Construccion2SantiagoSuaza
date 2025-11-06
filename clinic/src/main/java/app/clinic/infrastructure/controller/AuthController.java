package app.clinic.infrastructure.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AuthenticateUserUseCase;
import app.clinic.infrastructure.dto.AuthResponseDTO;
import app.clinic.infrastructure.dto.UserDTO;
import app.clinic.infrastructure.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, JwtService jwtService, RedisTemplate<String, String> redisTemplate) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
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
            throw new IllegalArgumentException("Username is required");
        }
        if (request.password == null || request.password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Sanitizar entrada básica
        String sanitizedUsername = request.username.trim().replaceAll("[<>\"'&]", "");
        String sanitizedPassword = request.password.trim();

        var user = authenticateUserUseCase.execute(sanitizedUsername, sanitizedPassword);

        // Generate unique session ID
        String sessionId = jwtService.generateSessionId();

        String token = jwtService.generateToken(user, sessionId);

        // Store session in Redis with expiration (only if Redis is available)
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set("session:" + sessionId, user.getCredentials().getUsername().getValue(), jwtService.getExpirationTime() / 1000, java.util.concurrent.TimeUnit.SECONDS);
        }

        var response = new AuthResponseDTO(
            token,
            user.getFullName(),
            user.getRole().toString(),
            jwtService.getExpirationTime()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Permite a un usuario cerrar sesión en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout exitoso", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token inválido", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                var parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtService.getSecretKey().getBytes()))
                    .build();
                var claims = parser.parseSignedClaims(token).getPayload();

                String sessionId = claims.get("sessionId", String.class);
                if (sessionId != null && redisTemplate != null) {
                    redisTemplate.delete("session:" + sessionId);
                }
            } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.ExpiredJwtException | IllegalArgumentException e) {
                // Token inválido - continuar
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    @Operation(summary = "Obtener perfil de usuario", description = "Obtiene los datos del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token inválido", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserDTO> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);
        try {
            var parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtService.getSecretKey().getBytes()))
                .build();
            var claims = parser.parseSignedClaims(token).getPayload();

            String username = claims.getSubject();
            var user = authenticateUserUseCase.execute(username, "dummy"); // We just need the user, password validation not needed here

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            var dto = new UserDTO(
                user.getIdentificationNumber().getValue(),
                user.getFullName(),
                user.getEmail().getValue(),
                user.getPhone().getValue(),
                user.getDateOfBirth().getValue().toString(),
                user.getAddress().getValue(),
                user.getRole().toString(),
                user.getCredentials().getUsername().getValue()
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
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