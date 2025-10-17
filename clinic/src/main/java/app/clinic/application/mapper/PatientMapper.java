package app.clinic.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.patient.UpdatePatientDTO;
import app.clinic.application.dto.patient.EmergencyContactDTO;
import app.clinic.application.dto.patient.InsurancePolicyDTO;
import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientAddress;
import app.clinic.domain.model.PatientBirthDate;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientEmail;
import app.clinic.domain.model.PatientFullName;
import app.clinic.domain.model.PatientGender;
import app.clinic.domain.model.PatientPassword;
import app.clinic.domain.model.PatientPhoneNumber;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.model.EmergencyContact;
import app.clinic.domain.model.EmergencyContactName;
import app.clinic.domain.model.EmergencyContactPhoneNumber;
import app.clinic.domain.model.Relationship;
import app.clinic.domain.model.InsurancePolicy;
import app.clinic.domain.model.InsuranceCompanyName;
import app.clinic.domain.model.PolicyNumber;
import app.clinic.domain.model.PolicyStatus;
import app.clinic.domain.model.PolicyExpirationDate;

/**
 * Mapper para convertir entre DTOs de aplicación y entidades del dominio.
 * Mantiene la separación entre capas de presentación y dominio.
 */
public class PatientMapper {

    /**
     * Convierte un DTO de creación a entidad del dominio.
     */
    public static Patient toDomainEntity(CreatePatientDTO dto) {
        // Mapear contactos de emergencia
        List<EmergencyContact> emergencyContacts = Collections.emptyList();
        if (dto.getEmergencyContact() != null) {
            String[] nameParts = dto.getEmergencyContact().getName().trim().split("\\s+", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            EmergencyContact emergencyContact = EmergencyContact.of(
                EmergencyContactName.of(firstName, lastName),
                Relationship.valueOf(dto.getEmergencyContact().getRelationship().toUpperCase()),
                EmergencyContactPhoneNumber.of(dto.getEmergencyContact().getPhoneNumber())
            );
            emergencyContacts = List.of(emergencyContact);
        }

        // Mapear póliza de seguro
        InsurancePolicy insurancePolicy = null;
        if (dto.getInsurancePolicy() != null) {
            insurancePolicy = InsurancePolicy.of(
                InsuranceCompanyName.of(dto.getInsurancePolicy().getCompanyName()),
                PolicyNumber.of(dto.getInsurancePolicy().getPolicyNumber()),
                PolicyStatus.valueOf(dto.getInsurancePolicy().getStatus().toUpperCase()),
                PolicyExpirationDate.of(LocalDate.parse(dto.getInsurancePolicy().getExpirationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            );
        }

        return Patient.of(
            PatientCedula.of(dto.getCedula()),
            PatientUsername.of(dto.getUsername()),
            PatientPassword.of(dto.getPassword()),
            parseFullNameToPatientFullName(dto.getFullName()),
            PatientBirthDate.of(LocalDate.parse(dto.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
            PatientGender.valueOf(dto.getGender().toUpperCase()),
            PatientAddress.of(dto.getAddress()),
            PatientPhoneNumber.of(dto.getPhoneNumber()),
            PatientEmail.of(dto.getEmail()),
            emergencyContacts,
            insurancePolicy
        );
    }

    /**
     * Convierte una entidad del dominio a DTO de respuesta.
     */
    public static PatientDTO toDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setCedula(patient.getCedula().getValue());
        dto.setUsername(patient.getUsername().getValue());
        dto.setFullName(patient.getFullName().getFullName());
        dto.setBirthDate(formatDateForDTO(patient.getBirthDate().getValue()));
        dto.setGender(patient.getGender().name());
        dto.setAddress(patient.getAddress().getValue());
        dto.setPhoneNumber(patient.getPhoneNumber().getValue());
        dto.setEmail(patient.getEmail().getValue());
        dto.setAge(patient.getAge().getValue());

        // Mapear contacto de emergencia si existe
        if (patient.getEmergencyContacts() != null && !patient.getEmergencyContacts().isEmpty()) {
            EmergencyContact emergencyContact = patient.getEmergencyContacts().get(0);
            EmergencyContactDTO emergencyContactDTO = new EmergencyContactDTO();
            emergencyContactDTO.setName(emergencyContact.getName().getFullName());
            emergencyContactDTO.setRelationship(emergencyContact.getRelationship().getDisplayName());
            emergencyContactDTO.setPhoneNumber(emergencyContact.getPhoneNumber().getValue());
            dto.setEmergencyContact(emergencyContactDTO);
        }

        // Mapear póliza de seguro si existe
        if (patient.getInsurancePolicy() != null) {
            InsurancePolicy insurancePolicy = patient.getInsurancePolicy();
            InsurancePolicyDTO insurancePolicyDTO = new InsurancePolicyDTO();
            insurancePolicyDTO.setCompanyName(insurancePolicy.getCompanyName().getValue());
            insurancePolicyDTO.setPolicyNumber(insurancePolicy.getPolicyNumber().getValue());
            insurancePolicyDTO.setStatus(insurancePolicy.getStatus().name());
            insurancePolicyDTO.setExpirationDate(formatDateForDTO(insurancePolicy.getExpirationDate().getValue()));
            insurancePolicyDTO.setActive(insurancePolicy.isActive());
            dto.setInsurancePolicy(insurancePolicyDTO);
        }

        return dto;
    }

    /**
     * Formatea una fecha LocalDate al formato dd/MM/yyyy para el DTO.
     */
    private static String formatDateForDTO(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Convierte un DTO de actualización a entidad del dominio.
     */
    public static Patient toDomainEntityForUpdate(Patient existingPatient, UpdatePatientDTO updateDTO) {
        // Crear nuevo paciente con datos actualizados
        return Patient.of(
            existingPatient.getCedula(), // Mantener cédula original
            existingPatient.getUsername(), // Mantener username existente (no se puede cambiar)
            existingPatient.getPassword(), // Mantener contraseña existente
            updateDTO.getFullName() != null ?
                parseFullNameToPatientFullName(updateDTO.getFullName()) :
                existingPatient.getFullName(),
            existingPatient.getBirthDate(), // Mantener fecha de nacimiento
            updateDTO.getGender() != null ? PatientGender.valueOf(updateDTO.getGender().toUpperCase()) : existingPatient.getGender(),
            PatientAddress.of(updateDTO.getAddress() != null ? updateDTO.getAddress() : existingPatient.getAddress().getValue()),
            PatientPhoneNumber.of(updateDTO.getPhoneNumber() != null ? updateDTO.getPhoneNumber() : existingPatient.getPhoneNumber().getValue()),
            PatientEmail.of(updateDTO.getEmail() != null ? updateDTO.getEmail() : existingPatient.getEmail().getValue()),
            existingPatient.getEmergencyContacts(), // TODO: Implementar actualización de contactos de emergencia
            existingPatient.getInsurancePolicy() // TODO: Implementar actualización de póliza de seguro
        );
    }

    /**
     * Parsea un nombre completo en formato "Nombre Apellido1 Apellido2..." a PatientFullName.
     * El primer nombre es el nombre de pila y el resto son apellidos.
     */
    private static PatientFullName parseFullNameToPatientFullName(String fullName) {
        String[] parts = fullName.trim().split("\\s+", 2);
        String firstName = parts[0];
        String lastNames = parts.length > 1 ? parts[1] : "";
        return PatientFullName.of(firstName, lastNames);
    }
}