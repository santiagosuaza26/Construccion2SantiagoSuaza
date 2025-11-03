package app.clinic.infrastructure.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Configuration
public class RateLimitConfig {

    @Bean("generalBucket")
    public Bucket createNewBucket() {
        // Permitir 100 solicitudes por minuto
        Refill refill = Refill.intervally(100, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(100, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean("authBucket")
    public Bucket createAuthBucket() {
        // Rate limiting más estricto para autenticación: 5 intentos por minuto
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean("userManagementBucket")
    public Bucket createUserManagementBucket() {
        // Rate limiting para gestión de usuarios: 10 operaciones por hora
        Refill refill = Refill.intervally(10, Duration.ofHours(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean("patientManagementBucket")
    public Bucket createPatientManagementBucket() {
        // Rate limiting para gestión de pacientes: 50 operaciones por hora
        Refill refill = Refill.intervally(50, Duration.ofHours(1));
        Bandwidth limit = Bandwidth.classic(50, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean("medicalOperationsBucket")
    public Bucket createMedicalOperationsBucket() {
        // Rate limiting para operaciones médicas: 200 operaciones por hora
        Refill refill = Refill.intervally(200, Duration.ofHours(1));
        Bandwidth limit = Bandwidth.classic(200, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}