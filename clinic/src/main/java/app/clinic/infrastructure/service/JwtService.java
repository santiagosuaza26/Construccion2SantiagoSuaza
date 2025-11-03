package app.clinic.infrastructure.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user, String sessionId) {
        return Jwts.builder()
            .subject(user.getCredentials().getUsername().getValue())
            .claim("role", user.getRole().toString())
            .claim("sessionId", sessionId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
            .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()), Jwts.SIG.HS256)
            .compact();
    }

    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public long getExpirationTime() {
        return jwtProperties.getExpirationTime();
    }

    public String getSecretKey() {
        return jwtProperties.getSecretKey();
    }
}