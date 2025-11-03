package app.clinic.application.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.infrastructure.dto.MedicalRecordDTO;

public class MedicalRecordMapper {

    public static MedicalRecordDTO toDTO(MedicalRecord medicalRecord, LocalDate filterDate) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(medicalRecord.getPatientIdentificationNumber());

        Map<String, Object> dayRecord = medicalRecord.getRecords().get(filterDate);

        if (dayRecord != null) {
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

        return dto;
    }
}