package app.clinic.infrastructure.config;

import app.clinic.domain.service.RoleBasedAccessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RoleBasedAccessService roleBasedAccessService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                         RoleBasedAccessService roleBasedAccessService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()

                // Recursos Humanos - solo HR
                .requestMatchers("/api/users/**").hasRole("RECURSOS_HUMANOS")

                // Personal Administrativo
                .requestMatchers("/api/patients/**").hasRole("PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/api/appointments/**").hasRole("PERSONAL_ADMINISTRATIVO")
                .requestMatchers("/api/billing/**").hasRole("PERSONAL_ADMINISTRATIVO")

                // Soporte de Información
                .requestMatchers("/api/inventory/**").hasRole("SOPORTE_DE_INFORMACION")

                // Enfermeras
                .requestMatchers("/api/vitals/**").hasRole("ENFERMERA")
                .requestMatchers("/api/administration/**").hasRole("ENFERMERA")

                // Médicos
                .requestMatchers("/api/medical-records/**").hasRole("MEDICO")
                .requestMatchers("/api/orders/**").hasRole("MEDICO")

                // Todos los demás requieren autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}