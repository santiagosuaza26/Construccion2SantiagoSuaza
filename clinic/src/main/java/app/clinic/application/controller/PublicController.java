package app.clinic.application.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public controller for basic API testing and health checks.
 * Provides endpoints that don't require authentication for development and testing purposes.
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    /**
     * Root endpoint - redirects to API information or provides basic API info.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Clinic Management System API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("documentation", "/swagger-ui.html");
        response.put("health", "/api/public/health");
        response.put("api-info", "/api/public/info");

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint to verify API is running.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Clinic Management System API");
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    /**
     * Welcome message endpoint.
     */
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Clinic Management System API");
        response.put("documentation", "/swagger-ui.html");
        response.put("h2-console", "/h2-console");

        return ResponseEntity.ok(response);
    }

    /**
     * API information endpoint.
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Clinic Management System API");
        info.put("version", "1.0.0");
        info.put("description", "Comprehensive API for clinic management system");

        Map<String, Object> endpoints = new HashMap<>();
        endpoints.put("health", "GET /api/public/health");
        endpoints.put("welcome", "GET /api/public/welcome");
        endpoints.put("swagger-ui", "GET /swagger-ui.html");
        endpoints.put("h2-console", "GET /h2-console");
        endpoints.put("patients", "GET|POST|PUT|DELETE /api/patients/** (requires auth)");
        endpoints.put("appointments", "GET|POST|PUT|DELETE /api/appointments/** (requires auth)");

        info.put("available-endpoints", endpoints);

        return ResponseEntity.ok(info);
    }
}