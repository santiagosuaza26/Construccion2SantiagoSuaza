package app.application.dto.response;

public class UserResponse {
    
    private String idCard;
    private String fullName;
    private String email;
    private String phone;
    private String birthDate;
    private String address;
    private String role;
    private String username;
    private String createdDate;
    
    // Default constructor
    public UserResponse() {}
    
    // Constructor básico (solo información esencial)
    public UserResponse(String idCard, String fullName, String role, String email) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
    }
    
    // Constructor completo
    public UserResponse(String idCard, String fullName, String email, String phone, 
                       String birthDate, String address, String role, String username, String createdDate) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.username = username;
        this.createdDate = createdDate;
    }
    
    // Getters
    public String getIdCard() { return idCard; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getCreatedDate() { return createdDate; }
    
    // Setters
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(String role) { this.role = role; }
    public void setUsername(String username) { this.username = username; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    
    // Utility methods
    public boolean isDoctor() {
        return "DOCTOR".equals(role);
    }
    
    public boolean isNurse() {
        return "NURSE".equals(role);
    }
    
    public boolean isAdministrative() {
        return "ADMINISTRATIVE".equals(role);
    }
    
    public boolean isHumanResources() {
        return "HUMAN_RESOURCES".equals(role);
    }
    
    public boolean isSupport() {
        return "SUPPORT".equals(role);
    }
    
    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }
    
    @Override
    public String toString() {
        return "UserResponse{" +
                "idCard='" + idCard + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}