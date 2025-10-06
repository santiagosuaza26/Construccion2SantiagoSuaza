package app.clinic.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.EmergencyContactDTO;
import app.clinic.application.dto.patient.InsurancePolicyDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.domain.model.EmergencyContact;
import app.clinic.domain.model.EmergencyContactName;
import app.clinic.domain.model.EmergencyContactPhoneNumber;
import app.clinic.domain.model.InsuranceCompanyName;
import app.clinic.domain.model.InsurancePolicy;
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
import app.clinic.domain.model.PolicyExpirationDate;
import app.clinic.domain.model.PolicyNumber;
import app.clinic.domain.model.PolicyStatus;
import app.clinic.domain.model.Relationship;

/**
 * Mapper class for converting between Patient domain entities and DTOs.
 * Handles bidirectional conversion between domain objects and data transfer objects.
 */
public class PatientMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Converts a CreatePatientDTO to a Patient domain entity.
     */
    public static Patient toDomainEntity(CreatePatientDTO dto) {
        // Split full name into first names and last names
        String[] nameParts = dto.getFullName().trim().split("\\s+", 2);
        String firstNames = nameParts[0];
        String lastNames = nameParts.length > 1 ? nameParts[1] : "";

        // Create emergency contact if provided
        EmergencyContact emergencyContact = null;
        if (dto.getEmergencyContact() != null) {
            // Split emergency contact name into first names and last names
            String[] emergencyContactNameParts = dto.getEmergencyContact().getName().trim().split("\\s+", 2);
            String emergencyFirstNames = emergencyContactNameParts[0];
            String emergencyLastNames = emergencyContactNameParts.length > 1 ? emergencyContactNameParts[1] : "";

            emergencyContact = EmergencyContact.of(
                EmergencyContactName.of(emergencyFirstNames, emergencyLastNames),
                Relationship.valueOf(dto.getEmergencyContact().getRelationship().toUpperCase()),
                EmergencyContactPhoneNumber.of(dto.getEmergencyContact().getPhoneNumber())
            );
        }

        // Create insurance policy if provided
        InsurancePolicy insurancePolicy = null;
        if (dto.getInsurancePolicy() != null) {
            insurancePolicy = InsurancePolicy.of(
                InsuranceCompanyName.of(dto.getInsurancePolicy().getCompanyName()),
                PolicyNumber.of(dto.getInsurancePolicy().getPolicyNumber()),
                PolicyStatus.valueOf(dto.getInsurancePolicy().getStatus().toUpperCase()),
                PolicyExpirationDate.of(parseDate(dto.getInsurancePolicy().getExpirationDate()))
            );
        }

        return Patient.of(
            PatientCedula.of(dto.getCedula()),
            PatientUsername.of(dto.getUsername()),
            PatientPassword.of(dto.getPassword()),
            PatientFullName.of(firstNames, lastNames),
            PatientBirthDate.of(parseDate(dto.getBirthDate())),
            PatientGender.valueOf(dto.getGender().toUpperCase()),
            PatientAddress.of(dto.getAddress()),
            PatientPhoneNumber.of(dto.getPhoneNumber()),
            PatientEmail.of(dto.getEmail()),
            emergencyContact != null ? java.util.List.of(emergencyContact) : java.util.List.of(),
            insurancePolicy
        );
    }

    /**
     * Converts a Patient domain entity to a PatientDTO.
     */
    public static PatientDTO toDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setCedula(patient.getCedula().getValue());
        dto.setUsername(patient.getUsername().getValue());
        dto.setFullName(patient.getFullName().getFullName());
        dto.setBirthDate(patient.getBirthDate().getValue().format(DATE_FORMAT));
        dto.setGender(patient.getGender().getDisplayName());
        dto.setAddress(patient.getAddress().getValue());
        dto.setPhoneNumber(patient.getPhoneNumber().getValue());
        dto.setEmail(patient.getEmail().getValue());
        dto.setAge(patient.getAge().getValue());

        // Convert emergency contact if present
        if (!patient.getEmergencyContacts().isEmpty()) {
            EmergencyContact emergencyContact = patient.getEmergencyContacts().get(0);
            dto.setEmergencyContact(convertEmergencyContactToDTO(emergencyContact));
        }

        // Convert insurance policy if present
        if (patient.getInsurancePolicy() != null) {
            dto.setInsurancePolicy(convertInsurancePolicyToDTO(patient.getInsurancePolicy()));
        }

        return dto;
    }

    /**
     * Helper method to convert EmergencyContact domain entity to DTO.
     */
    private static EmergencyContactDTO convertEmergencyContactToDTO(EmergencyContact emergencyContact) {
        EmergencyContactDTO dto = new EmergencyContactDTO();
        dto.setName(emergencyContact.getName().getFullName());
        dto.setRelationship(emergencyContact.getRelationship().toString());
        dto.setPhoneNumber(emergencyContact.getPhoneNumber().getValue());
        return dto;
    }

    /**
     * Helper method to convert InsurancePolicy domain entity to DTO.
     */
    private static InsurancePolicyDTO convertInsurancePolicyToDTO(InsurancePolicy insurancePolicy) {
        InsurancePolicyDTO dto = new InsurancePolicyDTO();
        dto.setCompanyName(insurancePolicy.getCompanyName().getValue());
        dto.setPolicyNumber(insurancePolicy.getPolicyNumber().getValue());
        dto.setStatus(insurancePolicy.getStatus().name());
        dto.setExpirationDate(insurancePolicy.getExpirationDate().getValue().format(DATE_FORMAT));
        dto.setActive(insurancePolicy.isActive());
        return dto;
    }

    /**
     * Updates an existing Patient domain entity with data from UpdatePatientDTO.
     */
    public static Patient updateEntity(Patient existingPatient, app.clinic.application.dto.patient.UpdatePatientDTO dto) {
        // Start with existing patient data
        PatientCedula cedula = existingPatient.getCedula();
        PatientUsername username = existingPatient.getUsername();
        PatientPassword password = existingPatient.getPassword();
        PatientFullName fullName = existingPatient.getFullName();
        PatientBirthDate birthDate = existingPatient.getBirthDate();
        PatientGender gender = existingPatient.getGender();
        PatientAddress address = existingPatient.getAddress();
        PatientPhoneNumber phoneNumber = existingPatient.getPhoneNumber();
        PatientEmail email = existingPatient.getEmail();
        List<EmergencyContact> emergencyContacts = existingPatient.getEmergencyContacts();
        InsurancePolicy insurancePolicy = existingPatient.getInsurancePolicy();

        // Update fields if provided in DTO
        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            String[] nameParts = dto.getFullName().trim().split("\\s+", 2);
            String firstNames = nameParts[0];
            String lastNames = nameParts.length > 1 ? nameParts[1] : "";
            fullName = PatientFullName.of(firstNames, lastNames);
        }

        if (dto.getBirthDate() != null && !dto.getBirthDate().trim().isEmpty()) {
            birthDate = PatientBirthDate.of(parseDate(dto.getBirthDate()));
        }

        if (dto.getGender() != null && !dto.getGender().trim().isEmpty()) {
            gender = PatientGender.valueOf(dto.getGender().toUpperCase());
        }

        if (dto.getAddress() != null && !dto.getAddress().trim().isEmpty()) {
            address = PatientAddress.of(dto.getAddress());
        }

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().trim().isEmpty()) {
            phoneNumber = PatientPhoneNumber.of(dto.getPhoneNumber());
        }

        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            email = PatientEmail.of(dto.getEmail());
        }

        // Update emergency contact if provided
        if (dto.getEmergencyContact() != null) {
            if (dto.getEmergencyContact().getName() != null && !dto.getEmergencyContact().getName().trim().isEmpty()) {
                String[] emergencyContactNameParts = dto.getEmergencyContact().getName().trim().split("\\s+", 2);
                String emergencyFirstNames = emergencyContactNameParts[0];
                String emergencyLastNames = emergencyContactNameParts.length > 1 ? emergencyContactNameParts[1] : "";

                EmergencyContact emergencyContact = EmergencyContact.of(
                    EmergencyContactName.of(emergencyFirstNames, emergencyLastNames),
                    dto.getEmergencyContact().getRelationship() != null ?
                        Relationship.valueOf(dto.getEmergencyContact().getRelationship().toUpperCase()) :
                        emergencyContacts.isEmpty() ? Relationship.PADRE : emergencyContacts.get(0).getRelationship(),
                    dto.getEmergencyContact().getPhoneNumber() != null ?
                        EmergencyContactPhoneNumber.of(dto.getEmergencyContact().getPhoneNumber()) :
                        emergencyContacts.isEmpty() ? EmergencyContactPhoneNumber.of("0000000000") : emergencyContacts.get(0).getPhoneNumber()
                );
                emergencyContacts = java.util.List.of(emergencyContact);
            }
        }

        // Update insurance policy if provided
        if (dto.getInsurancePolicy() != null) {
            if (dto.getInsurancePolicy().getCompanyName() != null && !dto.getInsurancePolicy().getCompanyName().trim().isEmpty()) {
                insurancePolicy = InsurancePolicy.of(
                    InsuranceCompanyName.of(dto.getInsurancePolicy().getCompanyName()),
                    dto.getInsurancePolicy().getPolicyNumber() != null ?
                        PolicyNumber.of(dto.getInsurancePolicy().getPolicyNumber()) :
                        insurancePolicy != null ? insurancePolicy.getPolicyNumber() : PolicyNumber.of("TEMP123"),
                    dto.getInsurancePolicy().getStatus() != null ?
                        PolicyStatus.valueOf(dto.getInsurancePolicy().getStatus().toUpperCase()) :
                        insurancePolicy != null ? insurancePolicy.getStatus() : PolicyStatus.ACTIVA,
                    dto.getInsurancePolicy().getExpirationDate() != null ?
                        PolicyExpirationDate.of(parseDate(dto.getInsurancePolicy().getExpirationDate())) :
                        insurancePolicy != null ? insurancePolicy.getExpirationDate() : PolicyExpirationDate.of(java.time.LocalDate.now().plusYears(1))
                );
            }
        }

        return Patient.of(cedula, username, password, fullName, birthDate, gender,
                         address, phoneNumber, email, emergencyContacts, insurancePolicy);
    }

    /**
     * Helper method to parse date string in DD/MM/YYYY format to LocalDate.
     */
    private static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMAT);
    }
}