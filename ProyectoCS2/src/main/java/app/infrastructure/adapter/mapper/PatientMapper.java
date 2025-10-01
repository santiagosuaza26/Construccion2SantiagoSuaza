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

        // Conversión completa de Credentials
        if (patient.getCredentials() != null) {
            entity.setUsername(patient.getCredentials().getUsername());
            entity.setPassword(patient.getCredentials().getPassword());
        }

        // Conversión completa de EmergencyContact
        if (patient.getEmergencyContact() != null) {
            entity.setEmergencyName(patient.getEmergencyContact().getFirstName() + " " + patient.getEmergencyContact().getLastName());
            entity.setEmergencyRelation(patient.getEmergencyContact().getRelationship());
            entity.setEmergencyPhone(patient.getEmergencyContact().getPhone());
        }

        // Conversión completa de InsurancePolicy
        if (patient.getInsurancePolicy() != null) {
            entity.setInsuranceCompany(patient.getInsurancePolicy().getCompany());
            entity.setInsurancePolicy(patient.getInsurancePolicy().getPolicyNumber());
            entity.setInsuranceActive(patient.getInsurancePolicy().isActive());
            entity.setInsuranceEndDate(patient.getInsurancePolicy().getEndDate());
        }

        return entity;
    }

    public Patient toDomain(PatientEntity entity) {
        if (entity == null) {
            return null;
        }

        // Crear Credentials desde la entidad
        Credentials credentials = null;
        if (entity.getUsername() != null && entity.getPassword() != null) {
            credentials = new Credentials(entity.getUsername(), entity.getPassword());
        }

        // Crear EmergencyContact desde la entidad
        EmergencyContact emergencyContact = null;
        if (entity.getEmergencyName() != null && entity.getEmergencyRelation() != null && entity.getEmergencyPhone() != null) {
            String[] nameParts = entity.getEmergencyName().split(" ", 2);
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            emergencyContact = new EmergencyContact(firstName, lastName, entity.getEmergencyRelation(), entity.getEmergencyPhone());
        }

        // Crear InsurancePolicy desde la entidad
        InsurancePolicy insurancePolicy = null;
        if (entity.getInsuranceCompany() != null && entity.getInsurancePolicy() != null) {
            insurancePolicy = new InsurancePolicy(
                entity.getInsuranceCompany(),
                entity.getInsurancePolicy(),
                entity.isInsuranceActive(),
                entity.getInsuranceEndDate()
            );
        }

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