package app.clinic.infrastructure.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AuthenticateUserUseCase;
import app.clinic.infrastructure.dto.AuthResponseDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String SECRET_KEY = "clinic-secret-key-for-jwt-tokens"; // En producción usar variable de entorno
    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, RedisTemplate<String, String> redisTemplate) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest request) {
        var user = authenticateUserUseCase.execute(request.username, request.password);

        // Generate unique session ID
        String sessionId = UUID.randomUUID().toString();

        String token = Jwts.builder()
            .subject(user.getCredentials().getUsername().getValue())
            .claim("role", user.getRole().toString())
            .claim("sessionId", sessionId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();

        // Store session in Redis with expiration
        redisTemplate.opsForValue().set("session:" + sessionId, user.getCredentials().getUsername().getValue(), EXPIRATION_TIME / 1000, java.util.concurrent.TimeUnit.SECONDS);

        var response = new AuthResponseDTO(
            token,
            user.getFullName(),
            user.getRole().toString(),
            EXPIRATION_TIME
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                var parser = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build();
                var claims = parser.ae(token).getBody();

                String sessionId = claims.get("sessionId", String.class);
                if (sessionId != null) {
                    redisTemplate.delete("session:" + sessionId);
                }
            } catch (Exception e) {
                // Token inválido - continuar
            }
        }
        return ResponseEntity.ok().build();
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }
}