package app.infrastructure.adapter.jpa.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class PatientEntity {
    @Id
    private String idCard;

    private String fullName;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String username;
    private String password;

    private String emergencyName;
    private String emergencyRelation;
    private String emergencyPhone;

    private String insuranceCompany;
    private String insurancePolicy;
    private boolean insuranceActive;
    private LocalDate insuranceEndDate;

    // Getters and setters
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmergencyName() { return emergencyName; }
    public void setEmergencyName(String emergencyName) { this.emergencyName = emergencyName; }
    public String getEmergencyRelation() { return emergencyRelation; }
    public void setEmergencyRelation(String emergencyRelation) { this.emergencyRelation = emergencyRelation; }
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    public String getInsuranceCompany() { return insuranceCompany; }
    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }
    public String getInsurancePolicy() { return insurancePolicy; }
    public void setInsurancePolicy(String insurancePolicy) { this.insurancePolicy = insurancePolicy; }
    public boolean isInsuranceActive() { return insuranceActive; }
    public void setInsuranceActive(boolean insuranceActive) { this.insuranceActive = insuranceActive; }
    public LocalDate getInsuranceEndDate() { return insuranceEndDate; }
    public void setInsuranceEndDate(LocalDate insuranceEndDate) { this.insuranceEndDate = insuranceEndDate; }
}