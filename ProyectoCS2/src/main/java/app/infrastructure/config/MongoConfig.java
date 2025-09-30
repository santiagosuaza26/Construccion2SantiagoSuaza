package app.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuración específica para MongoDB
 * Asegura que los repositorios MongoDB estén correctamente habilitados
 */
// DESHABILITADO: Configuración específica para MongoDB
// Esta configuración se deshabilita porque estamos usando implementación en memoria
// @Configuration
// @EnableMongoRepositories(
//     basePackages = "app.infrastructure.adapter.repository",
//     mongoTemplateRef = "mongoTemplate"
// )
// public class MongoConfig {

//     // Configuración adicional de MongoDB puede ir aquí si es necesaria
//     // Por ejemplo:
//     // - Configuración de conexión
//     // - Configuración de índices
//     // - Configuración de auditoría
// }