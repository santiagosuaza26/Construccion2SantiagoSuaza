package app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configuración para desarrollo - orígenes específicos
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:8080",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:8080",
            "https://tu-dominio.com", // Reemplazar con dominio real en producción
            "https://www.tu-dominio.com" // Reemplazar con dominio real en producción
        ));

        // Métodos HTTP permitidos (más restrictivos)
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // Headers específicos permitidos (más restrictivos)
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "X-Requested-With"
        ));

        // Headers expuestos (solo los necesarios)
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization"
        ));

        // Permitir credenciales para autenticación
        configuration.setAllowCredentials(true);

        // Tiempo máximo de cache para preflight requests (1 hora)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Aplicar configuración solo a rutas API específicas
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}