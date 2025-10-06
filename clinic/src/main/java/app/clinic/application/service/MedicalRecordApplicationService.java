package app.clinic.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import app.clinic.domain.model.Symptoms;
import app.clinic.domain.service.MedicalRecordDomainService;

/**
 * Application service for medical record management operations.
 * Coordinates between REST controllers and domain services.
 * Handles medical record-related use cases and business operations.
 */
@Service
public class MedicalRecordApplicationService {

    private final MedicalRecordDomainService medicalRecordDomainService;

    public MedicalRecordApplicationService(MedicalRecordDomainService medicalRecordDomainService) {
        this.medicalRecordDomainService = medicalRecordDomainService;
    }

    /**
     * Creates a new medical record entry for a patient.
     */
    public MedicalRecordEntryDTO createMedicalRecord(CreateMedicalRecordDTO createMedicalRecordDTO) {
        // Convert DTO to domain entities
        PatientRecordEntry entry = convertToPatientRecordEntry(createMedicalRecordDTO);

        // Use today's date as the record date
        PatientRecordDate recordDate = PatientRecordDate.today();

        // Create or update patient record using domain service
        medicalRecordDomainService.addRecordEntry(
            PatientCedula.of(createMedicalRecordDTO.getPatientCedula()),
            recordDate,
            entry
        );

        // Convert back to DTO and return
        return convertEntryToDTO(entry);
    }

    /**
     * Finds medical record for a specific patient.
     */
    public Optional<MedicalRecordDTO> findMedicalRecordByPatientCedula(String patientCedula) {
        PatientRecord patientRecord = medicalRecordDomainService.findByPatientCedula(
            PatientCedula.of(patientCedula)
        );

        if (patientRecord.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(convertToDTO(patientRecord, patientCedula));
    }

    /**
     * Finds a specific medical record entry by patient and date.
     */
    public Optional<MedicalRecordEntryDTO> findMedicalRecordEntry(String patientCedula, String recordDate) {
        PatientRecord patientRecord = medicalRecordDomainService.findByPatientCedula(
            PatientCedula.of(patientCedula)
        );

        if (patientRecord.isEmpty()) {
            return Optional.empty();
        }

        try {
            // Parse recordDate string using the standard format dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(recordDate.trim(), formatter);
            PatientRecordDate patientRecordDate = PatientRecordDate.of(localDate);

            // Get the record entry for the specified date
            PatientRecordEntry entry = patientRecord.getRecord(patientRecordDate);

            // Convert to DTO if entry exists, otherwise return empty
            return Optional.ofNullable(entry).map(this::convertEntryToDTO);

        } catch (java.time.format.DateTimeParseException e) {
            // Return empty if date parsing fails - invalid format
            return Optional.empty();
        } catch (Exception e) {
            // Return empty for any other unexpected errors
            return Optional.empty();
        }
    }

    /**
     * Updates an existing medical record entry.
     */
    public MedicalRecordEntryDTO updateMedicalRecordEntry(String patientCedula, String recordDate,
                                                         CreateMedicalRecordDTO updateDTO) {
        // Validate input parameters
        if (patientCedula == null || patientCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient cedula cannot be null or empty");
        }
        if (recordDate == null || recordDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Record date cannot be null or empty");
        }
        if (updateDTO == null) {
            throw new IllegalArgumentException("Update DTO cannot be null");
        }

        // Check if patient has any medical records
        if (!hasMedicalRecords(patientCedula)) {
            throw new IllegalArgumentException("Patient does not have any medical records");
        }

        try {
            // Parse recordDate string using the standard format dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(recordDate.trim(), formatter);
            PatientRecordDate patientRecordDate = PatientRecordDate.of(localDate);

            // Verify that the specific record entry exists
            PatientRecord patientRecord = medicalRecordDomainService.findByPatientCedula(
                PatientCedula.of(patientCedula)
            );

            if (!patientRecord.hasRecord(patientRecordDate)) {
                throw new IllegalArgumentException("Medical record entry not found for the specified date");
            }

            // Convert DTO to domain entity
            PatientRecordEntry entry = convertToPatientRecordEntry(updateDTO);

            // Update the record entry using domain service
            medicalRecordDomainService.addRecordEntry(
                PatientCedula.of(patientCedula),
                patientRecordDate,
                entry
            );

            // Convert back to DTO and return
            return convertEntryToDTO(entry);

        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: dd/MM/yyyy", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update medical record entry", e);
        }
    }

    /**
     * Deletes a medical record entry.
     */
    public void deleteMedicalRecordEntry(String patientCedula, String recordDate) {
        // Validate input parameters
        if (patientCedula == null || patientCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient cedula cannot be null or empty");
        }
        if (recordDate == null || recordDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Record date cannot be null or empty");
        }

        // Check if patient has any medical records
        if (!hasMedicalRecords(patientCedula)) {
            throw new IllegalArgumentException("Patient does not have any medical records");
        }

        try {
            // Parse recordDate string using the standard format dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(recordDate.trim(), formatter);
            PatientRecordDate patientRecordDate = PatientRecordDate.of(localDate);

            // Verify that the specific record entry exists
            PatientRecord patientRecord = medicalRecordDomainService.findByPatientCedula(
                PatientCedula.of(patientCedula)
            );

            if (!patientRecord.hasRecord(patientRecordDate)) {
                throw new IllegalArgumentException("Medical record entry not found for the specified date");
            }

            // Delete the record entry using domain service
            medicalRecordDomainService.removeRecordEntry(
                PatientCedula.of(patientCedula),
                patientRecordDate
            );

        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: dd/MM/yyyy", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete medical record entry", e);
        }
    }

    /**
     * Checks if a patient has any medical records.
     */
    public boolean hasMedicalRecords(String patientCedula) {
        PatientRecord patientRecord = medicalRecordDomainService.findByPatientCedula(
            PatientCedula.of(patientCedula)
        );
        return !patientRecord.isEmpty();
    }

    /**
     * Gets the number of medical record entries for a patient.
     */
    public int getMedicalRecordCount(String patientCedula) {
        PatientRecord patientRecord = medicalRecordDomainService.findByPatientCedula(
            PatientCedula.of(patientCedula)
        );
        return patientRecord.getRecords().size();
    }

    /**
     * Helper method to convert PatientRecord domain entity to MedicalRecordDTO.
     */
    private MedicalRecordDTO convertToDTO(PatientRecord patientRecord, String patientCedula) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientCedula(patientCedula);

        // Convert each record entry
        dto.setRecords(patientRecord.getRecords().entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getValue().toString(),
                entry -> convertEntryToDTO(entry.getValue())
            )));

        return dto;
    }

    /**
     * Helper method to convert PatientRecordEntry domain entity to MedicalRecordEntryDTO.
     */
    private MedicalRecordEntryDTO convertEntryToDTO(PatientRecordEntry entry) {
        MedicalRecordEntryDTO dto = new MedicalRecordEntryDTO();
        dto.setDoctorCedula(entry.getDoctorCedula().getValue());
        dto.setConsultationReason(entry.getConsultationReason().getValue());
        dto.setSymptoms(entry.getSymptoms().getValue());
        dto.setDiagnosis(entry.getDiagnosis().getValue());
        dto.setData(convertRecordDataToDTO(entry.getData()));

        return dto;
    }

    /**
     * Helper method to convert PatientRecordData domain entity to MedicalRecordDataDTO.
     */
    private MedicalRecordDataDTO convertRecordDataToDTO(PatientRecordData patientRecordData) {
        return new MedicalRecordDataDTO(patientRecordData.getData());
    }

    /**
     * Helper method to convert CreateMedicalRecordDTO to PatientRecordEntry domain entity.
     */
    private PatientRecordEntry convertToPatientRecordEntry(CreateMedicalRecordDTO dto) {
        // Create unstructured data map for medications, procedures, and diagnostic aids
        java.util.Map<String, Object> unstructuredData = new java.util.HashMap<>();

        // Process medications from DTO
        if (dto.getMedications() != null && !dto.getMedications().isEmpty()) {
            java.util.List<java.util.Map<String, Object>> medications = new java.util.ArrayList<>();
            for (MedicationRecordDTO medication : dto.getMedications()) {
                java.util.Map<String, Object> medicationData = new java.util.HashMap<>();
                medicationData.put("name", medication.getMedicationName());
                medicationData.put("dosage", medication.getDosage());
                medicationData.put("treatmentDuration", medication.getTreatmentDuration());
                medicationData.put("instructions", medication.getInstructions());
                medications.add(medicationData);
            }
            unstructuredData.put("medications", medications);
        }

        // Process procedures from DTO
        if (dto.getProcedures() != null && !dto.getProcedures().isEmpty()) {
            java.util.List<java.util.Map<String, Object>> procedures = new java.util.ArrayList<>();
            for (ProcedureRecordDTO procedure : dto.getProcedures()) {
                java.util.Map<String, Object> procedureData = new java.util.HashMap<>();
                procedureData.put("name", procedure.getProcedureName());
                procedureData.put("numberOfTimes", procedure.getNumberOfTimes());
                procedureData.put("frequency", procedure.getFrequency());
                procedureData.put("instructions", procedure.getInstructions());
                procedureData.put("requiresSpecialistAssistance", procedure.getRequiresSpecialistAssistance());
                procedures.add(procedureData);
            }
            unstructuredData.put("procedures", procedures);
        }

        // Process diagnostic aids from DTO
        if (dto.getDiagnosticAids() != null && !dto.getDiagnosticAids().isEmpty()) {
            java.util.List<java.util.Map<String, Object>> diagnosticAids = new java.util.ArrayList<>();
            for (DiagnosticAidRecordDTO diagnosticAid : dto.getDiagnosticAids()) {
                java.util.Map<String, Object> diagnosticAidData = new java.util.HashMap<>();
                diagnosticAidData.put("name", diagnosticAid.getDiagnosticAidName());
                diagnosticAidData.put("quantity", diagnosticAid.getQuantity());
                diagnosticAidData.put("purpose", diagnosticAid.getPurpose());
                diagnosticAidData.put("instructions", diagnosticAid.getInstructions());
                diagnosticAidData.put("requiresSpecialistAssistance", diagnosticAid.getRequiresSpecialistAssistance());
                diagnosticAids.add(diagnosticAidData);
            }
            unstructuredData.put("diagnosticAids", diagnosticAids);
        }

        // Create PatientRecordData with the structured information
        PatientRecordData data = PatientRecordData.of(unstructuredData);

        return PatientRecordEntry.of(
            DoctorCedula.of(dto.getDoctorCedula()),
            ConsultationReason.of(dto.getConsultationReason()),
            Symptoms.of(dto.getSymptoms()),
            Diagnosis.of(dto.getDiagnosis()),
            data
        );
    }
}