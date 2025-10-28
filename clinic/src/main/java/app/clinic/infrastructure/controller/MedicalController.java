package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddDiagnosticFollowupUseCase;
import app.clinic.application.usecase.AddHospitalizationOrderUseCase;
import app.clinic.application.usecase.AddMedicalRecordUseCase;
import app.clinic.application.usecase.CreateDiagnosticAidOrderUseCase;
import app.clinic.application.usecase.CreateMedicationOrderUseCase;
import app.clinic.application.usecase.CreateProcedureOrderUseCase;
import app.clinic.application.usecase.GetMedicalRecordUseCase;
import app.clinic.application.usecase.UpdateMedicalRecordUseCase;
import app.clinic.domain.model.entities.Order;
import app.clinic.infrastructure.dto.MedicalRecordDTO;

@RestController
@RequestMapping("/api/medical")
public class MedicalController {
    private final GetMedicalRecordUseCase getMedicalRecordUseCase;
    private final AddMedicalRecordUseCase addMedicalRecordUseCase;
    private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;
    private final CreateMedicationOrderUseCase createMedicationOrderUseCase;
    private final CreateProcedureOrderUseCase createProcedureOrderUseCase;
    private final CreateDiagnosticAidOrderUseCase createDiagnosticAidOrderUseCase;
    private final AddHospitalizationOrderUseCase addHospitalizationOrderUseCase;
    private final AddDiagnosticFollowupUseCase addDiagnosticFollowupUseCase;

    public MedicalController(GetMedicalRecordUseCase getMedicalRecordUseCase,
                           AddMedicalRecordUseCase addMedicalRecordUseCase,
                           UpdateMedicalRecordUseCase updateMedicalRecordUseCase,
                           CreateMedicationOrderUseCase createMedicationOrderUseCase,
                           CreateProcedureOrderUseCase createProcedureOrderUseCase,
                           CreateDiagnosticAidOrderUseCase createDiagnosticAidOrderUseCase,
                           AddHospitalizationOrderUseCase addHospitalizationOrderUseCase,
                           AddDiagnosticFollowupUseCase addDiagnosticFollowupUseCase) {
        this.getMedicalRecordUseCase = getMedicalRecordUseCase;
        this.addMedicalRecordUseCase = addMedicalRecordUseCase;
        this.updateMedicalRecordUseCase = updateMedicalRecordUseCase;
        this.createMedicationOrderUseCase = createMedicationOrderUseCase;
        this.createProcedureOrderUseCase = createProcedureOrderUseCase;
        this.createDiagnosticAidOrderUseCase = createDiagnosticAidOrderUseCase;
        this.addHospitalizationOrderUseCase = addHospitalizationOrderUseCase;
        this.addDiagnosticFollowupUseCase = addDiagnosticFollowupUseCase;
    }

    // Get complete medical record
    @GetMapping("/records/{patientId}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable String patientId) {
        getMedicalRecordUseCase.execute(patientId);

        // Convert domain entity to DTO (simplified for demo)
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(patientId);
        // Note: Full conversion would require mapping all record entries

        return ResponseEntity.ok(dto);
    }

    // Add new medical record entry
    @PostMapping("/records/{patientId}")
    public ResponseEntity<Void> addMedicalRecord(@PathVariable String patientId,
                                               @RequestBody AddMedicalRecordRequest request) {
        addMedicalRecordUseCase.execute(patientId, request.doctorId, request.reason, request.symptoms, request.diagnosis);
        return ResponseEntity.ok().build();
    }

    // Update medical record (adds new entry)
    @PutMapping("/records/{patientId}")
    public ResponseEntity<Void> updateMedicalRecord(@PathVariable String patientId,
                                                  @RequestBody UpdateMedicalRecordRequest request) {
        updateMedicalRecordUseCase.execute(patientId, request.doctorId, request.reason, request.symptoms, request.diagnosis);
        return ResponseEntity.ok().build();
    }

    // Create medication order
    @PostMapping("/orders/medications")
    public ResponseEntity<Order> createMedicationOrder(@RequestBody CreateMedicationOrderRequest request) {
        Order order = createMedicationOrderUseCase.execute(request.patientId, request.doctorId, request.medications);
        return ResponseEntity.ok(order);
    }

    // Create procedure order
    @PostMapping("/orders/procedures")
    public ResponseEntity<Order> createProcedureOrder(@RequestBody CreateProcedureOrderRequest request) {
        Order order = createProcedureOrderUseCase.execute(request.patientId, request.doctorId, request.procedures);
        return ResponseEntity.ok(order);
    }

    // Create diagnostic aid order
    @PostMapping("/orders/diagnostic-aids")
    public ResponseEntity<Order> createDiagnosticAidOrder(@RequestBody CreateDiagnosticAidOrderRequest request) {
        Order order = createDiagnosticAidOrderUseCase.execute(request.patientId, request.doctorId, request.diagnosticAids);
        return ResponseEntity.ok(order);
    }

    // Add hospitalization order
    @PostMapping("/orders/hospitalization")
    public ResponseEntity<Order> addHospitalizationOrder(@RequestBody AddHospitalizationRequest request) {
        Order order = addHospitalizationOrderUseCase.execute(request.patientId, request.doctorId, request.details);
        return ResponseEntity.ok(order);
    }

    // Add diagnostic follow-up
    @PostMapping("/followup/diagnostic")
    public ResponseEntity<Void> addDiagnosticFollowup(@RequestBody AddDiagnosticFollowupRequest request) {
        addDiagnosticFollowupUseCase.execute(request.patientId, request.diagnosis, request.orderNumber,
                                           request.medicationId, request.dosage, request.duration);
        return ResponseEntity.ok().build();
    }

    // Request DTOs
    public static class AddMedicalRecordRequest {
        public String doctorId;
        public String reason;
        public String symptoms;
        public String diagnosis;
    }

    public static class UpdateMedicalRecordRequest {
        public String doctorId;
        public String reason;
        public String symptoms;
        public String diagnosis;
    }

    public static class CreateMedicationOrderRequest {
        public String patientId;
        public String doctorId;
        public java.util.List<app.clinic.domain.model.entities.MedicationOrder> medications;
    }

    public static class CreateProcedureOrderRequest {
        public String patientId;
        public String doctorId;
        public java.util.List<app.clinic.domain.model.entities.ProcedureOrder> procedures;
    }

    public static class CreateDiagnosticAidOrderRequest {
        public String patientId;
        public String doctorId;
        public java.util.List<app.clinic.domain.model.entities.DiagnosticAidOrder> diagnosticAids;
    }

    public static class AddHospitalizationRequest {
        public String patientId;
        public String doctorId;
        public String details;
    }

    public static class AddDiagnosticFollowupRequest {
        public String patientId;
        public String diagnosis;
        public String orderNumber;
        public String medicationId;
        public String dosage;
        public String duration;
    }
}