package app.clinic.infrastructure.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {
    private String patientId;
    private List<RecordEntryDTO> records;
    private List<MedicationEntryDTO> medications;
    private List<ProcedureEntryDTO> procedures;
    private List<DiagnosticAidEntryDTO> diagnosticAids;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordEntryDTO {
        private LocalDate date;
        private String doctorId;
        private String reason;
        private String symptoms;
        private String diagnosis;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicationEntryDTO {
        private LocalDate date;
        private String orderNumber;
        private String medicationId;
        private String dosage;
        private String duration;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcedureEntryDTO {
        private LocalDate date;
        private String orderNumber;
        private String procedureId;
        private String quantity;
        private String frequency;
        private boolean requiresSpecialist;
        private String specialistId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagnosticAidEntryDTO {
        private LocalDate date;
        private String orderNumber;
        private String diagnosticAidId;
        private String quantity;
        private boolean requiresSpecialist;
        private String specialistId;
    }
}