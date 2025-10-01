package app.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración Web simplificada para la aplicación
 *
 * Configura únicamente recursos estáticos sin conflictos con CORS.
 * La configuración CORS se maneja en CorsConfig.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar recursos estáticos con menor prioridad que los endpoints API
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/public/**")
                .addResourceLocations("classpath:/public/");

        // Configurar recursos webjars si se utilizan
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}