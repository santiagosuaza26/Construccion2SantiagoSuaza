package app.clinic.infrastructure.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "clinic-management-system");
        response.put("timestamp", System.currentTimeMillis());
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, Object>> getWelcomeInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenido al Sistema de Gestión de Clínica");
        response.put("description", "Sistema integral para la gestión de clínicas médicas");
        response.put("features", new String[]{
            "Gestión de usuarios",
            "Registro de pacientes",
            "Citas médicas",
            "Registros médicos",
            "Facturación",
            "Inventario"
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Clinic Management System API");
        response.put("version", "1.0.0");
        response.put("description", "API REST para el sistema de gestión de clínicas");
        response.put("baseUrl", "/api");
        response.put("documentation", "/swagger-ui.html");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("auth", "/api/auth");
        endpoints.put("users", "/api/users");
        endpoints.put("patients", "/api/patients");
        endpoints.put("appointments", "/api/appointments");
        endpoints.put("medical", "/api/medical");
        endpoints.put("billing", "/api/billing");
        endpoints.put("inventory", "/api/inventory");
        endpoints.put("nurse", "/api/nurse");

        response.put("endpoints", endpoints);

        return ResponseEntity.ok(response);
    }
}