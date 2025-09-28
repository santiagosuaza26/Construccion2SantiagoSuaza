package app.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
public class ApplicationConfig {

    // Configuración adicional de la aplicación puede ir aquí
    // Por ejemplo:
    // - Configuración de Jackson para serialización JSON
    // - Configuración de caché
    // - Configuración de métricas
    // - Configuración de logging personalizado
}