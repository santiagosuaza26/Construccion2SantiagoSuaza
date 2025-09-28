package app.infrastructure.adapter.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class PatientEntity {

    @Id
    private String idCard;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(length = 30)
    private String address;

    @Column(nullable = false, length = 10)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    private CredentialsEntity credentials;

    @Embedded
    private EmergencyContactEntity emergencyContact;

    @Embedded
    private InsurancePolicyEntity insurancePolicy;

    // Constructores
    public PatientEntity() {}

    public PatientEntity(String idCard, String fullName, LocalDate birthDate, String gender,
                        String address, String phone, String email, CredentialsEntity credentials,
                        EmergencyContactEntity emergencyContact, InsurancePolicyEntity insurancePolicy) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.credentials = credentials;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
    }

    // Getters y Setters
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

    public CredentialsEntity getCredentials() { return credentials; }
    public void setCredentials(CredentialsEntity credentials) { this.credentials = credentials; }

    public EmergencyContactEntity getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(EmergencyContactEntity emergencyContact) { this.emergencyContact = emergencyContact; }

    public InsurancePolicyEntity getInsurancePolicy() { return insurancePolicy; }
    public void setInsurancePolicy(InsurancePolicyEntity insurancePolicy) { this.insurancePolicy = insurancePolicy; }
}