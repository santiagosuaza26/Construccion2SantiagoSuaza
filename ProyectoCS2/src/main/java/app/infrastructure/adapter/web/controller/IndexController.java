package app.infrastructure.adapter.web.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir la interfaz gráfica principal
 *
 * Este controlador maneja las rutas raíz y de interfaz para asegurar que
 * la interfaz gráfica se sirva correctamente desde cualquier punto de entrada.
 * - Ruta raíz ("/") -> sirve index.html directamente
 * - Ruta interfaz ("/interface/app") -> sirve index.html directamente
 */
@Controller
public class IndexController {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Sirve la interfaz gráfica principal desde la raíz
     */
    @GetMapping("/")
    public ResponseEntity<String> root() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:GUI/templates/index.html");
        String content = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }

    /**
     * Sirve la interfaz gráfica principal desde /interface/app
     */
    @GetMapping("/interface/app")
    public ResponseEntity<String> app() throws IOException {
        return root();
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