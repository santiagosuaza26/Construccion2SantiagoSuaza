package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddDiagnosticAidUseCase;
import app.clinic.application.usecase.AddMedicationUseCase;
import app.clinic.application.usecase.AddProcedureUseCase;
import app.clinic.application.usecase.UpdateDiagnosticAidUseCase;
import app.clinic.application.usecase.UpdateMedicationUseCase;
import app.clinic.application.usecase.UpdateProcedureUseCase;
import app.clinic.infrastructure.dto.DiagnosticAidDTO;
import app.clinic.infrastructure.dto.MedicationDTO;
import app.clinic.infrastructure.dto.ProcedureDTO;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final AddMedicationUseCase addMedicationUseCase;
    private final UpdateMedicationUseCase updateMedicationUseCase;
    private final AddProcedureUseCase addProcedureUseCase;
    private final UpdateProcedureUseCase updateProcedureUseCase;
    private final AddDiagnosticAidUseCase addDiagnosticAidUseCase;
    private final UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase;

    public InventoryController(AddMedicationUseCase addMedicationUseCase,
                             UpdateMedicationUseCase updateMedicationUseCase,
                             AddProcedureUseCase addProcedureUseCase,
                             UpdateProcedureUseCase updateProcedureUseCase,
                             AddDiagnosticAidUseCase addDiagnosticAidUseCase,
                             UpdateDiagnosticAidUseCase updateDiagnosticAidUseCase) {
        this.addMedicationUseCase = addMedicationUseCase;
        this.updateMedicationUseCase = updateMedicationUseCase;
        this.addProcedureUseCase = addProcedureUseCase;
        this.updateProcedureUseCase = updateProcedureUseCase;
        this.addDiagnosticAidUseCase = addDiagnosticAidUseCase;
        this.updateDiagnosticAidUseCase = updateDiagnosticAidUseCase;
    }

    // Medications
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