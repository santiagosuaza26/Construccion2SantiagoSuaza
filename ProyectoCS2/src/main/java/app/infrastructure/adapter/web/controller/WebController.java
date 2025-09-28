package app.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir páginas web estáticas
 *
 * Maneja las rutas para la interfaz gráfica de la aplicación.
 */
@Controller
public class WebController {

    /**
     * Página de bienvenida
     */
    @GetMapping("/")
    public String welcome() {
        return "index.html";
    }

    /**
     * Página principal (redirige a bienvenida)
     */
    @GetMapping("/app")
    public String app() {
        return "redirect:/";
    }

    /**
     * Dashboard principal
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard.html";
    }

    /**
     * Página de login
     */
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}