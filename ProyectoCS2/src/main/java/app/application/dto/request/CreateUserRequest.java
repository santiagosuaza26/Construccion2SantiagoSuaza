package app.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "ID card is required")
    @Pattern(regexp = "\\d+", message = "ID card must contain only numbers")
    private String idCard;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", 
                message = "Email must contain valid domain and @")
    private String email;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{1,10}", message = "Phone must contain between 1 and 10 digits")
    private String phone;
    
    @NotBlank(message = "Birth date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Birth date must be in DD/MM/YYYY format")
    private String birthDate;
    
    @Size(max = 30, message = "Address must be maximum 30 characters")
    private String address;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "HUMAN_RESOURCES|ADMINISTRATIVE|SUPPORT|NURSE|DOCTOR", 
                message = "Role must be: HUMAN_RESOURCES, ADMINISTRATIVE, SUPPORT, NURSE, or DOCTOR")
    private String role;
    
    @NotBlank(message = "Username is required")
    @Size(max = 15, message = "Username must be maximum 15 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only letters and numbers")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$", 
                message = "Password must contain uppercase, number and special character")
    private String password;
    
    // Default constructor
    public CreateUserRequest() {}
    
    // Constructor with all parameters
    public CreateUserRequest(String fullName, String idCard, String email, String phone, 
                            String birthDate, String address, String role, String username, String password) {
        this.fullName = fullName;
        this.idCard = idCard;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.username = username;
        this.password = password;
    }
    
    // Getters
    public String getFullName() { return fullName; }
    public String getIdCard() { return idCard; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(String role) { this.role = role; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "fullName='" + fullName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", password='[HIDDEN]'" +
                '}';
    }
}