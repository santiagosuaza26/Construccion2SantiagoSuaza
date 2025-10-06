package app.clinic.application.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import app.clinic.application.dto.visit.CreatePatientVisitDTO;
import app.clinic.application.dto.visit.NurseRecordDTO;
import app.clinic.application.dto.visit.PatientVisitDTO;
import app.clinic.application.dto.visit.VitalSignsDTO;
import app.clinic.domain.model.AppointmentDateTime;
import app.clinic.domain.model.BloodPressure;
import app.clinic.domain.model.Observations;
import app.clinic.domain.model.OxygenLevel;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientVisit;
import app.clinic.domain.model.PatientVisitRecord;
import app.clinic.domain.model.Pulse;
import app.clinic.domain.model.Temperature;
import app.clinic.domain.model.VitalSignsData;

/**
 * Mapper class for converting between PatientVisit domain entities and DTOs.
 * Handles bidirectional conversion between domain objects and data transfer objects.
 */
public class PatientVisitMapper {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Converts a CreatePatientVisitDTO to a PatientVisit domain entity.
     */
    public static PatientVisit toDomainEntity(CreatePatientVisitDTO dto) {
        // Convert patient cedula
        PatientCedula patientCedula = PatientCedula.of(dto.getPatientCedula());

        // Convert visit date time
        LocalDateTime visitDateTime;
        try {
            visitDateTime = LocalDateTime.parse(dto.getVisitDateTime(), DATE_TIME_FORMAT);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid visit date time format. Expected format: dd/MM/yyyy HH:mm", e);
        }
        AppointmentDateTime appointmentDateTime = AppointmentDateTime.of(visitDateTime);

        // Convert vital signs
        VitalSignsData vitalSignsData = convertVitalSignsToDomain(dto.getVitalSigns());

        // Convert nurse record
        PatientVisitRecord visitRecord = convertNurseRecordToDomain(dto.getNurseRecord(), vitalSignsData, patientCedula);

        // Create patient visit
        return PatientVisit.of(
            "VIS" + System.currentTimeMillis(),
            patientCedula,
            appointmentDateTime,
            visitRecord
        );
    }

    /**
     * Converts a PatientVisit domain entity to a PatientVisitDTO.
     */
    public static PatientVisitDTO toDTO(PatientVisit patientVisit) {
        PatientVisitDTO dto = new PatientVisitDTO();
        dto.setId(patientVisit.getId());
        dto.setPatientCedula(patientVisit.getPatientCedula().getValue());
        dto.setVisitDateTime(patientVisit.getVisitDateTime().getValue().format(DATE_TIME_FORMAT));
        dto.setVitalSigns(convertVitalSignsFromDomain(patientVisit.getVisitRecord().getVitalSigns()));
        dto.setNurseRecord(convertNurseRecordFromDomain(patientVisit.getVisitRecord()));
        dto.setCompleted(patientVisit.isCompleted());

        return dto;
    }

    /**
     * Helper method to convert VitalSignsDTO to VitalSignsData domain entity.
     */
    private static VitalSignsData convertVitalSignsToDomain(VitalSignsDTO vitalSignsDTO) {
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
    private static VitalSignsDTO convertVitalSignsFromDomain(VitalSignsData vitalSignsData) {
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
     * Helper method to convert NurseRecordDTO to PatientVisitRecord domain entity.
     */
    private static PatientVisitRecord convertNurseRecordToDomain(NurseRecordDTO nurseRecordDTO, VitalSignsData vitalSignsData, PatientCedula patientCedula) {
        if (nurseRecordDTO == null) {
            return PatientVisitRecord.of(
                patientCedula,
                vitalSignsData,
                Observations.of("")
            );
        }

        Observations observations = Observations.of(nurseRecordDTO.getObservations());

        return PatientVisitRecord.of(
            patientCedula,
            vitalSignsData,
            observations,
            nurseRecordDTO.getAdministeredMedications(),
            nurseRecordDTO.getPerformedProcedures()
        );
    }

    /**
     * Helper method to convert PatientVisitRecord domain entity to NurseRecordDTO.
     */
    private static NurseRecordDTO convertNurseRecordFromDomain(PatientVisitRecord visitRecord) {
        NurseRecordDTO dto = new NurseRecordDTO();
        dto.setAdministeredMedications(visitRecord.getAdministeredMedications());
        dto.setPerformedProcedures(visitRecord.getPerformedProcedures());

        if (visitRecord.getObservations() != null) {
            dto.setObservations(visitRecord.getObservations().getValue());
        }

        return dto;
    }
}