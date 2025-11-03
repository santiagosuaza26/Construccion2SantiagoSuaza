package app.clinic.infrastructure.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.GetMedicalRecordUseCase;
import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.infrastructure.dto.MedicalRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/medical/queries")
@Tag(name = "Medical Queries", description = "API para consultas avanzadas de registros médicos")
public class MedicalQueryController {

    private final GetMedicalRecordUseCase getMedicalRecordUseCase;

    public MedicalQueryController(GetMedicalRecordUseCase getMedicalRecordUseCase) {
        this.getMedicalRecordUseCase = getMedicalRecordUseCase;
    }

    @GetMapping("/records/patient/{patientId}/date/{date}")
    @Operation(summary = "Obtener registros médicos por paciente y fecha",
               description = "Obtiene los registros médicos de un paciente filtrados por una fecha específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros encontrados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordsByPatientAndDate(
            @PathVariable @NotBlank String patientId,
            @PathVariable @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato YYYY-MM-DD") String date) {

        try {
            LocalDate filterDate = parseDate(date);
            MedicalRecord medicalRecord = getMedicalRecordUseCase.execute(patientId);

            if (medicalRecord == null) {
                return ResponseEntity.notFound().build();
            }

            MedicalRecordDTO dto = createMedicalRecordDTO(patientId, filterDate, medicalRecord);
            return ResponseEntity.ok(dto);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date, e);
        }
    }

    private MedicalRecordDTO createMedicalRecordDTO(String patientId, LocalDate filterDate, MedicalRecord medicalRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(patientId);

        Map<String, Object> dayRecord = Optional.ofNullable(medicalRecord.getRecords())
                .map(records -> records.get(filterDate))
                .orElse(null);

        if (dayRecord != null && !dayRecord.isEmpty()) {
            dto.setRecords(extractRecordEntries(filterDate, dayRecord));
            dto.setMedications(extractMedicationEntries(filterDate, dayRecord));
            dto.setProcedures(extractProcedureEntries(filterDate, dayRecord));
            dto.setDiagnosticAids(extractDiagnosticAidEntries(filterDate, dayRecord));
        } else {
            // No records for this date, return empty lists
            dto.setRecords(new ArrayList<>());
            dto.setMedications(new ArrayList<>());
            dto.setProcedures(new ArrayList<>());
            dto.setDiagnosticAids(new ArrayList<>());
        }

        return dto;
    }

    private List<MedicalRecordDTO.RecordEntryDTO> extractRecordEntries(LocalDate date, Map<String, Object> dayRecord) {
        List<MedicalRecordDTO.RecordEntryDTO> entries = new ArrayList<>();

        if (dayRecord.containsKey("doctorIdentificationNumber")) {
            MedicalRecordDTO.RecordEntryDTO entry = new MedicalRecordDTO.RecordEntryDTO();
            entry.setDate(date);
            entry.setDoctorId(getStringValue(dayRecord, "doctorIdentificationNumber"));
            entry.setReason(getStringValue(dayRecord, "reason"));
            entry.setSymptoms(getStringValue(dayRecord, "symptoms"));
            entry.setDiagnosis(getStringValue(dayRecord, "diagnosis"));
            entries.add(entry);
        }

        return entries;
    }

    private List<MedicalRecordDTO.MedicationEntryDTO> extractMedicationEntries(LocalDate date, Map<String, Object> dayRecord) {
        List<MedicalRecordDTO.MedicationEntryDTO> entries = new ArrayList<>();

        if (dayRecord.containsKey("medication")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> medication = (Map<String, Object>) dayRecord.get("medication");
            if (medication != null) {
                MedicalRecordDTO.MedicationEntryDTO entry = new MedicalRecordDTO.MedicationEntryDTO();
                entry.setDate(date);
                entry.setOrderNumber(getStringValue(medication, "orderNumber"));
                entry.setMedicationId(getStringValue(medication, "medicationId"));
                entry.setDosage(getStringValue(medication, "dosage"));
                entry.setDuration(getStringValue(medication, "duration"));
                entries.add(entry);
            }
        }

        return entries;
    }

    private List<MedicalRecordDTO.ProcedureEntryDTO> extractProcedureEntries(LocalDate date, Map<String, Object> dayRecord) {
        List<MedicalRecordDTO.ProcedureEntryDTO> entries = new ArrayList<>();

        if (dayRecord.containsKey("procedure")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> procedure = (Map<String, Object>) dayRecord.get("procedure");
            if (procedure != null) {
                MedicalRecordDTO.ProcedureEntryDTO entry = new MedicalRecordDTO.ProcedureEntryDTO();
                entry.setDate(date);
                entry.setOrderNumber(getStringValue(procedure, "orderNumber"));
                entry.setProcedureId(getStringValue(procedure, "procedureId"));
                entry.setQuantity(getStringValue(procedure, "quantity"));
                entry.setFrequency(getStringValue(procedure, "frequency"));
                entry.setRequiresSpecialist(getBooleanValue(procedure, "requiresSpecialist"));
                entry.setSpecialistId(getStringValue(procedure, "specialistId"));
                entries.add(entry);
            }
        }

        return entries;
    }

    private List<MedicalRecordDTO.DiagnosticAidEntryDTO> extractDiagnosticAidEntries(LocalDate date, Map<String, Object> dayRecord) {
        List<MedicalRecordDTO.DiagnosticAidEntryDTO> entries = new ArrayList<>();

        if (dayRecord.containsKey("diagnosticAid")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> aid = (Map<String, Object>) dayRecord.get("diagnosticAid");
            if (aid != null) {
                MedicalRecordDTO.DiagnosticAidEntryDTO entry = new MedicalRecordDTO.DiagnosticAidEntryDTO();
                entry.setDate(date);
                entry.setOrderNumber(getStringValue(aid, "orderNumber"));
                entry.setDiagnosticAidId(getStringValue(aid, "diagnosticAidId"));
                entry.setQuantity(getStringValue(aid, "quantity"));
                entry.setRequiresSpecialist(getBooleanValue(aid, "requiresSpecialist"));
                entry.setSpecialistId(getStringValue(aid, "specialistId"));
                entries.add(entry);
            }
        }

        return entries;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Boolean getBooleanValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }
}