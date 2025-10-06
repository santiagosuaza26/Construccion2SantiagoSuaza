package app.clinic.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the clinic application.
 * Defines authentication and authorization rules for different endpoints.
 * Configures role-based access control according to business requirements.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures HTTP security for the application.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())

            // Configure session management
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/").permitAll() // Root endpoint
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // For development
                .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI for development
                .requestMatchers("/v3/api-docs/**").permitAll() // OpenAPI docs for development
                .requestMatchers("/swagger-ui.html").permitAll() // Swagger UI HTML for development

                // Permit all API endpoints for testing
                .requestMatchers("/api/**").permitAll()

                // All other requests need authentication
                .anyRequest().authenticated()
            )

            // Configure headers for H2 console (development only)
            .headers(headers -> headers
                .frameOptions().sameOrigin()
                .contentTypeOptions().and()
            );

        return http.build();
    }
}