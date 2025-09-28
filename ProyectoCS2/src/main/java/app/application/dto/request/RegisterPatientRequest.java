package app.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterPatientRequest {
    
    // Datos básicos del paciente
    @NotBlank(message = "ID card is required")
    @Pattern(regexp = "\\d+", message = "ID card must contain only numbers and be unique")
    private String idCard;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Birth date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in DD/MM/YYYY format, maximum 150 years")
    private String birthDate;
    
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "(?i)(masculino|femenino|otro)", message = "Gender must be: masculino, femenino, or otro")
    private String gender;
    
    @Size(max = 30, message = "Address must be maximum 30 characters")
    private String address;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{10}", message = "Patient phone must have exactly 10 digits")
    private String phone;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", 
                message = "Email must contain valid domain and @")
    private String email;
    
    // Credenciales de acceso
    @NotBlank(message = "Username is required")
    @Size(max = 15, message = "Username must be maximum 15 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only letters and numbers")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$", 
                message = "Password must contain uppercase, number and special character")
    private String password;
    
    // Contacto de emergencia (obligatorio - mínimo y máximo un solo contacto)
    @NotBlank(message = "Emergency contact first name is required")
    private String emergencyFirstName;
    
    @NotBlank(message = "Emergency contact last name is required")
    private String emergencyLastName;
    
    @NotBlank(message = "Emergency contact relationship is required")
    private String emergencyRelationship;
    
    @NotBlank(message = "Emergency contact phone is required")
    @Pattern(regexp = "\\d{10}", message = "Emergency phone must have exactly 10 digits")
    private String emergencyPhone;
    
    // Seguro médico (opcional - solo una póliza)
    private String insuranceCompany;
    
    private String policyNumber;
    
    @NotNull(message = "Policy status must be specified")
    private Boolean policyActive;
    
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Policy end date must be in dd/mm/yyyy format")
    private String policyEndDate;
    
    // Default constructor
    public RegisterPatientRequest() {}
    
    // Constructor with required fields
    public RegisterPatientRequest(String idCard, String fullName, String birthDate, String gender,
                                String phone, String email, String username, String password,
                                String emergencyFirstName, String emergencyLastName,
                                String emergencyRelationship, String emergencyPhone) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.emergencyFirstName = emergencyFirstName;
        this.emergencyLastName = emergencyLastName;
        this.emergencyRelationship = emergencyRelationship;
        this.emergencyPhone = emergencyPhone;
    }

    // Constructor with domain objects
    public RegisterPatientRequest(String idCard, String fullName, String birthDate, String gender,
                                String address, String phone, String email, String username, String password,
                                String emergencyFirstName, String emergencyLastName,
                                String emergencyRelationship, String emergencyPhone,
                                String insuranceCompany, String policyNumber, Boolean policyActive, String policyEndDate) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.emergencyFirstName = emergencyFirstName;
        this.emergencyLastName = emergencyLastName;
        this.emergencyRelationship = emergencyRelationship;
        this.emergencyPhone = emergencyPhone;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.policyActive = policyActive;
        this.policyEndDate = policyEndDate;
    }
    
    // Getters - Datos del paciente
    public String getIdCard() { return idCard; }
    public String getFullName() { return fullName; }
    public String getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    
    // Getters - Credenciales
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    // Getters - Contacto de emergencia
    public String getEmergencyFirstName() { return emergencyFirstName; }
    public String getEmergencyLastName() { return emergencyLastName; }
    public String getEmergencyRelationship() { return emergencyRelationship; }
    public String getEmergencyPhone() { return emergencyPhone; }
    
    // Getters - Seguro médico
    public String getInsuranceCompany() { return insuranceCompany; }
    public String getPolicyNumber() { return policyNumber; }
    public Boolean getPolicyActive() { return policyActive; }
    public String getPolicyEndDate() { return policyEndDate; }
    
    // Setters - Datos del paciente
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    
    // Setters - Credenciales
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    
    // Setters - Contacto de emergencia
    public void setEmergencyFirstName(String emergencyFirstName) { this.emergencyFirstName = emergencyFirstName; }
    public void setEmergencyLastName(String emergencyLastName) { this.emergencyLastName = emergencyLastName; }
    public void setEmergencyRelationship(String emergencyRelationship) { this.emergencyRelationship = emergencyRelationship; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    
    // Setters - Seguro médico
    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public void setPolicyActive(Boolean policyActive) { this.policyActive = policyActive; }
    public void setPolicyEndDate(String policyEndDate) { this.policyEndDate = policyEndDate; }
    
    // Método de utilidad para verificar si tiene seguro
    public boolean hasInsurance() {
        return insuranceCompany != null && !insuranceCompany.isBlank() &&
                policyNumber != null && !policyNumber.isBlank();
    }
    
    @Override
    public String toString() {
        return "RegisterPatientRequest{" +
                "idCard='" + idCard + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='[HIDDEN]'" +
                ", emergencyFirstName='" + emergencyFirstName + '\'' +
                ", emergencyLastName='" + emergencyLastName + '\'' +
                ", emergencyRelationship='" + emergencyRelationship + '\'' +
                ", emergencyPhone='" + emergencyPhone + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", policyActive=" + policyActive +
                ", policyEndDate='" + policyEndDate + '\'' +
                '}';
    }
}