package app.clinic.application.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CORS configuration for the clinic application.
 * Defines cross-origin resource sharing policies for API endpoints.
 * Allows frontend applications to communicate with the backend API.
 */
@Configuration
public class CorsConfig {

    /**
     * Configures CORS settings for the application.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins (adjust according to your frontend URL)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",    // React development server
            "http://localhost:4200",    // Angular development server
            "http://localhost:8000",    // Frontend development server (Python HTTP server)
            "http://localhost:8080",    // Frontend files served from same server
            "http://127.0.0.1:8080",   // Alternative localhost
            "https://your-frontend-domain.com", // Production frontend
            "null"                      // Allow file:// protocol (when opening HTML directly)
        ));

        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));

        // Expose headers
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Max age for preflight requests (in seconds)
        configuration.setMaxAge(3600L);

        // Register configuration for all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}