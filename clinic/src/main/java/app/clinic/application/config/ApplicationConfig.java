package app.clinic.application.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuración principal de la capa de aplicación.
 * Importa todas las configuraciones necesarias para el funcionamiento
 * de los casos de uso y servicios de aplicación.
 */
@Configuration
@ComponentScan(basePackages = {
    "app.clinic.application.service",
    "app.clinic.application.usecase",
    "app.clinic.application.mapper"
})
@Import({
    app.clinic.domain.config.DomainConfig.class
})
public class ApplicationConfig {

    /**
     * Inicialización de la capa de aplicación.
     * Se ejecuta después de que el dominio esté completamente configurado.
     */
    public void initializeApplicationLayer() {
        System.out.println("🏢 Application Layer initialized successfully");
        System.out.println("   - Use Cases: Ready");
        System.out.println("   - Application Services: Ready");
        System.out.println("   - Mappers: Ready");
        System.out.println("   - Domain Integration: Ready");
    }
}