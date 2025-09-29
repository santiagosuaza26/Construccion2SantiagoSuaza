package app.infrastructure.adapter.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class InsurancePolicyEntity {

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
    private app.infrastructure.adapter.jpa.entity.PatientEntity patient;

    // Constructors
    public InsurancePolicyEntity() {}

    public InsurancePolicyEntity(String policyNumber, String provider, BigDecimal coverageAmount,
                                LocalDateTime startDate, LocalDateTime endDate, app.infrastructure.adapter.jpa.entity.PatientEntity patient) {
        this.policyNumber = policyNumber;
        this.provider = provider;
        this.coverageAmount = coverageAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.patient = patient;
    }

    // Getters and Setters

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

    public app.infrastructure.adapter.jpa.entity.PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(app.infrastructure.adapter.jpa.entity.PatientEntity patient) {
        this.patient = patient;
    }
}