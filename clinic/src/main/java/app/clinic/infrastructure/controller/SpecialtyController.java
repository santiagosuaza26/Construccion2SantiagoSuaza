package app.clinic.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/specialties")
@Tag(name = "Specialties", description = "API para gestión de especialidades médicas")
public class SpecialtyController {

    // This would need a domain service for specialties
    // For now, providing a basic implementation with common medical specialties

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMERA', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Obtener todas las especialidades", description = "Obtiene una lista de todas las especialidades médicas disponibles")
    public ResponseEntity<List<String>> getAllSpecialties() {
        var specialties = java.util.Arrays.asList(
            "CARDIOLOGIA",
            "DERMATOLOGIA",
            "ENDOCRINOLOGIA",
            "GASTROENTEROLOGIA",
            "GINECOLOGIA",
            "HEMATOLOGIA",
            "INFECTOLOGIA",
            "MEDICINA_INTERNA",
            "NEFROLOGIA",
            "NEUROLOGIA",
            "OFTALMOLOGIA",
            "ORTOPEDIA",
            "PEDIATRIA",
            "PSIQUIATRIA",
            "RADIOLOGIA",
            "TRAUMATOLOGIA",
            "UROLOGIA"
        );
        return ResponseEntity.ok(specialties);
    }
}