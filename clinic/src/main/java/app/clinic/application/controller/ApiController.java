package app.clinic.application.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Information Controller
 * Handles requests to the /api path and provides API information and navigation.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * API root endpoint - provides API information and available endpoints.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Clinic Management System API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("description", "Comprehensive API for clinic management system");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("public", "/api/public/**");
        endpoints.put("authentication", "/api/auth/**");
        endpoints.put("users", "/api/users/**");
        endpoints.put("patients", "/api/patients/**");
        endpoints.put("appointments", "/api/appointments/**");
        endpoints.put("medical-records", "/api/medical-records/**");
        endpoints.put("orders", "/api/orders/**");
        endpoints.put("patient-visits", "/api/patient-visits/**");
        endpoints.put("billing", "/api/billing/**");
        endpoints.put("inventory", "/api/inventory/**");

        response.put("available-endpoints", endpoints);
        response.put("documentation", "/swagger-ui.html");
        response.put("health-check", "/api/public/health");

        return ResponseEntity.ok(response);
    }
}