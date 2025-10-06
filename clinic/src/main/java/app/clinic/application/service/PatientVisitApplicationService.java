package app.clinic.application.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.visit.CreatePatientVisitDTO;
import app.clinic.application.dto.visit.PatientVisitDTO;
import app.clinic.application.dto.visit.VitalSignsDTO;
import app.clinic.application.mapper.PatientVisitMapper;
import app.clinic.domain.model.AppointmentDateTime;
import app.clinic.domain.model.BloodPressure;
import app.clinic.domain.model.OxygenLevel;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientVisit;
import app.clinic.domain.model.PatientVisitId;
import app.clinic.domain.model.PatientVisitRecord;
import app.clinic.domain.model.Pulse;
import app.clinic.domain.model.Temperature;
import app.clinic.domain.model.VitalSignsData;
import app.clinic.domain.service.PatientVisitDomainService;

/**
 * Application service for patient visit management operations.
 * Coordinates between REST controllers and domain services.
 * Handles patient visit-related use cases and business operations.
 */
@Service
public class PatientVisitApplicationService {

    private final PatientVisitDomainService patientVisitDomainService;

    public PatientVisitApplicationService(PatientVisitDomainService patientVisitDomainService) {
        this.patientVisitDomainService = patientVisitDomainService;
    }

    /**
     * Creates a new patient visit record.
     */
    public PatientVisitDTO createPatientVisit(CreatePatientVisitDTO createPatientVisitDTO) {
        // Convert DTO to domain entity using mapper
        PatientVisit patientVisit = PatientVisitMapper.toDomainEntity(createPatientVisitDTO);

        // Use domain service for business logic (if needed)
        // For now, we'll work directly with the domain entity

        // Convert back to DTO for response
        return PatientVisitMapper.toDTO(patientVisit);
    }

    /**
     * Records vital signs for a patient visit.
     */
    public VitalSignsDTO recordVitalSigns(String visitId, VitalSignsDTO vitalSignsDTO) {
        // Validate input parameters
        if (visitId == null || visitId.trim().isEmpty()) {
            throw new IllegalArgumentException("Visit ID cannot be null or empty");
        }
        if (vitalSignsDTO == null) {
            throw new IllegalArgumentException("Vital signs data cannot be null");
        }

        // Validate vital signs data
        if (!validateVitalSigns(vitalSignsDTO)) {
            throw new IllegalArgumentException("Invalid vital signs data provided");
        }

        // Find existing patient visit
        PatientVisitId patientVisitId = PatientVisitId.of(visitId);
        Optional<PatientVisit> existingVisit = patientVisitDomainService.findPatientVisitById(patientVisitId);

        if (existingVisit.isEmpty()) {
            throw new IllegalArgumentException("Patient visit with ID " + visitId + " not found");
        }

        PatientVisit currentVisit = existingVisit.get();

        // Convert new vital signs to domain entities
        VitalSignsData newVitalSignsData = convertVitalSignsToDomain(vitalSignsDTO);

        // Create new visit record with updated vital signs
        PatientVisitRecord updatedVisitRecord = PatientVisitRecord.of(
            currentVisit.getPatientCedula(),
            newVitalSignsData,
            currentVisit.getVisitRecord().getObservations(),
            currentVisit.getVisitRecord().getAdministeredMedications(),
            currentVisit.getVisitRecord().getPerformedProcedures()
        );

        // Create updated patient visit with new vital signs
        PatientVisit updatedVisit = PatientVisit.of(
            currentVisit.getId(),
            currentVisit.getPatientCedula(),
            currentVisit.getVisitDateTime(),
            updatedVisitRecord,
            currentVisit.isCompleted()
        );

        // Save updated visit using domain service
        PatientVisit savedVisit = patientVisitDomainService.updatePatientVisit(updatedVisit);

        // Convert back to DTO for response
        return convertVitalSignsFromDomain(savedVisit.getVisitRecord().getVitalSigns());
    }

    /**
      * Finds a patient visit by ID.
      */
     public Optional<PatientVisitDTO> findPatientVisitById(String visitId) {
         // Validate input parameter
         if (visitId == null || visitId.trim().isEmpty()) {
             throw new IllegalArgumentException("Visit ID cannot be null or empty");
         }

         // Use domain service to find the patient visit
         PatientVisitId patientVisitId = PatientVisitId.of(visitId);
         Optional<PatientVisit> patientVisit = patientVisitDomainService.findPatientVisitById(patientVisitId);

         // Convert to DTO if found, otherwise return empty Optional
         return patientVisit.map(PatientVisitMapper::toDTO);
     }

    /**
      * Finds all visits for a specific patient.
      */
     public List<PatientVisitDTO> findVisitsByPatientCedula(String patientCedula) {
         // Validate input parameter
         if (patientCedula == null || patientCedula.trim().isEmpty()) {
             throw new IllegalArgumentException("Patient cedula cannot be null or empty");
         }

         // Use domain service to find patient visits
         PatientCedula patientCedulaDomain = PatientCedula.of(patientCedula);
         List<PatientVisit> patientVisits = patientVisitDomainService.findPatientVisitsByPatient(patientCedulaDomain);

         // Convert to DTOs
         return patientVisits.stream()
             .map(PatientVisitMapper::toDTO)
             .toList();
     }

    /**
     * Finds all visits within a date range.
     */
    public List<PatientVisitDTO> findVisitsByDateRange(String startDate, String endDate) {
        // Validate input parameters
        if (startDate == null || startDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Start date cannot be null or empty");
        }
        if (endDate == null || endDate.trim().isEmpty()) {
            throw new IllegalArgumentException("End date cannot be null or empty");
        }

        try {
            // Parse string dates to LocalDateTime (assuming format "yyyy-MM-dd HH:mm:ss")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(startDate.trim(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate.trim(), formatter);

            // Validate date range
            if (startDateTime.isAfter(endDateTime)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }

            // Use domain service to find patient visits
            List<PatientVisit> patientVisits = patientVisitDomainService.findVisitsByDateRange(startDateTime, endDateTime);

            // Convert to DTOs
            return patientVisits.stream()
                .map(PatientVisitMapper::toDTO)
                .toList();

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd HH:mm:ss", e);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while searching for patient visits by date range", e);
        }
    }

    /**
     * Updates a patient visit record.
     */
    public PatientVisitDTO updatePatientVisit(String visitId, CreatePatientVisitDTO updateDTO) {
        // Validate input parameters
        if (visitId == null || visitId.trim().isEmpty()) {
            throw new IllegalArgumentException("Visit ID cannot be null or empty");
        }
        if (updateDTO == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        // Find existing patient visit
        PatientVisitId patientVisitId = PatientVisitId.of(visitId);
        Optional<PatientVisit> existingVisit = patientVisitDomainService.findPatientVisitById(patientVisitId);

        if (existingVisit.isEmpty()) {
            throw new IllegalArgumentException("Patient visit with ID " + visitId + " not found");
        }

        PatientVisit currentVisit = existingVisit.get();

        // Convert updated DTO to domain entity using mapper
        PatientVisit updatedPatientVisit = PatientVisitMapper.toDomainEntity(updateDTO);

        // Preserve the original visit ID and completed status
        PatientVisit visitWithUpdatedData = PatientVisit.of(
            currentVisit.getId(),
            updatedPatientVisit.getPatientCedula(),
            updatedPatientVisit.getVisitDateTime(),
            updatedPatientVisit.getVisitRecord(),
            currentVisit.isCompleted()
        );

        // Update patient visit using domain service
        PatientVisit savedVisit = patientVisitDomainService.updatePatientVisit(visitWithUpdatedData);

        // Convert back to DTO for response
        return PatientVisitMapper.toDTO(savedVisit);
    }

    /**
      * Marks a patient visit as completed.
      */
     public PatientVisitDTO completePatientVisit(String visitId) {
         // Validate input parameter
         if (visitId == null || visitId.trim().isEmpty()) {
             throw new IllegalArgumentException("Visit ID cannot be null or empty");
         }

         // Use domain service to complete the patient visit
         PatientVisitId patientVisitId = PatientVisitId.of(visitId);
         PatientVisit completedVisit = patientVisitDomainService.completePatientVisit(patientVisitId);

         // Convert to DTO for response
         return PatientVisitMapper.toDTO(completedVisit);
     }

    /**
     * Validates vital signs data.
     */
    public boolean validateVitalSigns(VitalSignsDTO vitalSignsDTO) {
        if (vitalSignsDTO == null) {
            return false;
        }

        // Blood pressure validation (format: "120/80")
        if (vitalSignsDTO.getBloodPressure() != null && !vitalSignsDTO.getBloodPressure().trim().isEmpty()) {
            String bp = vitalSignsDTO.getBloodPressure().trim();
            if (!bp.matches("^\\d{2,3}/\\d{2,3}$")) {
                return false;
            }
        }

        // Temperature validation (normal range: 35.0 - 42.0 °C)
        if (vitalSignsDTO.getTemperature() != null && !vitalSignsDTO.getTemperature().trim().isEmpty()) {
            try {
                double temp = Double.parseDouble(vitalSignsDTO.getTemperature());
                if (temp < 35.0 || temp > 42.0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Pulse validation (normal range: 50 - 200 bpm)
        if (vitalSignsDTO.getPulse() != null) {
            int pulse = vitalSignsDTO.getPulse();
            if (pulse < 50 || pulse > 200) {
                return false;
            }
        }

        // Oxygen level validation (normal range: 90 - 100%)
        if (vitalSignsDTO.getOxygenLevel() != null && !vitalSignsDTO.getOxygenLevel().trim().isEmpty()) {
            try {
                int oxygen = Integer.parseInt(vitalSignsDTO.getOxygenLevel().replace("%", ""));
                if (oxygen < 90 || oxygen > 100) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets visit statistics for a patient.
     */
    public VisitStatisticsDTO getVisitStatistics(String patientCedula) {
        // Validate input parameter
        if (patientCedula == null || patientCedula.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient cedula cannot be null or empty");
        }

        // Get all visits for the specific patient
        PatientCedula patientCedulaDomain = PatientCedula.of(patientCedula);
        List<PatientVisit> patientVisits = patientVisitDomainService.findPatientVisitsByPatient(patientCedulaDomain);

        // Calculate statistics
        int totalVisits = patientVisits.size();
        int completedVisits = (int) patientVisits.stream().filter(PatientVisit::isCompleted).count();
        int pendingVisits = totalVisits - completedVisits;

        // Find last visit date
        String lastVisitDate = patientVisits.stream()
            .map(PatientVisit::getVisitDateTime)
            .filter(dateTime -> dateTime != null)
            .map(AppointmentDateTime::getValue)
            .max(LocalDateTime::compareTo)
            .map(dateTime -> dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .orElse(null);

        // Create and return statistics DTO
        VisitStatisticsDTO statistics = new VisitStatisticsDTO();
        statistics.setTotalVisits(totalVisits);
        statistics.setCompletedVisits(completedVisits);
        statistics.setPendingVisits(pendingVisits);
        statistics.setLastVisitDate(lastVisitDate);

        return statistics;
    }

    /**
      * Checks if a patient has any pending visits.
      */
     public boolean hasPendingVisits(String patientCedula) {
         // Validate input parameter
         if (patientCedula == null || patientCedula.trim().isEmpty()) {
             throw new IllegalArgumentException("Patient cedula cannot be null or empty");
         }

         // Get all completed visits
         List<PatientVisit> completedVisits = patientVisitDomainService.findAllCompletedPatientVisits();

         // Get all visits for the specific patient
         PatientCedula patientCedulaDomain = PatientCedula.of(patientCedula);
         List<PatientVisit> patientVisits = patientVisitDomainService.findPatientVisitsByPatient(patientCedulaDomain);

         // Check if patient has any visits that are not completed
         return patientVisits.stream()
             .anyMatch(visit -> !visit.isCompleted());
     }

    /**
     * Helper method to convert VitalSignsDTO to VitalSignsData domain entity.
     */
    private VitalSignsData convertVitalSignsToDomain(VitalSignsDTO vitalSignsDTO) {
        if (vitalSignsDTO == null) {
            return null;
        }

        BloodPressure bloodPressure = null;
        if (vitalSignsDTO.getBloodPressure() != null && !vitalSignsDTO.getBloodPressure().trim().isEmpty()) {
            bloodPressure = BloodPressure.of(vitalSignsDTO.getBloodPressure());
        }

        Temperature temperature = null;
        if (vitalSignsDTO.getTemperature() != null && !vitalSignsDTO.getTemperature().trim().isEmpty()) {
            try {
                double tempValue = Double.parseDouble(vitalSignsDTO.getTemperature());
                temperature = Temperature.of(tempValue);
            } catch (NumberFormatException e) {
                // Handle invalid temperature format
                temperature = null;
            }
        }

        Pulse pulse = null;
        if (vitalSignsDTO.getPulse() != null) {
            pulse = Pulse.of(vitalSignsDTO.getPulse());
        }

        OxygenLevel oxygenLevel = null;
        if (vitalSignsDTO.getOxygenLevel() != null && !vitalSignsDTO.getOxygenLevel().trim().isEmpty()) {
            try {
                String oxygenStr = vitalSignsDTO.getOxygenLevel().replace("%", "");
                double oxygenValue = Double.parseDouble(oxygenStr);
                oxygenLevel = OxygenLevel.of(oxygenValue);
            } catch (NumberFormatException e) {
                // Handle invalid oxygen level format
                oxygenLevel = null;
            }
        }

        return VitalSignsData.of(bloodPressure, temperature, pulse, oxygenLevel);
    }

    /**
     * Helper method to convert VitalSignsData domain entity to VitalSignsDTO.
     */
    private VitalSignsDTO convertVitalSignsFromDomain(VitalSignsData vitalSignsData) {
        if (vitalSignsData == null) {
            return null;
        }

        VitalSignsDTO dto = new VitalSignsDTO();

        if (vitalSignsData.getBloodPressure() != null) {
            dto.setBloodPressure(vitalSignsData.getBloodPressure().getValue());
        }

        if (vitalSignsData.getTemperature() != null) {
            dto.setTemperature(vitalSignsData.getTemperature().getValue().toString());
        }

        if (vitalSignsData.getPulse() != null) {
            dto.setPulse(vitalSignsData.getPulse().getValue());
        }

        if (vitalSignsData.getOxygenLevel() != null) {
            dto.setOxygenLevel(vitalSignsData.getOxygenLevel().getValue() + "%");
        }

        return dto;
    }

    /**
     * DTO for visit statistics.
     */
    public static class VisitStatisticsDTO {
        private int totalVisits;
        private int completedVisits;
        private int pendingVisits;
        private String lastVisitDate;

        // Constructors, getters and setters
        public VisitStatisticsDTO() {}

        public VisitStatisticsDTO(int totalVisits, int completedVisits, int pendingVisits, String lastVisitDate) {
            this.totalVisits = totalVisits;
            this.completedVisits = completedVisits;
            this.pendingVisits = pendingVisits;
            this.lastVisitDate = lastVisitDate;
        }

        // Getters and setters
        public int getTotalVisits() { return totalVisits; }
        public void setTotalVisits(int totalVisits) { this.totalVisits = totalVisits; }
        public int getCompletedVisits() { return completedVisits; }
        public void setCompletedVisits(int completedVisits) { this.completedVisits = completedVisits; }
        public int getPendingVisits() { return pendingVisits; }
        public void setPendingVisits(int pendingVisits) { this.pendingVisits = pendingVisits; }
        public String getLastVisitDate() { return lastVisitDate; }
        public void setLastVisitDate(String lastVisitDate) { this.lastVisitDate = lastVisitDate; }
    }
}