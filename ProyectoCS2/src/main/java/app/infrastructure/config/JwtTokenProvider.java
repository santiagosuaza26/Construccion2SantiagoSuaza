package app.infrastructure.config;

import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Proveedor de tokens JWT simplificado
 *
 * Implementa generación y validación básica de tokens JWT sin
 * depender de librerías externas complejas.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGeneration2024}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 horas en milisegundos
    private Long jwtExpiration;

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private final ObjectMapper objectMapper;

    public JwtTokenProvider() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Genera un token JWT para el usuario autenticado
     */
    public String generateToken(AuthenticatedUser user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", user.getIdCard());
            claims.put("username", "user_" + user.getIdCard());
            claims.put("role", user.getRole().name());
            claims.put("isStaff", user.isStaff());
            claims.put("iat", System.currentTimeMillis());
            claims.put("exp", System.currentTimeMillis() + jwtExpiration);

            return createToken(claims);
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Valida un token JWT y retorna la información del usuario
     */
    public AuthenticatedUser validateToken(String token) {
        try {
            Map<String, Object> claims = parseToken(token);

            String userId = (String) claims.get("sub");
            String roleName = (String) claims.get("role");
            Boolean isStaff = (Boolean) claims.get("isStaff");

            if (userId == null || roleName == null) {
                return null;
            }

            Role role = Role.valueOf(roleName);

            return new AuthenticatedUser(userId, "User", role, isStaff != null ? isStaff : true);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extrae el ID del usuario del token
     */
    public String getUserIdFromToken(String token) {
        try {
            Map<String, Object> claims = parseToken(token);
            return (String) claims.get("sub");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    public boolean isTokenExpired(String token) {
        try {
            Map<String, Object> claims = parseToken(token);
            Long exp = (Long) claims.get("exp");

            if (exp == null) {
                return true;
            }

            return exp < System.currentTimeMillis();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Crea el token JWT con los claims proporcionados
     */
    private String createToken(Map<String, Object> claims) throws Exception {
        String header = createHeader();
        String payload = createPayload(claims);
        String signature = createSignature(header + "." + payload);

        return header + "." + payload + "." + signature;
    }

    /**
     * Parsea y valida un token JWT
     */
    private Map<String, Object> parseToken(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        // Verificar firma
        String expectedSignature = createSignature(header + "." + payload);
        if (!signature.equals(expectedSignature)) {
            throw new IllegalArgumentException("Invalid token signature");
        }

        // Decodificar payload
        return parsePayload(payload);
    }

    /**
     * Crea el header del JWT
     */
    private String createHeader() throws JsonProcessingException {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String headerJson = objectMapper.writeValueAsString(header);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes());
    }

    /**
     * Crea el payload del JWT
     */
    private String createPayload(Map<String, Object> claims) throws JsonProcessingException {
        String payloadJson = objectMapper.writeValueAsString(claims);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());
    }

    /**
     * Parsea el payload del JWT
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parsePayload(String encodedPayload) throws Exception {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPayload);
        String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);

        // Usar Jackson para parsear JSON de forma segura
        JsonNode jsonNode = objectMapper.readTree(payloadJson);
        return objectMapper.convertValue(jsonNode, Map.class);
    }

    /**
     * Crea la firma HMAC del token
     */
    private String createSignature(String data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(), HMAC_ALGORITHM);
        mac.init(secretKey);

        byte[] signatureBytes = mac.doFinal(data.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }

}