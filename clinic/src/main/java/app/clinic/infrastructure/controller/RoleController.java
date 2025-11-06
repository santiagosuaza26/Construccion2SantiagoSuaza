package app.clinic.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.domain.model.valueobject.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "API para gestión de roles del sistema")
public class RoleController {

    @GetMapping
    @PreAuthorize("hasRole('RECURSOS_HUMANOS')")
    @Operation(summary = "Obtener todos los roles", description = "Obtiene una lista de todos los roles disponibles en el sistema")
    public ResponseEntity<List<String>> getAllRoles() {
        var roles = java.util.Arrays.stream(Role.values())
            .map(Role::toString)
            .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}