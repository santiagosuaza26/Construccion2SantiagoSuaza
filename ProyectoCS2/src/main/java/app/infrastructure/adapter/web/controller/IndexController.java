package app.infrastructure.adapter.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para servir la interfaz gráfica principal
 *
 * Este controlador asegura que la interfaz gráfica se sirva correctamente
 * sin conflictos con otros manejadores de recursos estáticos.
 */
@Controller
@RequestMapping("/interface")
public class IndexController {

    /**
     * Sirve la interfaz gráfica principal
     */
    @GetMapping("/app")
    public String app() {
        return "forward:/index.html";
    }

    /**
     * Health check específico para la interfaz
     */
    @GetMapping("/health")
    public String interfaceHealth() {
        return "Interface is running correctly";
    }

    /**
     * Información de la interfaz
     */
    @GetMapping("/info")
    public String interfaceInfo() {
        return "Sistema de Gestión Clínica CS2 - Interfaz Gráfica v1.0";
    }
}