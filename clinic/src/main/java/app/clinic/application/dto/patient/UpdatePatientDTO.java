package app.clinic.application.dto.patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating existing patients.
 * All fields are optional for partial updates.
 */
public class UpdatePatientDTO {

    @NotBlank(message = "Cedula is required for identification")
    @Size(max = 20, message = "Cedula must not exceed 20 characters")
    private String cedula;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Birth date must be in DD/MM/YYYY format")
    private String birthDate;

    @Pattern(regexp = "^(MASCULINO|FEMENINO|OTRO)$",
             message = "Gender must be one of: MASCULINO, FEMENINO, OTRO")
    private String gender;

    @Size(max = 30, message = "Address must not exceed 30 characters")
    private String address;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @Email(message = "Email must be valid")
    private String email;

    @Valid
    private UpdateEmergencyContactDTO emergencyContact;

    @Valid
    private UpdateInsurancePolicyDTO insurancePolicy;

    // Default constructor
    public UpdatePatientDTO() {}

    // Constructor with parameters
    public UpdatePatientDTO(String cedula, String fullName, String birthDate, String gender,
                          String address, String phoneNumber, String email,
                          UpdateEmergencyContactDTO emergencyContact,
                          UpdateInsurancePolicyDTO insurancePolicy) {
        this.cedula = cedula;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
    }

    // Getters and Setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UpdateEmergencyContactDTO getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(UpdateEmergencyContactDTO emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public UpdateInsurancePolicyDTO getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(UpdateInsurancePolicyDTO insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    @Override
    public String toString() {
        return String.format("UpdatePatientDTO{cedula='%s', fullName='%s', gender='%s'}",
                           cedula, fullName, gender);
    }
}