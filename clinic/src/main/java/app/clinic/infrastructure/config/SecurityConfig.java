package app.clinic.infrastructure.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${clinic.cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;

    @Value("${clinic.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()

                // Endpoints protegidos por roles
                .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_RECURSOS_HUMANOS", "ROLE_PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/api/patients/**").hasAnyAuthority("ROLE_PERSONAL_ADMINISTRATIVO", "ROLE_MEDICO", "ROLE_ENFERMERA")
                .requestMatchers("/api/appointments/**").hasAnyAuthority("ROLE_PERSONAL_ADMINISTRATIVO", "ROLE_MEDICO")
                .requestMatchers("/api/inventory/**").hasAnyAuthority("ROLE_PERSONAL_ADMINISTRATIVO", "ROLE_MEDICO", "ROLE_SOPORTE_DE_INFORMACION")
                .requestMatchers("/api/medical/**").hasAuthority("ROLE_MEDICO")
                .requestMatchers("/api/nurse/**").hasAuthority("ROLE_ENFERMERA")
                .requestMatchers("/api/billing/**").hasAnyAuthority("ROLE_PERSONAL_ADMINISTRATIVO", "ROLE_MEDICO")
                .requestMatchers("/api/support/**").hasAnyAuthority("ROLE_RECURSOS_HUMANOS", "ROLE_PERSONAL_ADMINISTRATIVO", "ROLE_MEDICO", "ROLE_ENFERMERA", "ROLE_SOPORTE_DE_INFORMACION")

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Permitir acceso a H2 console
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Usar variables de entorno para configurar orígenes permitidos
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOriginPatterns(origins);
        
        // Usar variables de entorno para configurar métodos permitidos
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods);
        
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L); // Cache preflight por 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}