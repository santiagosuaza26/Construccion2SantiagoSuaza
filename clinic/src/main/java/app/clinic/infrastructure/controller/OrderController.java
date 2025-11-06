package app.clinic.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/{orderId}/medications")
    @Operation(summary = "Agregar medicamentos a orden", description = "Agrega medicamentos a una orden existente")
    public ResponseEntity<Void> addMedicationsToOrder(@PathVariable String orderId, @RequestBody java.util.List<String> medicationIds) {
        // This would need a use case to add medications to an existing order
        // For now, return success
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/medications")
    @Operation(summary = "Obtener medicamentos de orden", description = "Obtiene la lista de medicamentos de una orden")
    public ResponseEntity<java.util.List<String>> getOrderMedications(@PathVariable String orderId) {
        // This would need a use case to get medications from an order
        // For now, return an empty list
        return ResponseEntity.ok(new java.util.ArrayList<>());
    }

    @PostMapping("/{orderId}/procedures")
    @Operation(summary = "Agregar procedimientos a orden", description = "Agrega procedimientos a una orden existente")
    public ResponseEntity<Void> addProceduresToOrder(@PathVariable String orderId, @RequestBody java.util.List<String> procedureIds) {
        // This would need a use case to add procedures to an existing order
        // For now, return success
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/procedures")
    @Operation(summary = "Obtener procedimientos de orden", description = "Obtiene la lista de procedimientos de una orden")
    public ResponseEntity<java.util.List<String>> getOrderProcedures(@PathVariable String orderId) {
        // This would need a use case to get procedures from an order
        // For now, return an empty list
        return ResponseEntity.ok(new java.util.ArrayList<>());
    }

    @PostMapping("/{orderId}/diagnostics")
    @Operation(summary = "Agregar diagnósticos a orden", description = "Agrega ayudas diagnósticas a una orden existente")
    public ResponseEntity<Void> addDiagnosticsToOrder(@PathVariable String orderId, @RequestBody java.util.List<String> diagnosticIds) {
        // This would need a use case to add diagnostics to an existing order
        // For now, return success
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/diagnostics")
    @Operation(summary = "Obtener diagnósticos de orden", description = "Obtiene la lista de ayudas diagnósticas de una orden")
    public ResponseEntity<java.util.List<String>> getOrderDiagnostics(@PathVariable String orderId) {
        // This would need a use case to get diagnostics from an order
        // For now, return an empty list
        return ResponseEntity.ok(new java.util.ArrayList<>());
    }

    @PostMapping
    @Operation(summary = "Crear nueva orden", description = "Crea una nueva orden general para un paciente")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        // This would need a general order creation use case
        // For now, return a placeholder response
        return ResponseEntity.ok(null);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Obtiene una lista de todas las órdenes")
    public ResponseEntity<java.util.List<Order>> getAllOrders() {
        // This would need a use case to get all orders
        // For now, return an empty list
        return ResponseEntity.ok(new java.util.ArrayList<>());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Obtiene los detalles de una orden específica")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        // This would need a use case to get order by ID
        // For now, return null
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden", description = "Elimina una orden específica")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        // This would need a use case to delete order
        // For now, return success
        return ResponseEntity.noContent().build();
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

    public static class CreateOrderRequest {
        @NotBlank(message = "El ID del paciente no puede estar vacío")
        public String patientId;

        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        public String diagnosis;
        public java.util.List<String> medications;
        public java.util.List<String> procedures;
        public java.util.List<String> diagnosticAids;
    }
}