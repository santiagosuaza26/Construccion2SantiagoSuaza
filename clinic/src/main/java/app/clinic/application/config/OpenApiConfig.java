package app.clinic.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI configuration for the clinic application.
 * Provides API documentation and Swagger UI setup.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures OpenAPI documentation for the application.
     */
    @Bean
    public OpenAPI clinicOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080").description("Development server"))
                .addServersItem(new Server().url("https://api.clinic-management.com").description("Production server"))
                .info(new Info()
                        .title("Clinic Management System API")
                        .description("REST API for clinic management system handling users, patients, medical records, orders, visits, and billing")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Clinic Management Team")
                                .email("support@clinic-management.com")
                                .url("https://clinic-management.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}