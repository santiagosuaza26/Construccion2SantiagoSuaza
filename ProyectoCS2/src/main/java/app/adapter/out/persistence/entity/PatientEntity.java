package app.adapter.out.persistence.entity;

import java.time.LocalDate;

import app.domain.model.Credentials;
import app.domain.model.EmergencyContact;
import app.domain.model.InsurancePolicy;
import app.domain.model.Patient;
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
    
    @Column(name = "gender", nullable = false)
    private String gender;
    
    @Column(name = "address", nullable = false, length = 30)
    private String address;
    
    @Column(name = "phone", nullable = false, length = 10)
    private String phone;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "username", nullable = false, unique = true, length = 15)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    // Emergency contact fields
    @Column(name = "emergency_first_name")
    private String emergencyFirstName;
    
    @Column(name = "emergency_last_name")
    private String emergencyLastName;
    
    @Column(name = "emergency_relationship")
    private String emergencyRelationship;
    
    @Column(name = "emergency_phone", length = 10)
    private String emergencyPhone;
    
    // Insurance policy fields
    @Column(name = "insurance_company")
    private String insuranceCompany;
    
    @Column(name = "policy_number")
    private String policyNumber;
    
    @Column(name = "policy_active")
    private Boolean policyActive;
    
    @Column(name = "policy_end_date")
    private LocalDate policyEndDate;

    public PatientEntity() {}

    public Patient toDomain() {
        Credentials credentials = new Credentials(username, password);
        
        EmergencyContact emergencyContact = null;
        if (emergencyFirstName != null) {
            emergencyContact = new EmergencyContact(emergencyFirstName, emergencyLastName, 
                                                    emergencyRelationship, emergencyPhone);
        }
        
        InsurancePolicy insurancePolicy = null;
        if (insuranceCompany != null) {
            insurancePolicy = new InsurancePolicy(insuranceCompany, policyNumber, 
                                                    policyActive != null ? policyActive : false, policyEndDate);
        }
        
        return new Patient(idCard, fullName, birthDate, gender, address, phone, email,
                            credentials, emergencyContact, insurancePolicy);
    }

    public static PatientEntity fromDomain(Patient patient) {
        PatientEntity entity = new PatientEntity();
        entity.idCard = patient.getIdCard();
        entity.fullName = patient.getFullName();
        entity.birthDate = patient.getBirthDate();
        entity.gender = patient.getGender();
        entity.address = patient.getAddress();
        entity.phone = patient.getPhone();
        entity.email = patient.getEmail();
        entity.username = patient.getCredentials().getUsername();
        entity.password = patient.getCredentials().getPassword();
        
        if (patient.getEmergencyContact() != null) {
            EmergencyContact ec = patient.getEmergencyContact();
            entity.emergencyFirstName = ec.getFirstName();
            entity.emergencyLastName = ec.getLastName();
            entity.emergencyRelationship = ec.getRelationship();
            entity.emergencyPhone = ec.getPhone();
        }
        
        if (patient.getInsurancePolicy() != null) {
            InsurancePolicy ip = patient.getInsurancePolicy();
            entity.insuranceCompany = ip.getCompany();
            entity.policyNumber = ip.getPolicyNumber();
            entity.policyActive = ip.isActive();
            entity.policyEndDate = ip.getEndDate();
        }
        
        return entity;
    }

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
}