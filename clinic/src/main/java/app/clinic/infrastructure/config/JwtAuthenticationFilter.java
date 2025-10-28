package app.clinic.infrastructure.config;

import java.io.IOException;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String SECRET_KEY = "clinic-secret-key-for-jwt-tokens"; // En producción usar variable de entorno

    public JwtAuthenticationFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                JwtParser parser = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build();
                Claims claims = parser.parseClaimsJws(token).getBody();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);
                String sessionId = claims.get("sessionId", String.class);

                // Check if session is still valid in Redis
                if (username != null && role != null && sessionId != null) {
                    String storedUsername = redisTemplate.opsForValue().get("session:" + sessionId);
                    if (storedUsername != null && storedUsername.equals(username)) {
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
                // Token inválido - continuar sin autenticación
            }
        }

        filterChain.doFilter(request, response);
    }
}