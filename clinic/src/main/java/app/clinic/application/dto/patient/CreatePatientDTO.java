package app.clinic.application.dto.patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new patients.
 * Contains validation annotations for input data.
 */
public class CreatePatientDTO {

    @NotBlank(message = "Cedula is required")
    @Size(max = 20, message = "Cedula must not exceed 20 characters")
    private String cedula;

    @NotBlank(message = "Username is required")
    @Size(max = 15, message = "Username must not exceed 15 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only letters and numbers")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Birth date is required")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Birth date must be in DD/MM/YYYY format")
    private String birthDate;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "^(MASCULINO|FEMENINO|OTRO)$",
             message = "Gender must be one of: MASCULINO, FEMENINO, OTRO")
    private String gender;

    @NotBlank(message = "Address is required")
    @Size(max = 30, message = "Address must not exceed 30 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Valid
    private CreateEmergencyContactDTO emergencyContact;

    @Valid
    private CreateInsurancePolicyDTO insurancePolicy;

    // Default constructor
    public CreatePatientDTO() {}

    // Constructor with parameters
    public CreatePatientDTO(String cedula, String username, String password, String fullName,
                           String birthDate, String gender, String address, String phoneNumber,
                           String email, CreateEmergencyContactDTO emergencyContact,
                           CreateInsurancePolicyDTO insurancePolicy) {
        this.cedula = cedula;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public CreateEmergencyContactDTO getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(CreateEmergencyContactDTO emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public CreateInsurancePolicyDTO getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(CreateInsurancePolicyDTO insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    @Override
    public String toString() {
        return String.format("CreatePatientDTO{cedula='%s', username='%s', fullName='%s', gender='%s'}",
                           cedula, username, fullName, gender);
    }
}