package app.clinic.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA para resúmenes de facturación de pacientes.
 * Contiene información agregada sobre facturación por paciente y período.
 */
@Entity
@Table(name = "billing_summaries")
public class BillingSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_cedula", nullable = false)
    private String patientCedula;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "total_billed", precision = 15, scale = 2)
    private BigDecimal totalBilled;

    @Column(name = "total_copayment", precision = 15, scale = 2)
    private BigDecimal totalCopayment;

    @Column(name = "total_insurance_coverage", precision = 15, scale = 2)
    private BigDecimal totalInsuranceCoverage;

    @Column(name = "total_patient_responsibility", precision = 15, scale = 2)
    private BigDecimal totalPatientResponsibility;

    @Column(name = "number_of_invoices")
    private Integer numberOfInvoices;

    @Column(name = "average_invoice_amount", precision = 15, scale = 2)
    private BigDecimal averageInvoiceAmount;

    @Column(name = "last_calculation_date")
    private LocalDateTime lastCalculationDate;

    @Column(name = "copayment_limit_exceeded")
    private Boolean copaymentLimitExceeded;

    // Constructores
    public BillingSummaryEntity() {}

    public BillingSummaryEntity(String patientCedula, Integer year, BigDecimal totalBilled,
                               BigDecimal totalCopayment, BigDecimal totalInsuranceCoverage,
                               BigDecimal totalPatientResponsibility, Integer numberOfInvoices,
                               BigDecimal averageInvoiceAmount, Boolean copaymentLimitExceeded) {
        this.patientCedula = patientCedula;
        this.year = year;
        this.totalBilled = totalBilled;
        this.totalCopayment = totalCopayment;
        this.totalInsuranceCoverage = totalInsuranceCoverage;
        this.totalPatientResponsibility = totalPatientResponsibility;
        this.numberOfInvoices = numberOfInvoices;
        this.averageInvoiceAmount = averageInvoiceAmount;
        this.copaymentLimitExceeded = copaymentLimitExceeded;
        this.lastCalculationDate = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTotalBilled() {
        return totalBilled;
    }

    public void setTotalBilled(BigDecimal totalBilled) {
        this.totalBilled = totalBilled;
    }

    public BigDecimal getTotalCopayment() {
        return totalCopayment;
    }

    public void setTotalCopayment(BigDecimal totalCopayment) {
        this.totalCopayment = totalCopayment;
    }

    public BigDecimal getTotalInsuranceCoverage() {
        return totalInsuranceCoverage;
    }

    public void setTotalInsuranceCoverage(BigDecimal totalInsuranceCoverage) {
        this.totalInsuranceCoverage = totalInsuranceCoverage;
    }

    public BigDecimal getTotalPatientResponsibility() {
        return totalPatientResponsibility;
    }

    public void setTotalPatientResponsibility(BigDecimal totalPatientResponsibility) {
        this.totalPatientResponsibility = totalPatientResponsibility;
    }

    public Integer getNumberOfInvoices() {
        return numberOfInvoices;
    }

    public void setNumberOfInvoices(Integer numberOfInvoices) {
        this.numberOfInvoices = numberOfInvoices;
    }

    public BigDecimal getAverageInvoiceAmount() {
        return averageInvoiceAmount;
    }

    public void setAverageInvoiceAmount(BigDecimal averageInvoiceAmount) {
        this.averageInvoiceAmount = averageInvoiceAmount;
    }

    public LocalDateTime getLastCalculationDate() {
        return lastCalculationDate;
    }

    public void setLastCalculationDate(LocalDateTime lastCalculationDate) {
        this.lastCalculationDate = lastCalculationDate;
    }

    public Boolean getCopaymentLimitExceeded() {
        return copaymentLimitExceeded;
    }

    public void setCopaymentLimitExceeded(Boolean copaymentLimitExceeded) {
        this.copaymentLimitExceeded = copaymentLimitExceeded;
    }

    @Override
    public String toString() {
        return String.format("BillingSummaryEntity{patientCedula='%s', year=%d, totalBilled=%s, totalCopayment=%s}",
                           patientCedula, year, totalBilled, totalCopayment);
    }
}