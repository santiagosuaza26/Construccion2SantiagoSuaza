package app.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de logging para la aplicación
 *
 * Proporciona utilidades de logging consistentes y estructuradas
 * para toda la aplicación.
 */
@Configuration
public class LoggingConfig {

    /**
     * Crea un logger para la clase especificada
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Loguea una operación exitosa
     */
    public static void logOperationSuccess(Logger logger, String operation, String details) {
        logger.info("✅ {} - {}", operation, details);
    }

    /**
     * Loguea una operación fallida
     */
    public static void logOperationFailure(Logger logger, String operation, String error, Exception e) {
        logger.error("❌ {} - Error: {} - Exception: {}", operation, error, e.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("Stack trace: ", e);
        }
    }

    /**
     * Loguea una operación de autenticación
     */
    public static void logAuthentication(Logger logger, String action, String userId, boolean success) {
        if (success) {
            logger.info("🔐 AUTH SUCCESS - {} - User: {}", action, userId);
        } else {
            logger.warn("🔐 AUTH FAILED - {} - User: {}", action, userId);
        }
    }

    /**
     * Loguea una operación de autorización
     */
    public static void logAuthorization(Logger logger, String userId, String resource, String action, boolean granted) {
        if (granted) {
            logger.debug("🔑 ACCESS GRANTED - User: {} - Resource: {} - Action: {}", userId, resource, action);
        } else {
            logger.warn("🔑 ACCESS DENIED - User: {} - Resource: {} - Action: {}", userId, resource, action);
        }
    }

    /**
     * Loguea una operación de auditoría
     */
    public static void logAudit(Logger logger, String userId, String operation, String details) {
        logger.info("📋 AUDIT - User: {} - Operation: {} - Details: {}", userId, operation, details);
    }

    /**
     * Loguea una operación de base de datos
     */
    public static void logDatabaseOperation(Logger logger, String operation, String entity, String id) {
        logger.debug("💾 DB {} - Entity: {} - ID: {}", operation, entity, id);
    }

    /**
     * Loguea una validación de negocio
     */
    public static void logBusinessValidation(Logger logger, String validation, boolean passed, String details) {
        if (passed) {
            logger.debug("✅ VALIDATION PASSED - {} - {}", validation, details);
        } else {
            logger.warn("❌ VALIDATION FAILED - {} - {}", validation, details);
        }
    }

    /**
     * Loguea el inicio de una operación
     */
    public static void logOperationStart(Logger logger, String operation, String params) {
        logger.debug("🚀 {} STARTED - Params: {}", operation, params);
    }

    /**
     * Loguea el fin de una operación
     */
    public static void logOperationEnd(Logger logger, String operation, long durationMs) {
        logger.debug("🏁 {} COMPLETED - Duration: {}ms", operation, durationMs);
    }

    /**
     * Loguea una advertencia de rendimiento
     */
    public static void logPerformanceWarning(Logger logger, String operation, long durationMs, long thresholdMs) {
        logger.warn("⚠️ PERFORMANCE WARNING - {} took {}ms (threshold: {}ms)", operation, durationMs, thresholdMs);
    }

    /**
     * Loguea información de configuración
     */
    public static void logConfiguration(Logger logger, String component, String config) {
        logger.info("⚙️ CONFIG - {}: {}", component, config);
    }
}