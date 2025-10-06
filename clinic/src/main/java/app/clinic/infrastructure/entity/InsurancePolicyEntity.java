package app.clinic.infrastructure.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing insurance policies table in the database.
 * Maps domain InsurancePolicy objects to database records.
 */
@Entity
@Table(name = "insurance_policies")
public class InsurancePolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "policy_number", nullable = false, length = 50)
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PolicyStatus status;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    // Default constructor
    public InsurancePolicyEntity() {}

    // Constructor with parameters
    public InsurancePolicyEntity(String companyName, String policyNumber, PolicyStatus status, LocalDate expirationDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.status = status;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return status == PolicyStatus.ACTIVE && expirationDate.isAfter(LocalDate.now());
    }

    /**
     * Enumeration for policy status in the database.
     */
    public enum PolicyStatus {
        ACTIVE,
        INACTIVE
    }
}