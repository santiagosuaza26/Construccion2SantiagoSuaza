package app.clinic.domain.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Insurance;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Gender;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.repository.PatientRepository;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(String identificationNumber, String fullName, String dateOfBirth, String gender, String address, String phone, String email, String password, String emergencyName, String emergencyRelation, String emergencyPhone, String companyName, String policyNumber, boolean insuranceActive, String validityDate) {
        Id id = new Id(identificationNumber);
        if (patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient with this identification number already exists");
        }
        // Validate password complexity for patients
        new Password(password); // This will throw if password doesn't meet complexity requirements
        EmergencyContact emergencyContact = new EmergencyContact(emergencyName, emergencyRelation, new Phone(emergencyPhone));
        Insurance insurance = new Insurance(companyName, policyNumber, insuranceActive, java.time.LocalDate.parse(validityDate));
        Patient patient = new Patient(id, fullName, new DateOfBirth(dateOfBirth), Gender.valueOf(gender), new Address(address), new Phone(phone), new Email(email), emergencyContact, insurance);
        patientRepository.save(patient);
        return patient;
    }

    public void updatePatient(String identificationNumber, String fullName, String dateOfBirth, String gender, String address, String phone, String email, String emergencyName, String emergencyRelation, String emergencyPhone, String companyName, String policyNumber, boolean insuranceActive, String validityDate) {
        Id id = new Id(identificationNumber);
        Patient patient = patientRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        EmergencyContact emergencyContact = new EmergencyContact(emergencyName, emergencyRelation, new Phone(emergencyPhone));
        Insurance insurance = new Insurance(companyName, policyNumber, insuranceActive, java.time.LocalDate.parse(validityDate));
        Patient updatedPatient = new Patient(id, fullName, new DateOfBirth(dateOfBirth), Gender.valueOf(gender), new Address(address), new Phone(phone), new Email(email), emergencyContact, insurance);
        patientRepository.save(updatedPatient);
    }

    public Patient findPatientById(String identificationNumber) {
        Id id = new Id(identificationNumber);
        return patientRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    public void deletePatient(String identificationNumber) {
        Id id = new Id(identificationNumber);
        if (!patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        patientRepository.deleteByIdentificationNumber(id);
    }
}