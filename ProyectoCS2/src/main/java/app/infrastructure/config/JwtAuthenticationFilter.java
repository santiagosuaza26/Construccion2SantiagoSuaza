package app.infrastructure.config;

import app.application.service.AuthApplicationService;
import app.domain.services.AuthenticationService;
import app.domain.services.AuthenticationService.AuthenticatedUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro de autenticación JWT
 *
 * Intercepta todas las peticiones para validar tokens JWT y establecer
 * el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthApplicationService authApplicationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthApplicationService authApplicationService,
                                   JwtTokenProvider jwtTokenProvider) {
        this.authApplicationService = authApplicationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extraer token del header Authorization
            String token = extractTokenFromRequest(request);

            if (token != null && !token.isEmpty()) {
                // Validar token y obtener información del usuario
                AuthenticatedUser authenticatedUser = validateToken(token);

                if (authenticatedUser != null) {
                    // Crear authentication token para Spring Security
                    UsernamePasswordAuthenticationToken authentication =
                        createAuthenticationToken(authenticatedUser);

                    // Establecer contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Agregar información del usuario al request para uso posterior
                    request.setAttribute("currentUser", authenticatedUser);
                    request.setAttribute("userId", authenticatedUser.getIdCard());
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // En caso de error de autenticación, devolver error 401
            handleAuthenticationError(response, e);
        }
    }

    /**
     * Extrae el token JWT del header Authorization
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    /**
     * Valida el token JWT y retorna información del usuario
     */
    private AuthenticatedUser validateToken(String token) {
        try {
            // Verificar si el token ha expirado
            if (jwtTokenProvider.isTokenExpired(token)) {
                logger.warn("JWT token has expired");
                return null;
            }

            // Validar token y obtener información del usuario
            return jwtTokenProvider.validateToken(token);

        } catch (Exception e) {
            logger.error("Error validating JWT token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea el authentication token para Spring Security
     */
    private UsernamePasswordAuthenticationToken createAuthenticationToken(AuthenticatedUser user) {
        // Crear authorities basados en el rol del usuario
        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new UsernamePasswordAuthenticationToken(
            user.getIdCard(),
            null,
            authorities
        );
    }

    /**
     * Maneja errores de autenticación
     */
    private void handleAuthenticationError(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errorMessage = """
            {
                "success": false,
                "message": "Authentication failed",
                "errorCode": "AUTH_401",
                "timestamp": "%s"
            }
            """.formatted(java.time.LocalDateTime.now().toString());

        response.getWriter().write(errorMessage);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // No filtrar endpoints públicos
        return path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/register") ||
               path.startsWith("/h2-console") ||
               path.startsWith("/actuator") ||
               path.equals("/api/auth/") ||
               path.equals("/api/");
    }
}