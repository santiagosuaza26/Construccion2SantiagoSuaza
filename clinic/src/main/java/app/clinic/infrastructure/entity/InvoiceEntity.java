package app.clinic.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA para persistencia de facturas en la base de datos.
 * Mapea la información de facturación de pacientes.
 */
@Entity
@Table(name = "invoices")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "patient_cedula", nullable = false)
    private String patientCedula;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "copayment_amount", precision = 10, scale = 2)
    private BigDecimal copaymentAmount;

    @Column(name = "insurance_coverage", precision = 10, scale = 2)
    private BigDecimal insuranceCoverage;

    @Column(name = "patient_responsibility", precision = 10, scale = 2)
    private BigDecimal patientResponsibility;

    @Column(name = "billing_date")
    private LocalDateTime billingDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "invoice_year")
    private Integer year;

    // Constructores
    public InvoiceEntity() {}

    public InvoiceEntity(String invoiceNumber, String patientCedula, BigDecimal totalAmount,
                        BigDecimal copaymentAmount, BigDecimal insuranceCoverage,
                        BigDecimal patientResponsibility, LocalDateTime billingDate,
                        LocalDateTime dueDate, InvoiceStatus status, String notes, Integer year) {
        this.invoiceNumber = invoiceNumber;
        this.patientCedula = patientCedula;
        this.totalAmount = totalAmount;
        this.copaymentAmount = copaymentAmount;
        this.insuranceCoverage = insuranceCoverage;
        this.patientResponsibility = patientResponsibility;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.status = status;
        this.notes = notes;
        this.year = year;
    }

    // Enum para el estado de la factura
    public enum InvoiceStatus {
        PENDING, PAID, OVERDUE, CANCELLED
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getCopaymentAmount() {
        return copaymentAmount;
    }

    public void setCopaymentAmount(BigDecimal copaymentAmount) {
        this.copaymentAmount = copaymentAmount;
    }

    public BigDecimal getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(BigDecimal insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }

    public BigDecimal getPatientResponsibility() {
        return patientResponsibility;
    }

    public void setPatientResponsibility(BigDecimal patientResponsibility) {
        this.patientResponsibility = patientResponsibility;
    }

    public LocalDateTime getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDateTime billingDate) {
        this.billingDate = billingDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("InvoiceEntity{id=%d, invoiceNumber='%s', patientCedula='%s', totalAmount=%s, status=%s}",
                           id, invoiceNumber, patientCedula, totalAmount, status);
    }
}