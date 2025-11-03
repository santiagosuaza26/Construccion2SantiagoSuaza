package app.clinic.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddHospitalizationOrderUseCase;
import app.clinic.application.usecase.CreateDiagnosticAidOrderUseCase;
import app.clinic.application.usecase.CreateMedicationOrderUseCase;
import app.clinic.application.usecase.CreateProcedureOrderUseCase;
import app.clinic.domain.model.entities.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/api/medical/orders")
@Tag(name = "Medical Orders", description = "API para gestión de órdenes médicas")
public class OrderController {

    private final CreateMedicationOrderUseCase createMedicationOrderUseCase;
    private final CreateProcedureOrderUseCase createProcedureOrderUseCase;
    private final CreateDiagnosticAidOrderUseCase createDiagnosticAidOrderUseCase;
    private final AddHospitalizationOrderUseCase addHospitalizationOrderUseCase;

    public OrderController(CreateMedicationOrderUseCase createMedicationOrderUseCase,
                          CreateProcedureOrderUseCase createProcedureOrderUseCase,
                          CreateDiagnosticAidOrderUseCase createDiagnosticAidOrderUseCase,
                          AddHospitalizationOrderUseCase addHospitalizationOrderUseCase) {
        this.createMedicationOrderUseCase = createMedicationOrderUseCase;
        this.createProcedureOrderUseCase = createProcedureOrderUseCase;
        this.createDiagnosticAidOrderUseCase = createDiagnosticAidOrderUseCase;
        this.addHospitalizationOrderUseCase = addHospitalizationOrderUseCase;
    }

    @PostMapping("/medications")
    @Operation(summary = "Crear orden de medicamentos", description = "Crea una nueva orden de medicamentos para un paciente")
    public ResponseEntity<Order> createMedicationOrder(@Valid @RequestBody CreateMedicationOrderRequest request) {
        Order order = createMedicationOrderUseCase.execute(request.patientId, request.doctorId, request.medications);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/procedures")
    @Operation(summary = "Crear orden de procedimientos", description = "Crea una nueva orden de procedimientos para un paciente")
    public ResponseEntity<Order> createProcedureOrder(@Valid @RequestBody CreateProcedureOrderRequest request) {
        Order order = createProcedureOrderUseCase.execute(request.patientId, request.doctorId, request.procedures);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/diagnostic-aids")
    @Operation(summary = "Crear orden de ayudas diagnósticas", description = "Crea una nueva orden de ayudas diagnósticas para un paciente")
    public ResponseEntity<Order> createDiagnosticAidOrder(@Valid @RequestBody CreateDiagnosticAidOrderRequest request) {
        Order order = createDiagnosticAidOrderUseCase.execute(request.patientId, request.doctorId, request.diagnosticAids);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/hospitalization")
    @Operation(summary = "Agregar orden de hospitalización", description = "Agrega una orden de hospitalización para un paciente")
    public ResponseEntity<Order> addHospitalizationOrder(@Valid @RequestBody AddHospitalizationRequest request) {
        Order order = addHospitalizationOrderUseCase.execute(request.patientId, request.doctorId, request.details);
        return ResponseEntity.ok(order);
    }

    // Request DTOs
    public static class CreateMedicationOrderRequest {
        @NotBlank(message = "El ID del paciente no puede estar vacío")
        public String patientId;

        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotEmpty(message = "La lista de medicamentos no puede estar vacía")
        public List<app.clinic.domain.model.entities.MedicationOrder> medications;
    }

    public static class CreateProcedureOrderRequest {
        @NotBlank(message = "El ID del paciente no puede estar vacío")
        public String patientId;

        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotEmpty(message = "La lista de procedimientos no puede estar vacía")
        public List<app.clinic.domain.model.entities.ProcedureOrder> procedures;
    }

    public static class CreateDiagnosticAidOrderRequest {
        @NotBlank(message = "El ID del paciente no puede estar vacío")
        public String patientId;

        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotEmpty(message = "La lista de ayudas diagnósticas no puede estar vacía")
        public List<app.clinic.domain.model.entities.DiagnosticAidOrder> diagnosticAids;
    }

    public static class AddHospitalizationRequest {
        @NotBlank(message = "El ID del paciente no puede estar vacío")
        public String patientId;

        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotBlank(message = "Los detalles de hospitalización no pueden estar vacíos")
        public String details;
    }
}