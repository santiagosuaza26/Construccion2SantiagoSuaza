package app.clinic.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.clinic.application.dto.medical.CreateMedicalRecordDTO;
import app.clinic.application.dto.medical.DiagnosticAidRecordDTO;
import app.clinic.application.dto.medical.MedicalRecordDTO;
import app.clinic.application.dto.medical.MedicalRecordDataDTO;
import app.clinic.application.dto.medical.MedicalRecordEntryDTO;
import app.clinic.application.dto.medical.MedicationRecordDTO;
import app.clinic.application.dto.medical.ProcedureRecordDTO;
import app.clinic.domain.model.ConsultationReason;
import app.clinic.domain.model.Diagnosis;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientRecord;
import app.clinic.domain.model.PatientRecordData;
import app.clinic.domain.model.PatientRecordDate;
import app.clinic.domain.model.PatientRecordEntry;
import app.clinic.domain.model.PatientRecordEntryId;
import app.clinic.domain.model.PatientRecordEntryKey;
import app.clinic.domain.model.Symptoms;

/**
 * Mapper class for converting between Medical Record DTOs and domain entities.
 */
public class MedicalRecordMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Converts a CreateMedicalRecordDTO to PatientRecordEntry domain entity.
     */
    public static PatientRecordEntry toPatientRecordEntryDomainEntity(CreateMedicalRecordDTO createMedicalRecordDTO) {
        // Create additional data map with medications, procedures, and diagnostic aids
        Map<String, Object> additionalData = new HashMap<>();

        if (createMedicalRecordDTO.getMedications() != null) {
            additionalData.put("medications", createMedicalRecordDTO.getMedications());
        }
        if (createMedicalRecordDTO.getProcedures() != null) {
            additionalData.put("procedures", createMedicalRecordDTO.getProcedures());
        }
        if (createMedicalRecordDTO.getDiagnosticAids() != null) {
            additionalData.put("diagnosticAids", createMedicalRecordDTO.getDiagnosticAids());
        }

        PatientRecordData data = additionalData.isEmpty() ?
            PatientRecordData.empty() : PatientRecordData.of(additionalData);

        return PatientRecordEntry.of(
            DoctorCedula.of(createMedicalRecordDTO.getDoctorCedula()),
            ConsultationReason.of(createMedicalRecordDTO.getConsultationReason()),
            Symptoms.of(createMedicalRecordDTO.getSymptoms()),
            Diagnosis.of(createMedicalRecordDTO.getDiagnosis()),
            data
        );
    }

    /**
     * Converts a PatientRecord domain entity to MedicalRecordDTO.
     */
    public static MedicalRecordDTO toMedicalRecordDTO(PatientRecord patientRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientCedula(""); // Not available in PatientRecord

        Map<String, MedicalRecordEntryDTO> recordsMap = new HashMap<>();

        // Convert each record entry to DTO
        patientRecord.getRecords().forEach((date, entry) -> {
            String dateKey = date.getValue().format(DATE_FORMATTER);
            recordsMap.put(dateKey, convertToMedicalRecordEntryDTO(entry));
        });

        dto.setRecords(recordsMap);
        return dto;
    }

    /**
     * Converts a PatientRecordEntry domain entity to MedicalRecordEntryDTO.
     */
    public static MedicalRecordEntryDTO toMedicalRecordEntryDTO(PatientRecordEntry patientRecordEntry) {
        MedicalRecordEntryDTO dto = new MedicalRecordEntryDTO();
        dto.setDoctorCedula(patientRecordEntry.getDoctorCedula().getValue());
        dto.setConsultationReason(patientRecordEntry.getConsultationReason().getValue());
        dto.setSymptoms(patientRecordEntry.getSymptoms().getValue());
        dto.setDiagnosis(patientRecordEntry.getDiagnosis().getValue());

        // Convert additional data to MedicalRecordDataDTO
        dto.setData(convertToMedicalRecordDataDTO(patientRecordEntry.getData()));
        return dto;
    }

    /**
     * Converts a PatientRecordData domain entity to MedicalRecordDataDTO.
     */
    public static MedicalRecordDataDTO toMedicalRecordDataDTO(PatientRecordData patientRecordData) {
        MedicalRecordDataDTO dto = new MedicalRecordDataDTO();
        dto.setAdditionalData(patientRecordData.getData());
        return dto;
    }

    /**
     * Converts a MedicalRecordDataDTO to PatientRecordData domain entity.
     */
    public static PatientRecordData toPatientRecordDataDomainEntity(MedicalRecordDataDTO medicalRecordDataDTO) {
        return PatientRecordData.of(medicalRecordDataDTO.getAdditionalData());
    }

    /**
     * Converts a PatientRecordEntry domain entity to MedicalRecordEntryDTO.
     */
    public static MedicalRecordEntryDTO convertToMedicalRecordEntryDTO(PatientRecordEntry entry) {
        MedicalRecordEntryDTO dto = new MedicalRecordEntryDTO();
        dto.setDoctorCedula(entry.getDoctorCedula().getValue());
        dto.setConsultationReason(entry.getConsultationReason().getValue());
        dto.setSymptoms(entry.getSymptoms().getValue());
        dto.setDiagnosis(entry.getDiagnosis().getValue());
        dto.setData(convertToMedicalRecordDataDTO(entry.getData()));
        return dto;
    }

    /**
     * Converts a PatientRecordData domain entity to MedicalRecordDataDTO.
     */
    public static MedicalRecordDataDTO convertToMedicalRecordDataDTO(PatientRecordData data) {
        MedicalRecordDataDTO dto = new MedicalRecordDataDTO();
        dto.setAdditionalData(data.getData());
        return dto;
    }

    /**
     * Converts a list of PatientRecord entities to a map of MedicalRecordDTOs.
     */
    public static Map<String, MedicalRecordDTO> toMedicalRecordDTOMap(Map<PatientCedula, PatientRecord> patientRecords) {
        Map<String, MedicalRecordDTO> dtoMap = new HashMap<>();

        patientRecords.forEach((patientCedula, patientRecord) -> {
            MedicalRecordDTO dto = toMedicalRecordDTO(patientRecord);
            dto.setPatientCedula(patientCedula.getValue());
            dtoMap.put(patientCedula.getValue(), dto);
        });

        return dtoMap;
    }

    /**
     * Converts a list of MedicationRecordDTOs to a map for storage in PatientRecordData.
     */
    public static Map<String, Object> convertMedicationsToMap(List<MedicationRecordDTO> medications) {
        Map<String, Object> medicationsMap = new HashMap<>();

        for (int i = 0; i < medications.size(); i++) {
            MedicationRecordDTO medication = medications.get(i);
            Map<String, Object> medicationData = new HashMap<>();
            medicationData.put("medicationName", medication.getMedicationName());
            medicationData.put("dosage", medication.getDosage());
            medicationData.put("treatmentDuration", medication.getTreatmentDuration());
            medicationData.put("instructions", medication.getInstructions());
            medicationsMap.put("medication_" + i, medicationData);
        }

        return medicationsMap;
    }

    /**
     * Converts a list of ProcedureRecordDTOs to a map for storage in PatientRecordData.
     */
    public static Map<String, Object> convertProceduresToMap(List<ProcedureRecordDTO> procedures) {
        Map<String, Object> proceduresMap = new HashMap<>();

        for (int i = 0; i < procedures.size(); i++) {
            ProcedureRecordDTO procedure = procedures.get(i);
            Map<String, Object> procedureData = new HashMap<>();
            procedureData.put("procedureName", procedure.getProcedureName());
            procedureData.put("numberOfTimes", procedure.getNumberOfTimes());
            procedureData.put("frequency", procedure.getFrequency());
            procedureData.put("instructions", procedure.getInstructions());
            procedureData.put("requiresSpecialistAssistance", procedure.getRequiresSpecialistAssistance());
            proceduresMap.put("procedure_" + i, procedureData);
        }

        return proceduresMap;
    }

    /**
     * Converts a list of DiagnosticAidRecordDTOs to a map for storage in PatientRecordData.
     */
    public static Map<String, Object> convertDiagnosticAidsToMap(List<DiagnosticAidRecordDTO> diagnosticAids) {
        Map<String, Object> diagnosticAidsMap = new HashMap<>();

        for (int i = 0; i < diagnosticAids.size(); i++) {
            DiagnosticAidRecordDTO diagnosticAid = diagnosticAids.get(i);
            Map<String, Object> diagnosticAidData = new HashMap<>();
            diagnosticAidData.put("diagnosticAidName", diagnosticAid.getDiagnosticAidName());
            diagnosticAidData.put("purpose", diagnosticAid.getPurpose());
            diagnosticAidData.put("instructions", diagnosticAid.getInstructions());
            diagnosticAidsMap.put("diagnostic_aid_" + i, diagnosticAidData);
        }

        return diagnosticAidsMap;
    }

    /**
     * Creates a PatientRecordEntryKey for a given patient, current date, and entry ID.
     */
    public static PatientRecordEntryKey createPatientRecordEntryKey(String patientCedula) {
        String entryId = UUID.randomUUID().toString();
        return PatientRecordEntryKey.of(
            PatientCedula.of(patientCedula),
            createCurrentPatientRecordDate(),
            PatientRecordEntryId.of(entryId)
        );
    }

    /**
     * Creates a PatientRecordDate for the current date.
     */
    public static PatientRecordDate createCurrentPatientRecordDate() {
        return PatientRecordDate.of(LocalDate.now());
    }

    /**
     * Creates a PatientRecordDate for a specific date string.
     */
    public static PatientRecordDate createPatientRecordDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
        return PatientRecordDate.of(date);
    }
}