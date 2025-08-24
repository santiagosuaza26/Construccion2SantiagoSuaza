package app.domain.model;

import java.time.LocalDate;

public class Patient {
    private String id;
    private String fullName;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private EmergencyContact emergencyContact;
    private MedicalInsurance medicalInsurance;

    public Patient(String id, String fullName, LocalDate birthDate, String gender, String address, String phoneNumber, String email, EmergencyContact emergencyContact, MedicalInsurance medicalInsurance) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.medicalInsurance = medicalInsurance;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public MedicalInsurance getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setMedicalInsurance(MedicalInsurance medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", emergencyContact=" + emergencyContact +
                ", medicalInsurance=" + medicalInsurance +
                '}';
    }

}