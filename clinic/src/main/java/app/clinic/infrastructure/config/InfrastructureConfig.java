package app.clinic.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for infrastructure layer.
 * Configures repositories, services, and other infrastructure components.
 */
@Configuration
@EnableJpaRepositories(basePackages = "app.clinic.infrastructure.repository")
@EnableTransactionManagement
public class InfrastructureConfig {
    // Configuration class for infrastructure layer
    // JPA repositories are automatically configured by @EnableJpaRepositories
}