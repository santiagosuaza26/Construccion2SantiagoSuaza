package app.clinic.application.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root controller for the application.
 * Handles requests to the root path and provides basic navigation.
 */
@RestController
public class RootController {

    /**
     * Root endpoint - provides basic application information and navigation.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Clinic Management System");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("documentation", "/swagger-ui.html");
        response.put("api", "/api");
        response.put("health", "/api/public/health");
        response.put("h2-console", "/h2-console");

        return ResponseEntity.ok(response);
    }
}