package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Username;
import app.clinic.domain.repository.UserRepository;
import app.clinic.infrastructure.dto.AuthResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO authenticate(String username, String password) {
        User user = userRepository.findByUsername(new Username(username))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Para desarrollo: comparar contraseñas en texto plano (como están en la DB)
        if (!password.equals(user.getCredentials().getPassword().getValue())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user, jwtService.generateSessionId());

        return new AuthResponseDTO(token, user.getFullName(), user.getRole().name(), jwtService.getExpirationTime());
    }

    public void logout(String token) {
        // En JWT, el logout se maneja del lado del cliente invalidando el token
        // Opcionalmente, se puede implementar una lista negra de tokens
    }

    public User getProfile(String token) {
        // Extraer username del token y buscar usuario
        String username = extractUsernameFromToken(token);
        return userRepository.findByUsername(new Username(username))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private String extractUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtService.getSecretKey().getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido");
        }
    }
}