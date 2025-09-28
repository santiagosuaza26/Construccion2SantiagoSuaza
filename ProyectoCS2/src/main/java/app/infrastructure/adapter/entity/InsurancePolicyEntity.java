package app.infrastructure.adapter.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "coverage_amount", precision = 15, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    // Constructors
    public InsurancePolicyEntity() {}

    public InsurancePolicyEntity(String policyNumber, String provider, BigDecimal coverageAmount,
                                LocalDateTime startDate, LocalDateTime endDate, PatientEntity patient) {
        this.policyNumber = policyNumber;
        this.provider = provider;
        this.coverageAmount = coverageAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.patient = patient;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }
}