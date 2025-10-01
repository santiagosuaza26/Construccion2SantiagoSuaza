package app.infrastructure.adapter.jpa.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class PatientEntity {
    @Id
    @Column(name = "id_card")
    private String idCard;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(length = 30)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "emergency_name")
    private String emergencyName;

    @Column(name = "emergency_relation")
    private String emergencyRelation;

    @Column(name = "emergency_phone")
    private String emergencyPhone;

    @Column(name = "insurance_company")
    private String insuranceCompany;

    @Column(name = "insurance_policy")
    private String insurancePolicy;

    @Column(name = "insurance_active")
    private boolean insuranceActive;

    @Column(name = "insurance_end_date")
    private LocalDate insuranceEndDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @jakarta.persistence.Version
    private Long version;

    // Constructor por defecto requerido por JPA
    public PatientEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.insuranceActive = false;
    }

    // Constructor con parámetros
    public PatientEntity(String idCard, String fullName, LocalDate birthDate, String gender,
                        String address, String phone, String email, String username, String password) {
        this();
        this.idCard = idCard;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters and setters con auditoría automática
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
    public void setInsuranceEndDate(LocalDate insuranceEndDate) {
        this.insuranceEndDate = insuranceEndDate;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}