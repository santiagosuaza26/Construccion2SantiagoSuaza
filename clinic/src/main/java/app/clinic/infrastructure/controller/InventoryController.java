package app.clinic.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddDiagnosticAidUseCase;
import app.clinic.application.usecase.AddMedicationUseCase;
import app.clinic.application.usecase.AddProcedureUseCase;
import app.clinic.application.usecase.ListDiagnosticAidsUseCase;
import app.clinic.application.usecase.ListMedicationsUseCase;
import app.clinic.application.usecase.ListProceduresUseCase;
import app.clinic.application.usecase.UpdateDiagnosticAidUseCase;
import app.clinic.application.usecase.UpdateMedicationUseCase;
import app.clinic.application.usecase.UpdateProcedureUseCase;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.infrastructure.dto.DiagnosticAidDTO;
import app.clinic.infrastructure.dto.MedicationDTO;
import app.clinic.infrastructure.dto.ProcedureDTO;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final AddMedicationUseCase addMedicationUseCase;
    private final UpdateMedicationUseCase updateMedicationUseCase;
    private final ListMedicationsUseCase listMedicationsUseCase;
    private final AddProcedureUseCase addProcedureUseCase;
    private final UpdateProcedureUseCase updateProcedureUseCase;
    private final ListProceduresUseCase listProceduresUseCase;
    private final AddDiagnosticAidUseCase addDiagnosticAidUseCase;
    private final UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase;
    private final ListDiagnosticAidsUseCase listDiagnosticAidsUseCase;

    public InventoryController(AddMedicationUseCase addMedicationUseCase,
                             UpdateMedicationUseCase updateMedicationUseCase,
                             ListMedicationsUseCase listMedicationsUseCase,
                             AddProcedureUseCase addProcedureUseCase,
                             UpdateProcedureUseCase updateProcedureUseCase,
                             ListProceduresUseCase listProceduresUseCase,
                             AddDiagnosticAidUseCase addDiagnosticAidUseCase,
                             UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase,
                             ListDiagnosticAidsUseCase listDiagnosticAidsUseCase) {
        this.addMedicationUseCase = addMedicationUseCase;
        this.updateMedicationUseCase = updateMedicationUseCase;
        this.listMedicationsUseCase = listMedicationsUseCase;
        this.addProcedureUseCase = addProcedureUseCase;
        this.updateProcedureUseCase = updateProcedureUseCase;
        this.listProceduresUseCase = listProceduresUseCase;
        this.addDiagnosticAidUseCase = addDiagnosticAidUseCase;
        this.updateDiagnosticAidUseCase = updateDiagnosticAidUseCase;
        this.listDiagnosticAidsUseCase = listDiagnosticAidsUseCase;
    }

    // Medications
    @GetMapping("/medications")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<MedicationDTO>> listMedications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleString = authentication.getAuthorities().iterator().next().getAuthority();
        if (roleString.startsWith("ROLE_")) {
            roleString = roleString.substring(5);
        }
        Role userRole = Role.valueOf(roleString);

        var medications = listMedicationsUseCase.execute(userRole);

        var medicationDTOs = medications.stream().map(medication -> new MedicationDTO(
            medication.getId().getValue(),
            medication.getName(),
            medication.getCost(),
            medication.isRequiresSpecialist(),
            medication.getSpecialistType().getValue()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(medicationDTOs);
    }

    @PostMapping("/medications")
    public ResponseEntity<MedicationDTO> addMedication(@RequestBody AddMedicationRequest request) {
        // Validar entrada
        if (request.id == null || request.id.trim().isEmpty()) {
            throw new IllegalArgumentException("Medication ID is required");
        }
        if (request.name == null || request.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Medication name is required");
        }
        if (request.cost < 0) {
            throw new IllegalArgumentException("Cost must be non-negative");
        }
        if (request.specialistType == null || request.specialistType.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialist type is required");
        }

        var medication = addMedicationUseCase.execute(
            null, request.id, request.name, request.cost, request.requiresSpecialist, request.specialistType
        );

        var dto = new MedicationDTO(
            medication.getId().getValue(),
            medication.getName(),
            medication.getCost(),
            medication.isRequiresSpecialist(),
            medication.getSpecialistType().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/medications/{id}")
    public ResponseEntity<MedicationDTO> updateMedication(@PathVariable String id, @RequestBody UpdateMedicationRequest request) {
        // Validar entrada
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Medication ID is required");
        }
        if (request.name == null || request.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Medication name is required");
        }
        if (request.cost < 0) {
            throw new IllegalArgumentException("Cost must be non-negative");
        }
        if (request.specialistType == null || request.specialistType.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialist type is required");
        }

        var medication = updateMedicationUseCase.execute(id, request.name, request.cost, request.requiresSpecialist, request.specialistType);

        var dto = new MedicationDTO(
            medication.getId().getValue(),
            medication.getName(),
            medication.getCost(),
            medication.isRequiresSpecialist(),
            medication.getSpecialistType().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    // Procedures
    @GetMapping("/procedures")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<ProcedureDTO>> listProcedures() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleString = authentication.getAuthorities().iterator().next().getAuthority();
        if (roleString.startsWith("ROLE_")) {
            roleString = roleString.substring(5);
        }
        Role userRole = Role.valueOf(roleString);

        var procedures = listProceduresUseCase.execute(userRole);

        var procedureDTOs = procedures.stream().map(procedure -> new ProcedureDTO(
            procedure.getId().getValue(),
            procedure.getName(),
            procedure.getCost(),
            procedure.isRequiresSpecialist(),
            procedure.getSpecialistType().getValue()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(procedureDTOs);
    }

    @PostMapping("/procedures")
    public ResponseEntity<ProcedureDTO> addProcedure(@RequestBody AddProcedureRequest request) {
        var procedure = addProcedureUseCase.execute(
            null, request.id, request.name, request.cost, request.requiresSpecialist, request.specialistType
        );

        var dto = new ProcedureDTO(
            procedure.getId().getValue(),
            procedure.getName(),
            procedure.getCost(),
            procedure.isRequiresSpecialist(),
            procedure.getSpecialistType().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/procedures/{id}")
    public ResponseEntity<ProcedureDTO> updateProcedure(@PathVariable String id, @RequestBody UpdateProcedureRequest request) {
        var procedure = updateProcedureUseCase.execute(id, request.name, request.cost, request.requiresSpecialist, request.specialistType);

        var dto = new ProcedureDTO(
            procedure.getId().getValue(),
            procedure.getName(),
            procedure.getCost(),
            procedure.isRequiresSpecialist(),
            procedure.getSpecialistType().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    // Diagnostic Aids
    @GetMapping("/diagnostic-aids")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<DiagnosticAidDTO>> listDiagnosticAids() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleString = authentication.getAuthorities().iterator().next().getAuthority();
        if (roleString.startsWith("ROLE_")) {
            roleString = roleString.substring(5);
        }
        Role userRole = Role.valueOf(roleString);

        var diagnosticAids = listDiagnosticAidsUseCase.execute(userRole);

        var diagnosticAidDTOs = diagnosticAids.stream().map(diagnosticAid -> new DiagnosticAidDTO(
            diagnosticAid.getId().getValue(),
            diagnosticAid.getName(),
            diagnosticAid.getCost(),
            diagnosticAid.isRequiresSpecialist(),
            diagnosticAid.getSpecialistType().getValue()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(diagnosticAidDTOs);
    }

    @PostMapping("/diagnostic-aids")
    public ResponseEntity<DiagnosticAidDTO> addDiagnosticAid(@RequestBody AddDiagnosticAidRequest request) {
        var diagnosticAid = addDiagnosticAidUseCase.execute(
            null, request.id, request.name, request.cost, request.requiresSpecialist, request.specialistType
        );

        var dto = new DiagnosticAidDTO(
            diagnosticAid.getId().getValue(),
            diagnosticAid.getName(),
            diagnosticAid.getCost(),
            diagnosticAid.isRequiresSpecialist(),
            diagnosticAid.getSpecialistType().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/diagnostic-aids/{id}")
    public ResponseEntity<DiagnosticAidDTO> updateDiagnosticAid(@PathVariable String id, @RequestBody UpdateDiagnosticAidRequest request) {
        var diagnosticAid = updateDiagnosticAidUseCase.execute(id, request.name, request.cost, request.requiresSpecialist, request.specialistType);

        var dto = new DiagnosticAidDTO(
            diagnosticAid.getId().getValue(),
            diagnosticAid.getName(),
            diagnosticAid.getCost(),
            diagnosticAid.isRequiresSpecialist(),
            diagnosticAid.getSpecialistType().getValue()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable String id) {
        // Note: Delete functionality would need to be implemented in the domain service
        // For now, return success
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/procedures/{id}")
    public ResponseEntity<Void> deleteProcedure(@PathVariable String id) {
        // Note: Delete functionality would need to be implemented in the domain service
        // For now, return success
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/diagnostic-aids/{id}")
    public ResponseEntity<Void> deleteDiagnosticAid(@PathVariable String id) {
        // Note: Delete functionality would need to be implemented in the domain service
        // For now, return success
        return ResponseEntity.noContent().build();
    }

    // Request DTOs
    public static class AddMedicationRequest {
        public String id;
        public String name;
        public double cost;
        public boolean requiresSpecialist;
        public String specialistType;
    }

    public static class UpdateMedicationRequest {
        public String name;
        public double cost;
        public boolean requiresSpecialist;
        public String specialistType;
    }

    public static class AddProcedureRequest {
        public String id;
        public String name;
        public double cost;
        public boolean requiresSpecialist;
        public String specialistType;
    }

    public static class UpdateProcedureRequest {
        public String name;
        public double cost;
        public boolean requiresSpecialist;
        public String specialistType;
    }

    public static class AddDiagnosticAidRequest {
        public String id;
        public String name;
        public double cost;
        public boolean requiresSpecialist;
        public String specialistType;
    }

    public static class UpdateDiagnosticAidRequest {
        public String name;
        public double cost;
        public boolean requiresSpecialist;
        public String specialistType;
    }
}