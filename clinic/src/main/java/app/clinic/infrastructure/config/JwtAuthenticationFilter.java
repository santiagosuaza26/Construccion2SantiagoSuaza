package app.clinic.infrastructure.config;

import java.io.IOException;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
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
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(RedisTemplate<String, String> redisTemplate, JwtProperties jwtProperties) {
        this.redisTemplate = redisTemplate;
        this.jwtProperties = jwtProperties;
        String secretKey = jwtProperties.getSecretKey();
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key must be configured");
        }
        if (secretKey.length() < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 characters long");
        }
    }

    // Constructor for development without Redis
    public JwtAuthenticationFilter(JwtProperties jwtProperties) {
        this.redisTemplate = null;
        this.jwtProperties = jwtProperties;
        String secretKey = jwtProperties.getSecretKey();
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key must be configured");
        }
        if (secretKey.length() < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 characters long");
        }
    }

    // Default constructor for Spring
    public JwtAuthenticationFilter() {
        this.redisTemplate = null;
        this.jwtProperties = new JwtProperties();
        // Note: Validation will happen in doFilterInternal when needed
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                JwtParser parser = Jwts.parser()
                    .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                    .build();
                Claims claims = parser.parseSignedClaims(token).getPayload();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);
                String sessionId = claims.get("sessionId", String.class);

                // Check if session is still valid in Redis (only if Redis is available)
                if (username != null && role != null) {
                    if (redisTemplate != null && sessionId != null) {
                        String storedUsername = redisTemplate.opsForValue().get("session:" + sessionId);
                        if (storedUsername != null && storedUsername.equals(username)) {
                            // Create authentication token with proper role format
                            UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
                                );

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } else {
                            // Session expired or invalid
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Session expired or invalid\"}");
                            return;
                        }
                    } else {
                        // Redis not available (development mode), authenticate without session validation
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token expired\",\"message\":\"Please login again\"}");
                return;
            } catch (MalformedJwtException | SecurityException | UnsupportedJwtException | IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid token\",\"message\":\"Token format is invalid\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}