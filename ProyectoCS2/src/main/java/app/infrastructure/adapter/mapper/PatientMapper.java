package app.infrastructure.adapter.mapper;

import app.domain.model.Patient;
import app.infrastructure.adapter.entity.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientEntity toEntity(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientEntity entity = new PatientEntity();
        entity.setIdCard(patient.getIdCard());
        entity.setFirstName(patient.getFirstName());
        entity.setLastName(patient.getLastName());
        entity.setEmail(patient.getEmail());
        entity.setPhone(patient.getPhone());
        entity.setDateOfBirth(patient.getDateOfBirth());
        entity.setGender(patient.getGender());
        entity.setAddress(patient.getAddress());
        entity.setActive(patient.isActive());

        return entity;
    }

    public Patient toDomain(PatientEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Patient(
            entity.getIdCard(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getAddress(),
            entity.isActive()
        );
    }
}