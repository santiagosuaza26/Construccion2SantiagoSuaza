package app.clinic.infrastructure.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * JPA entity representing patients table in the database.
 * Maps domain Patient objects to database records.
 */
@Entity
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(name = "username", nullable = false, unique = true, length = 15)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_names", nullable = false, length = 50)
    private String firstNames;

    @Column(name = "last_names", nullable = false, length = 50)
    private String lastNames;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private PatientGender gender;

    @Column(name = "address", nullable = false, length = 30)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emergency_contact_id")
    private EmergencyContactEntity emergencyContact;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private InsurancePolicyEntity insurancePolicy;

    // Default constructor
    public PatientEntity() {}

    // Constructor with parameters
    public PatientEntity(String cedula, String username, String password, String firstNames,
                        String lastNames, LocalDate birthDate, PatientGender gender, String address,
                        String phoneNumber, String email, EmergencyContactEntity emergencyContact,
                        InsurancePolicyEntity insurancePolicy) {
        this.cedula = cedula;
        this.username = username;
        this.password = password;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getFullName() {
        return firstNames + " " + lastNames;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PatientGender getGender() {
        return gender;
    }

    public void setGender(PatientGender gender) {
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

    public EmergencyContactEntity getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContactEntity emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public InsurancePolicyEntity getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicyEntity insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    /**
     * Enumeration for patient gender in the database.
     */
    public enum PatientGender {
        MASCULINO,
        FEMENINO,
        OTRO
    }
}