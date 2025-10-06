package app.clinic.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new users.
 * Contains validation annotations for input data.
 */
public class CreateUserDTO {

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

    @NotBlank(message = "Address is required")
    @Size(max = 30, message = "Address must not exceed 30 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{1,10}$", message = "Phone number must contain between 1 and 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Role is required")
    @Pattern(regexp = "^(HUMAN_RESOURCES|ADMINISTRATIVE_STAFF|INFORMATION_SUPPORT|NURSE|DOCTOR)$",
             message = "Role must be one of: HUMAN_RESOURCES, ADMINISTRATIVE_STAFF, INFORMATION_SUPPORT, NURSE, DOCTOR")
    private String role;

    // Default constructor
    public CreateUserDTO() {}

    // Constructor with parameters
    public CreateUserDTO(String cedula, String username, String password, String fullName,
                        String birthDate, String address, String phoneNumber, String email, String role) {
        this.cedula = cedula;
        this.username = username;
        this.password = password;
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
        return String.format("CreateUserDTO{cedula='%s', username='%s', fullName='%s', role='%s'}",
                           cedula, username, fullName, role);
    }
}