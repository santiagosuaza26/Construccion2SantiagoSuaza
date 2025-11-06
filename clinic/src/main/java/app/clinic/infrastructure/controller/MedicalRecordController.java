package app.clinic.infrastructure.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddMedicalRecordUseCase;
import app.clinic.application.usecase.GetMedicalRecordUseCase;
import app.clinic.application.usecase.UpdateMedicalRecordUseCase;
import app.clinic.domain.model.entities.DiagnosisEntry;
import app.clinic.domain.model.entities.DiagnosticAidEntry;
import app.clinic.domain.model.entities.MedicationEntry;
import app.clinic.domain.model.entities.ProcedureEntry;
import app.clinic.infrastructure.dto.MedicalRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/medical/records")
@Tag(name = "Medical Records", description = "API para gestión de registros médicos")
public class MedicalRecordController {

    private final GetMedicalRecordUseCase getMedicalRecordUseCase;
    private final AddMedicalRecordUseCase addMedicalRecordUseCase;
    private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;

    public MedicalRecordController(GetMedicalRecordUseCase getMedicalRecordUseCase,
                                  AddMedicalRecordUseCase addMedicalRecordUseCase,
                                  UpdateMedicalRecordUseCase updateMedicalRecordUseCase) {
        this.getMedicalRecordUseCase = getMedicalRecordUseCase;
        this.addMedicalRecordUseCase = addMedicalRecordUseCase;
        this.updateMedicalRecordUseCase = updateMedicalRecordUseCase;
    }

    @GetMapping("/{patientId}")
    @Operation(summary = "Obtener registro médico completo", description = "Obtiene el registro médico completo de un paciente")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMERA')")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable @NotBlank String patientId) {
        var medicalRecord = getMedicalRecordUseCase.execute(patientId);

        // Convert domain entity to DTO with proper mapping
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(patientId);

        // Map all entries from the medical record
        List<MedicalRecordDTO.RecordEntryDTO> recordEntries = new ArrayList<>();
        List<MedicalRecordDTO.MedicationEntryDTO> medicationEntries = new ArrayList<>();
        List<MedicalRecordDTO.ProcedureEntryDTO> procedureEntries = new ArrayList<>();
        List<MedicalRecordDTO.DiagnosticAidEntryDTO> diagnosticAidEntries = new ArrayList<>();

        // Process typed records
        for (var entry : medicalRecord.getTypedRecords().values()) {
            for (var medicalEntry : entry) {
                if (medicalEntry instanceof DiagnosisEntry) {
                    DiagnosisEntry de = (DiagnosisEntry) medicalEntry;
                    MedicalRecordDTO.RecordEntryDTO recordEntry = new MedicalRecordDTO.RecordEntryDTO();
                    recordEntry.setDate(de.getDate());
                    recordEntry.setDoctorId(de.getDoctorId());
                    recordEntry.setReason(""); // Not stored in DiagnosisEntry
                    recordEntry.setSymptoms(de.getSymptoms());
                    recordEntry.setDiagnosis(de.getDiagnosis());
                    recordEntries.add(recordEntry);
                } else if (medicalEntry instanceof MedicationEntry) {
                    MedicationEntry me = (MedicationEntry) medicalEntry;
                    MedicalRecordDTO.MedicationEntryDTO medicationEntry = new MedicalRecordDTO.MedicationEntryDTO();
                    medicationEntry.setDate(me.getDate());
                    medicationEntry.setOrderNumber(me.getOrderNumber());
                    medicationEntry.setMedicationId(me.getMedicationId());
                    medicationEntry.setDosage(me.getDosage());
                    medicationEntry.setDuration(me.getDuration());
                    medicationEntries.add(medicationEntry);
                } else if (medicalEntry instanceof ProcedureEntry) {
                    ProcedureEntry pe = (ProcedureEntry) medicalEntry;
                    MedicalRecordDTO.ProcedureEntryDTO procedureEntry = new MedicalRecordDTO.ProcedureEntryDTO();
                    procedureEntry.setDate(pe.getDate());
                    procedureEntry.setOrderNumber(pe.getOrderNumber());
                    procedureEntry.setProcedureId(pe.getProcedureId());
                    procedureEntry.setQuantity(pe.getQuantity());
                    procedureEntry.setFrequency(pe.getFrequency());
                    procedureEntry.setRequiresSpecialist(pe.isRequiresSpecialist());
                    procedureEntry.setSpecialistId(pe.getSpecialistId());
                    procedureEntries.add(procedureEntry);
                } else if (medicalEntry instanceof DiagnosticAidEntry) {
                    DiagnosticAidEntry dae = (DiagnosticAidEntry) medicalEntry;
                    MedicalRecordDTO.DiagnosticAidEntryDTO aidEntry = new MedicalRecordDTO.DiagnosticAidEntryDTO();
                    aidEntry.setDate(dae.getDate());
                    aidEntry.setOrderNumber(dae.getOrderNumber());
                    aidEntry.setDiagnosticAidId(dae.getDiagnosticAidId());
                    aidEntry.setQuantity(dae.getQuantity());
                    aidEntry.setRequiresSpecialist(dae.isRequiresSpecialist());
                    aidEntry.setSpecialistId(dae.getSpecialistId());
                    diagnosticAidEntries.add(aidEntry);
                }
            }
        }

        dto.setRecords(recordEntries);
        dto.setMedications(medicationEntries);
        dto.setProcedures(procedureEntries);
        dto.setDiagnosticAids(diagnosticAidEntries);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{patientId}")
    @Operation(summary = "Agregar entrada al registro médico", description = "Agrega una nueva entrada al registro médico de un paciente")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Void> addMedicalRecord(@PathVariable @NotBlank String patientId,
                                                @Valid @RequestBody AddMedicalRecordRequest request) {
        addMedicalRecordUseCase.execute(patientId, request.doctorId, request.reason, request.symptoms, request.diagnosis);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{patientId}")
    @Operation(summary = "Actualizar registro médico", description = "Actualiza el registro médico agregando una nueva entrada")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Void> updateMedicalRecord(@PathVariable @NotBlank String patientId,
                                                   @Valid @RequestBody UpdateMedicalRecordRequest request) {
        updateMedicalRecordUseCase.execute(patientId, request.doctorId, request.reason, request.symptoms, request.diagnosis);
        return ResponseEntity.ok().build();
    }

    // Request DTOs
    public static class AddMedicalRecordRequest {
        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotBlank(message = "La razón de la consulta no puede estar vacía")
        public String reason;

        @NotBlank(message = "Los síntomas no pueden estar vacíos")
        public String symptoms;

        @NotBlank(message = "El diagnóstico no puede estar vacío")
        public String diagnosis;
    }

    public static class UpdateMedicalRecordRequest {
        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotBlank(message = "La razón de la consulta no puede estar vacía")
        public String reason;

        @NotBlank(message = "Los síntomas no pueden estar vacíos")
        public String symptoms;

        @NotBlank(message = "El diagnóstico no puede estar vacío")
        public String diagnosis;
    }
}