package app.infrastructure.adapter.mapper;

import org.springframework.stereotype.Component;

import app.domain.model.Credentials;
import app.domain.model.EmergencyContact;
import app.domain.model.InsurancePolicy;
import app.domain.model.Patient;
import app.infrastructure.adapter.jpa.entity.PatientEntity;

@Component("infrastructurePatientMapper")
public class PatientMapper {

    public PatientEntity toEntity(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientEntity entity = new PatientEntity();
        entity.setIdCard(patient.getIdCard());
        entity.setFullName(patient.getFullName());
        entity.setEmail(patient.getEmail());
        entity.setPhone(patient.getPhone());
        entity.setBirthDate(patient.getBirthDate());
        entity.setGender(patient.getGender());
        entity.setAddress(patient.getAddress());

        // TODO: Implementar conversión completa de Credentials, EmergencyContact y InsurancePolicy
        // Por ahora dejar campos embebidos como null para hacer que compile

        return entity;
    }

    public Patient toDomain(PatientEntity entity) {
        if (entity == null) {
            return null;
        }

        // TODO: Implementar conversión completa de CredentialsEntity, EmergencyContactEntity y InsurancePolicyEntity
        // Por ahora usar valores por defecto para hacer que compile
        Credentials credentials = new Credentials("temp", "TempPass123!");
        EmergencyContact emergencyContact = new EmergencyContact("Temp", "Contact", "Family", "1234567890");
        InsurancePolicy insurancePolicy = new InsurancePolicy("Temp", "TEMP123", true, java.time.LocalDate.now().plusDays(30));

        return new Patient(
            entity.getIdCard(),
            entity.getFullName(),
            entity.getBirthDate(),
            entity.getGender(),
            entity.getAddress(),
            entity.getPhone(),
            entity.getEmail(),
            credentials,
            emergencyContact,
            insurancePolicy
        );
    }
}