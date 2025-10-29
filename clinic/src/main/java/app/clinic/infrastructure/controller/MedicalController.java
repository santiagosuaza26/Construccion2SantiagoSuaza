package app.clinic.infrastructure.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import app.clinic.domain.model.entities.MedicalRecord;
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

    // Get medical records by patient and date
    @GetMapping("/records/patient/{patientId}/date/{date}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordsByPatientAndDate(@PathVariable String patientId, @PathVariable String date) {
        try {
            LocalDate filterDate = LocalDate.parse(date);
            MedicalRecord medicalRecord = getMedicalRecordUseCase.execute(patientId);

            // Filter records by the specified date
            Map<String, Object> dayRecord = medicalRecord.getRecords().get(filterDate);

            MedicalRecordDTO dto = new MedicalRecordDTO();
            dto.setPatientId(patientId);

            if (dayRecord != null) {
                // Map the filtered record to DTO
                List<MedicalRecordDTO.RecordEntryDTO> recordEntries = new ArrayList<>();
                List<MedicalRecordDTO.MedicationEntryDTO> medicationEntries = new ArrayList<>();
                List<MedicalRecordDTO.ProcedureEntryDTO> procedureEntries = new ArrayList<>();
                List<MedicalRecordDTO.DiagnosticAidEntryDTO> diagnosticAidEntries = new ArrayList<>();

                // Check for basic record entry
                if (dayRecord.containsKey("doctorIdentificationNumber")) {
                    MedicalRecordDTO.RecordEntryDTO recordEntry = new MedicalRecordDTO.RecordEntryDTO();
                    recordEntry.setDate(filterDate);
                    recordEntry.setDoctorId((String) dayRecord.get("doctorIdentificationNumber"));
                    recordEntry.setReason((String) dayRecord.get("reason"));
                    recordEntry.setSymptoms((String) dayRecord.get("symptoms"));
                    recordEntry.setDiagnosis((String) dayRecord.get("diagnosis"));
                    recordEntries.add(recordEntry);
                }

                // Check for medication
                if (dayRecord.containsKey("medication")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> medication = (Map<String, Object>) dayRecord.get("medication");
                    MedicalRecordDTO.MedicationEntryDTO medicationEntry = new MedicalRecordDTO.MedicationEntryDTO();
                    medicationEntry.setDate(filterDate);
                    medicationEntry.setOrderNumber((String) medication.get("orderNumber"));
                    medicationEntry.setMedicationId((String) medication.get("medicationId"));
                    medicationEntry.setDosage((String) medication.get("dosage"));
                    medicationEntry.setDuration((String) medication.get("duration"));
                    medicationEntries.add(medicationEntry);
                }

                // Check for procedure
                if (dayRecord.containsKey("procedure")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> procedure = (Map<String, Object>) dayRecord.get("procedure");
                    MedicalRecordDTO.ProcedureEntryDTO procedureEntry = new MedicalRecordDTO.ProcedureEntryDTO();
                    procedureEntry.setDate(filterDate);
                    procedureEntry.setOrderNumber((String) procedure.get("orderNumber"));
                    procedureEntry.setProcedureId((String) procedure.get("procedureId"));
                    procedureEntry.setQuantity((String) procedure.get("quantity"));
                    procedureEntry.setFrequency((String) procedure.get("frequency"));
                    procedureEntry.setRequiresSpecialist((Boolean) procedure.get("requiresSpecialist"));
                    procedureEntry.setSpecialistId((String) procedure.get("specialistId"));
                    procedureEntries.add(procedureEntry);
                }

                // Check for diagnostic aid
                if (dayRecord.containsKey("diagnosticAid")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> aid = (Map<String, Object>) dayRecord.get("diagnosticAid");
                    MedicalRecordDTO.DiagnosticAidEntryDTO aidEntry = new MedicalRecordDTO.DiagnosticAidEntryDTO();
                    aidEntry.setDate(filterDate);
                    aidEntry.setOrderNumber((String) aid.get("orderNumber"));
                    aidEntry.setDiagnosticAidId((String) aid.get("diagnosticAidId"));
                    aidEntry.setQuantity((String) aid.get("quantity"));
                    aidEntry.setRequiresSpecialist((Boolean) aid.get("requiresSpecialist"));
                    aidEntry.setSpecialistId((String) aid.get("specialistId"));
                    diagnosticAidEntries.add(aidEntry);
                }

                dto.setRecords(recordEntries);
                dto.setMedications(medicationEntries);
                dto.setProcedures(procedureEntries);
                dto.setDiagnosticAids(diagnosticAidEntries);
            } else {
                // No records for this date, return empty lists
                dto.setRecords(new ArrayList<>());
                dto.setMedications(new ArrayList<>());
                dto.setProcedures(new ArrayList<>());
                dto.setDiagnosticAids(new ArrayList<>());
            }

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // Handle invalid date format or other errors
            return ResponseEntity.badRequest().build();
        }
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