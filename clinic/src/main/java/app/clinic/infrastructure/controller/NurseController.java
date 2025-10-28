package app.clinic.infrastructure.controller;

import app.clinic.application.usecase.GetPatientUseCase;
import app.clinic.application.usecase.RecordVitalSignsUseCase;
import app.clinic.application.usecase.RecordMedicationAdministrationUseCase;
import app.clinic.application.usecase.RecordProcedureRealizationUseCase;
import app.clinic.infrastructure.dto.PatientDTO;
import app.clinic.infrastructure.dto.VitalSignsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nurse")
public class NurseController {
    private final GetPatientUseCase getPatientUseCase;
    private final RecordVitalSignsUseCase recordVitalSignsUseCase;
    private final RecordMedicationAdministrationUseCase recordMedicationAdministrationUseCase;
    private final RecordProcedureRealizationUseCase recordProcedureRealizationUseCase;

    public NurseController(GetPatientUseCase getPatientUseCase,
                          RecordVitalSignsUseCase recordVitalSignsUseCase,
                          RecordMedicationAdministrationUseCase recordMedicationAdministrationUseCase,
                          RecordProcedureRealizationUseCase recordProcedureRealizationUseCase) {
        this.getPatientUseCase = getPatientUseCase;
        this.recordVitalSignsUseCase = recordVitalSignsUseCase;
        this.recordMedicationAdministrationUseCase = recordMedicationAdministrationUseCase;
        this.recordProcedureRealizationUseCase = recordProcedureRealizationUseCase;
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String id) {
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
    }

    @PostMapping("/vital-signs")
    public ResponseEntity<VitalSignsDTO> recordVitalSigns(@RequestBody RecordVitalSignsRequest request) {
        var vitalSigns = recordVitalSignsUseCase.execute(
            request.patientId,
            request.bloodPressure,
            request.temperature,
            request.pulse,
            request.oxygenLevel
        );
        var dto = new VitalSignsDTO(
            vitalSigns.getPatientIdentificationNumber(),
            vitalSigns.getBloodPressure(),
            vitalSigns.getTemperature(),
            vitalSigns.getPulse(),
            vitalSigns.getOxygenLevel(),
            vitalSigns.getDateTime().toString()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/medication-administration")
    public ResponseEntity<Void> recordMedicationAdministration(@RequestBody RecordMedicationAdministrationRequest request) {
        recordMedicationAdministrationUseCase.execute(
            request.patientId,
            request.nurseId,
            request.orderNumber,
            request.item,
            request.administrationDetails
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/procedure-realization")
    public ResponseEntity<Void> recordProcedureRealization(@RequestBody RecordProcedureRealizationRequest request) {
        recordProcedureRealizationUseCase.execute(
            request.patientId,
            request.nurseId,
            request.orderNumber,
            request.item,
            request.realizationDetails
        );
        return ResponseEntity.ok().build();
    }

    public static class RecordVitalSignsRequest {
        public String patientId;
        public String bloodPressure;
        public double temperature;
        public int pulse;
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