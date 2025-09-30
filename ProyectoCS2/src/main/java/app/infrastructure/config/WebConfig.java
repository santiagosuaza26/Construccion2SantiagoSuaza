package app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración Web para la aplicación
 *
 * Configura CORS y manejo de recursos estáticos para la interfaz web.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
        }

        @Bean
        public org.springframework.web.servlet.ViewResolver viewResolver() {
                org.springframework.web.servlet.view.InternalResourceViewResolver resolver =
                        new org.springframework.web.servlet.view.InternalResourceViewResolver();
                resolver.setPrefix("/WEB-INF/views/");
                resolver.setSuffix(".jsp");
                resolver.setOrder(2); // Menor prioridad que los controladores REST
                return resolver;
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Configurar recursos estáticos con menor prioridad que los controladores REST
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");

                // Servir CSS, JS e imágenes
                registry.addResourceHandler("/css/**")
                        .addResourceLocations("classpath:/static/css/");

                registry.addResourceHandler("/js/**")
                        .addResourceLocations("classpath:/static/js/");

                registry.addResourceHandler("/images/**")
                        .addResourceLocations("classpath:/static/images/");

                // Swagger UI resources - debe tener mayor prioridad
                registry.addResourceHandler("/swagger-ui/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/");
        }

        @Override
        public void configurePathMatch(org.springframework.web.servlet.config.annotation.PathMatchConfigurer configurer) {
                // Configuración para evitar conflictos con recursos estáticos
                configurer.setUseSuffixPatternMatch(false);
                configurer.setUseTrailingSlashMatch(true);
        }

        @Override
        public void configureHandlerExceptionResolvers(java.util.List<org.springframework.web.servlet.HandlerExceptionResolver> resolvers) {
                // Configurar el orden de resolución de excepciones
                resolvers.add(0, new org.springframework.web.servlet.handler.SimpleMappingExceptionResolver());
        }

        @Bean
        @org.springframework.core.annotation.Order(1)
        public org.springframework.web.servlet.handler.SimpleMappingExceptionResolver exceptionResolver() {
                org.springframework.web.servlet.handler.SimpleMappingExceptionResolver resolver =
                        new org.springframework.web.servlet.handler.SimpleMappingExceptionResolver();

                java.util.Properties mappings = new java.util.Properties();
                mappings.setProperty("NoResourceFoundException", "error/404");
                mappings.setProperty("org.springframework.web.servlet.resource.NoResourceFoundException", "error/404");

                resolver.setExceptionMappings(mappings);
                resolver.setDefaultErrorView("error/500");
                resolver.setExceptionAttribute("exception");

                return resolver;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(1); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.resource.ResourceHttpRequestHandler resourceHttpRequestHandler() {
                org.springframework.web.servlet.resource.ResourceHttpRequestHandler handler =
                        new org.springframework.web.servlet.resource.ResourceHttpRequestHandler();
                return handler;
        }

        @Bean
        public org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
                org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter adapter =
                        new org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter();
                adapter.setOrder(0); // Máxima prioridad
                return adapter;
        }

        @Bean
        public org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping requestMappingHandlerMapping() {
                org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping mapping =
                        new org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping();
                mapping.setOrder(0); // Máxima prioridad para controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping resourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(1); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping staticResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(2); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping swaggerResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(3); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping actuatorResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(4); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping h2ConsoleResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(5); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping defaultResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(6); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping catchAllResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(7); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping fallbackResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(8); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping lowestPriorityResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(9); // Menor prioridad que controladores REST
                return mapping;
        }

        @Bean
        public org.springframework.web.servlet.handler.SimpleUrlHandlerMapping finalFallbackResourceHandlerMapping() {
                org.springframework.web.servlet.handler.SimpleUrlHandlerMapping mapping =
                        new org.springframework.web.servlet.handler.SimpleUrlHandlerMapping();
                mapping.setOrder(10); // Menor prioridad que controladores REST
                return mapping;
        }
}