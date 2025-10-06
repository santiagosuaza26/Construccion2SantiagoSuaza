package app.clinic.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Entity representing a patient in the clinic system.
 * Contains personal information, emergency contacts, and insurance policy.
 */
public class Patient {
    private final PatientCedula cedula;
    private final PatientUsername username;
    private final PatientPassword password;
    private final PatientFullName fullName;
    private final PatientBirthDate birthDate;
    private final PatientGender gender;
    private final PatientAddress address;
    private final PatientPhoneNumber phoneNumber;
    private final PatientEmail email;
    private final List<EmergencyContact> emergencyContacts;
    private final InsurancePolicy insurancePolicy;

    private Patient(PatientCedula cedula, PatientUsername username, PatientPassword password,
                   PatientFullName fullName, PatientBirthDate birthDate, PatientGender gender,
                   PatientAddress address, PatientPhoneNumber phoneNumber, PatientEmail email,
                   List<EmergencyContact> emergencyContacts, InsurancePolicy insurancePolicy) {
        this.cedula = cedula;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyContacts = List.copyOf(emergencyContacts != null ? emergencyContacts : List.of());
        this.insurancePolicy = insurancePolicy;
    }

    public static Patient of(PatientCedula cedula, PatientUsername username, PatientPassword password,
                            PatientFullName fullName, PatientBirthDate birthDate, PatientGender gender,
                            PatientAddress address, PatientPhoneNumber phoneNumber, PatientEmail email,
                            List<EmergencyContact> emergencyContacts, InsurancePolicy insurancePolicy) {
        return new Patient(cedula, username, password, fullName, birthDate, gender,
                          address, phoneNumber, email, emergencyContacts, insurancePolicy);
    }

    public PatientCedula getCedula() {
        return cedula;
    }

    public PatientUsername getUsername() {
        return username;
    }

    public PatientPassword getPassword() {
        return password;
    }

    public PatientFullName getFullName() {
        return fullName;
    }

    public PatientBirthDate getBirthDate() {
        return birthDate;
    }

    public PatientGender getGender() {
        return gender;
    }

    public PatientAddress getAddress() {
        return address;
    }

    public PatientPhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public PatientEmail getEmail() {
        return email;
    }

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public PatientAge getAge() {
        return birthDate.getAge();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(cedula, patient.cedula) &&
               Objects.equals(username, patient.username) &&
               Objects.equals(password, patient.password) &&
               Objects.equals(fullName, patient.fullName) &&
               Objects.equals(birthDate, patient.birthDate) &&
               gender == patient.gender &&
               Objects.equals(address, patient.address) &&
               Objects.equals(phoneNumber, patient.phoneNumber) &&
               Objects.equals(email, patient.email) &&
               Objects.equals(emergencyContacts, patient.emergencyContacts) &&
               Objects.equals(insurancePolicy, patient.insurancePolicy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula, username, password, fullName, birthDate, gender,
                          address, phoneNumber, email, emergencyContacts, insurancePolicy);
    }

    @Override
    public String toString() {
        return String.format("Patient{cedula=%s, name=%s, age=%s, gender=%s}",
                           cedula, fullName, getAge(), gender);
    }
}