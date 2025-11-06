package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.mapper.VitalSignsMapper;
import app.clinic.application.usecase.GetPatientUseCase;
import app.clinic.application.usecase.GetVitalSignsUseCase;
import app.clinic.application.usecase.RecordMedicationAdministrationUseCase;
import app.clinic.application.usecase.RecordProcedureRealizationUseCase;
import app.clinic.application.usecase.RecordVitalSignsUseCase;
import app.clinic.infrastructure.dto.PatientDTO;
import app.clinic.infrastructure.dto.VitalSignsDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/nurse")
public class NurseController {
    private final GetPatientUseCase getPatientUseCase;
    private final GetVitalSignsUseCase getVitalSignsUseCase;
    private final RecordVitalSignsUseCase recordVitalSignsUseCase;
    private final RecordMedicationAdministrationUseCase recordMedicationAdministrationUseCase;
    private final RecordProcedureRealizationUseCase recordProcedureRealizationUseCase;

    public NurseController(GetPatientUseCase getPatientUseCase,
                           GetVitalSignsUseCase getVitalSignsUseCase,
                           RecordVitalSignsUseCase recordVitalSignsUseCase,
                           RecordMedicationAdministrationUseCase recordMedicationAdministrationUseCase,
                           RecordProcedureRealizationUseCase recordProcedureRealizationUseCase) {
        this.getPatientUseCase = getPatientUseCase;
        this.getVitalSignsUseCase = getVitalSignsUseCase;
        this.recordVitalSignsUseCase = recordVitalSignsUseCase;
        this.recordMedicationAdministrationUseCase = recordMedicationAdministrationUseCase;
        this.recordProcedureRealizationUseCase = recordProcedureRealizationUseCase;
    }

    /**
     * Retrieves patient information by ID.
     * @param id The patient identification number
     * @return PatientDTO with patient information
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasRole('ENFERMERA')")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var patient = getPatientUseCase.execute(id);
            var dto = new PatientDTO(
                patient.getIdentificationNumber().getValue(),
                patient.getFullName(),
                patient.getDateOfBirth().getValue().toString(),
                patient.getGender().toString(),
                patient.getAddress().getValue(),
                patient.getPhone().getValue(),
                patient.getEmail().getValue(),
                patient.getEmergencyContact().getName(),
                patient.getEmergencyContact().getRelation(),
                patient.getEmergencyContact().getPhone().getValue(),
                patient.getInsurance() != null ? patient.getInsurance().getCompanyName() : "",
                patient.getInsurance() != null ? patient.getInsurance().getPolicyNumber() : "",
                patient.getInsurance() != null ? patient.getInsurance().isActive() : false,
                patient.getInsurance() != null ? patient.getInsurance().getValidityDate().toString() : ""
            );
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Records new vital signs for a patient.
     * @param request The vital signs data to record
     * @return VitalSignsDTO with the recorded data
     */
    @PostMapping("/vital-signs")
    @PreAuthorize("hasRole('ENFERMERA')")
    public ResponseEntity<VitalSignsDTO> recordVitalSigns(@Valid @RequestBody RecordVitalSignsRequest request) {
        if (request == null || request.patientId == null || request.patientId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var vitalSigns = recordVitalSignsUseCase.execute(
                request.patientId,
                request.bloodPressure,
                request.temperature,
                request.pulse,
                request.oxygenLevel
            );
            var dto = VitalSignsMapper.toDTO(vitalSigns);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Records medication administration for a patient.
     * @param request The medication administration data
     * @return HTTP 200 if successful
     */
    @PostMapping("/medication-administration")
    @PreAuthorize("hasRole('ENFERMERA')")
    public ResponseEntity<Void> recordMedicationAdministration(@RequestBody RecordMedicationAdministrationRequest request) {
        if (request == null || request.patientId == null || request.patientId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            recordMedicationAdministrationUseCase.execute(
                request.patientId,
                request.nurseId,
                request.orderNumber,
                request.item,
                request.administrationDetails
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Records procedure realization for a patient.
     * @param request The procedure realization data
     * @return HTTP 200 if successful
     */
    @PostMapping("/procedure-realization")
    @PreAuthorize("hasRole('ENFERMERA')")
    public ResponseEntity<Void> recordProcedureRealization(@RequestBody RecordProcedureRealizationRequest request) {
        if (request == null || request.patientId == null || request.patientId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            recordProcedureRealizationUseCase.execute(
                request.patientId,
                request.nurseId,
                request.orderNumber,
                request.item,
                request.realizationDetails
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves all vital signs records for a specific patient.
     * @param patientId The identification number of the patient
     * @return List of vital signs DTOs for the patient
     */
    @GetMapping("/vital-signs")
    @PreAuthorize("hasRole('ENFERMERA')")
    public ResponseEntity<java.util.List<VitalSignsDTO>> getVitalSigns(@RequestParam String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var vitalSignsList = getVitalSignsUseCase.execute(patientId);
            var dtoList = vitalSignsList.stream()
                .map(VitalSignsMapper::toDTO)
                .toList();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public static class RecordVitalSignsRequest {
        @jakarta.validation.constraints.NotBlank(message = "Patient ID is required")
        public String patientId;

        @jakarta.validation.constraints.NotBlank(message = "Blood pressure is required")
        public String bloodPressure;

        @jakarta.validation.constraints.Min(value = 30, message = "Temperature must be at least 30°C")
        @jakarta.validation.constraints.Max(value = 45, message = "Temperature must be at most 45°C")
        public double temperature;

        @jakarta.validation.constraints.Min(value = 40, message = "Pulse must be at least 40 bpm")
        @jakarta.validation.constraints.Max(value = 200, message = "Pulse must be at most 200 bpm")
        public int pulse;

        @jakarta.validation.constraints.Min(value = 70, message = "Oxygen level must be at least 70%")
        @jakarta.validation.constraints.Max(value = 100, message = "Oxygen level must be at most 100%")
        public int oxygenLevel;
    }

    public static class RecordMedicationAdministrationRequest {
        public String patientId;
        public String nurseId;
        public String orderNumber;
        public int item;
        public String administrationDetails;
    }

    public static class RecordProcedureRealizationRequest {
        public String patientId;
        public String nurseId;
        public String orderNumber;
        public int item;
        public String realizationDetails;
    }
}