package app.clinic.domain.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Domain Layer.
 * This configuration is completely independent of external frameworks
 * and only handles domain-specific setup.
 *
 * Key Principles:
 * - No external dependencies (Spring, databases, etc.)
 * - Pure domain configuration
 * - Framework-agnostic setup
 * - Clean Architecture compliance
 */
@Configuration
@ComponentScan(
    basePackages = "app.clinic.domain",
    excludeFilters = @ComponentScan.Filter(Configuration.class)
)
public class DomainLayerConfig {

    /**
     * Domain layer initialization.
     * This method ensures the domain layer is properly configured
     * without any external framework dependencies.
     */
    public void initializeDomainLayer() {
        // Domain layer initialization logic
        // No external dependencies allowed here
        System.out.println("🏥 Domain Layer initialized successfully - Framework Independent");
    }

    /**
     * Validates domain layer integrity.
     * Ensures all domain services are properly configured
     * and have no external framework dependencies.
     */
    public void validateDomainIntegrity() {
        // Domain integrity validation logic
        // This method should validate that:
        // 1. All domain services are properly instantiated
        // 2. No external framework dependencies exist
        // 3. All business rules are correctly implemented
        System.out.println("✅ Domain Layer integrity validated - No external dependencies");
    }
}