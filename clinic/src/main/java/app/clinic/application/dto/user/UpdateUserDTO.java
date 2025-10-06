package app.clinic.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating existing users.
 * All fields are optional for partial updates.
 */
public class UpdateUserDTO {

    @NotBlank(message = "Cedula is required for identification")
    @Size(max = 20, message = "Cedula must not exceed 20 characters")
    private String cedula;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Birth date must be in DD/MM/YYYY format")
    private String birthDate;

    @Size(max = 30, message = "Address must not exceed 30 characters")
    private String address;

    @Pattern(regexp = "^\\d{1,10}$", message = "Phone number must contain between 1 and 10 digits")
    private String phoneNumber;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^(HUMAN_RESOURCES|ADMINISTRATIVE_STAFF|INFORMATION_SUPPORT|NURSE|DOCTOR)$",
             message = "Role must be one of: HUMAN_RESOURCES, ADMINISTRATIVE_STAFF, INFORMATION_SUPPORT, NURSE, DOCTOR")
    private String role;

    // Default constructor
    public UpdateUserDTO() {}

    // Constructor with parameters
    public UpdateUserDTO(String cedula, String fullName, String birthDate, String address,
                        String phoneNumber, String email, String role) {
        this.cedula = cedula;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("UpdateUserDTO{cedula='%s', fullName='%s', role='%s'}",
                           cedula, fullName, role);
    }
}