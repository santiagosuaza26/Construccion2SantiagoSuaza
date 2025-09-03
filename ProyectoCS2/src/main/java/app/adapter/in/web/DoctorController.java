package app.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.usecase.AddDiagnosticToOrderUseCase;
import app.application.usecase.AddMedicationToOrderUseCase;
import app.application.usecase.AddProcedureToOrderUseCase;
import app.application.usecase.AppendClinicalHistoryUseCase;
import app.application.usecase.CreateOrderUseCase;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderHeader;
import app.domain.model.ProcedureOrderItem;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final CreateOrderUseCase createOrderUseCase;
    private final AddMedicationToOrderUseCase addMedicationUseCase;
    private final AddProcedureToOrderUseCase addProcedureUseCase;
    private final AddDiagnosticToOrderUseCase addDiagnosticUseCase;
    private final AppendClinicalHistoryUseCase appendHistoryUseCase;

    public DoctorController(CreateOrderUseCase createOrderUseCase,
                        AddMedicationToOrderUseCase addMedicationUseCase,
                        AddProcedureToOrderUseCase addProcedureUseCase,
                        AddDiagnosticToOrderUseCase addDiagnosticUseCase,
                        AppendClinicalHistoryUseCase appendHistoryUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.addMedicationUseCase = addMedicationUseCase;
        this.addProcedureUseCase = addProcedureUseCase;
        this.addDiagnosticUseCase = addDiagnosticUseCase;
        this.appendHistoryUseCase = appendHistoryUseCase;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderHeader> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            OrderHeader header = createOrderUseCase.execute(request.orderNumber, request.patientId, request.doctorId);
            return ResponseEntity.ok(header);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/orders/{orderNumber}/medications")
    public ResponseEntity<Void> addMedications(@PathVariable String orderNumber, @RequestBody List<MedicationOrderItem> items) {
        try {
            addMedicationUseCase.execute(orderNumber, items);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/orders/{orderNumber}/procedures")
    public ResponseEntity<Void> addProcedures(@PathVariable String orderNumber, @RequestBody List<ProcedureOrderItem> items) {
        try {
            addProcedureUseCase.execute(orderNumber, items);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/orders/{orderNumber}/diagnostics")
    public ResponseEntity<Void> addDiagnostics(@PathVariable String orderNumber, @RequestBody List<DiagnosticOrderItem> items) {
        try {
            addDiagnosticUseCase.execute(orderNumber, items);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/patients/{patientId}/history")
    public ResponseEntity<Void> appendHistory(@PathVariable String patientId, @RequestBody ClinicalHistoryEntry entry) {
        appendHistoryUseCase.execute(patientId, entry);
        return ResponseEntity.ok().build();
    }

    // DTO interno para crear órdenes
    public static class CreateOrderRequest {
        public String orderNumber;
        public String patientId;
        public String doctorId;
    }
}