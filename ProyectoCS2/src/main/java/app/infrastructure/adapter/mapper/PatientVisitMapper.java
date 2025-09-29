package app.infrastructure.adapter.mapper;

import org.springframework.stereotype.Component;

import app.domain.model.PatientVisit;
import app.domain.model.VitalSigns;
import app.infrastructure.adapter.jpa.entity.PatientVisitEntity;
import app.infrastructure.adapter.jpa.entity.VitalSignsEmbedded;

@Component("infrastructurePatientVisitMapper")
public class PatientVisitMapper {

    public PatientVisitEntity toEntity(PatientVisit patientVisit) {
        if (patientVisit == null) {
            return null;
        }

        VitalSignsEmbedded vitalSignsEmbedded = null;
        if (patientVisit.getVitalSigns() != null) {
            VitalSigns vitalSigns = patientVisit.getVitalSigns();
            vitalSignsEmbedded = new VitalSignsEmbedded(
                vitalSigns.getBloodPressure(),
                vitalSigns.getTemperature(),
                vitalSigns.getPulse(),
                vitalSigns.getOxygenLevel()
            );
        }

        return new PatientVisitEntity(
            patientVisit.getVisitId(),
            patientVisit.getPatientIdCard(),
            patientVisit.getAttendingStaffIdCard(),
            patientVisit.getAttendingStaffRole(),
            patientVisit.getVisitDateTime(),
            patientVisit.getNotes(),
            vitalSignsEmbedded,
            patientVisit.getRelatedOrderNumber()
        );
    }

    public PatientVisit toDomain(PatientVisitEntity entity) {
        if (entity == null) {
            return null;
        }

        VitalSigns vitalSigns = null;
        if (entity.getVitalSigns() != null) {
            VitalSignsEmbedded embedded = entity.getVitalSigns();
            vitalSigns = new VitalSigns(
                embedded.getBloodPressure(),
                embedded.getTemperature(),
                embedded.getPulse(),
                embedded.getOxygenLevel()
            );
        }

        return new PatientVisit(
            entity.getVisitId(),
            entity.getPatientIdCard(),
            entity.getAttendingStaffIdCard(),
            entity.getAttendingStaffRole(),
            entity.getVisitDateTime(),
            entity.getNotes(),
            vitalSigns,
            entity.getRelatedOrderNumber()
        );
    }
}