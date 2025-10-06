package app.clinic.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Entidad JPA para detalles específicos de facturación.
 * Contiene información detallada sobre órdenes médicas y procedimientos incluidos en una factura.
 */
@Entity
@Table(name = "billing_details")
public class BillingDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_cedula", nullable = false)
    private String patientCedula;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "patient_full_name")
    private String patientFullName;

    @Column(name = "patient_age")
    private Integer patientAge;

    @Column(name = "insurance_company")
    private String insuranceCompany;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "policy_expiration_date")
    private LocalDate policyExpirationDate;

    @Column(name = "doctor_cedula")
    private String doctorCedula;

    @Column(name = "consultation_date")
    private LocalDate consultationDate;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment_cost", precision = 10, scale = 2)
    private BigDecimal treatmentCost;

    @Column(name = "medication_cost", precision = 10, scale = 2)
    private BigDecimal medicationCost;

    @Column(name = "procedure_cost", precision = 10, scale = 2)
    private BigDecimal procedureCost;

    @Column(name = "diagnostic_aid_cost", precision = 10, scale = 2)
    private BigDecimal diagnosticAidCost;

    @ElementCollection
    @CollectionTable(name = "billing_order_summaries", joinColumns = @JoinColumn(name = "billing_details_id"))
    @Column(name = "order_summary")
    private List<String> orderSummaries = new ArrayList<>();

    @Column(name = "created_date")
    private LocalDate createdDate;

    // Constructores
    public BillingDetailsEntity() {}

    public BillingDetailsEntity(String patientCedula, String invoiceNumber, String patientFullName,
                               Integer patientAge, String insuranceCompany, String policyNumber,
                               LocalDate policyExpirationDate, String doctorCedula,
                               LocalDate consultationDate, String diagnosis,
                               BigDecimal treatmentCost, BigDecimal medicationCost,
                               BigDecimal procedureCost, BigDecimal diagnosticAidCost,
                               List<String> orderSummaries) {
        this.patientCedula = patientCedula;
        this.invoiceNumber = invoiceNumber;
        this.patientFullName = patientFullName;
        this.patientAge = patientAge;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.policyExpirationDate = policyExpirationDate;
        this.doctorCedula = doctorCedula;
        this.consultationDate = consultationDate;
        this.diagnosis = diagnosis;
        this.treatmentCost = treatmentCost;
        this.medicationCost = medicationCost;
        this.procedureCost = procedureCost;
        this.diagnosticAidCost = diagnosticAidCost;
        this.orderSummaries = orderSummaries != null ? orderSummaries : new ArrayList<>();
        this.createdDate = LocalDate.now();
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public LocalDate getPolicyExpirationDate() {
        return policyExpirationDate;
    }

    public void setPolicyExpirationDate(LocalDate policyExpirationDate) {
        this.policyExpirationDate = policyExpirationDate;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public LocalDate getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDate consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public BigDecimal getMedicationCost() {
        return medicationCost;
    }

    public void setMedicationCost(BigDecimal medicationCost) {
        this.medicationCost = medicationCost;
    }

    public BigDecimal getProcedureCost() {
        return procedureCost;
    }

    public void setProcedureCost(BigDecimal procedureCost) {
        this.procedureCost = procedureCost;
    }

    public BigDecimal getDiagnosticAidCost() {
        return diagnosticAidCost;
    }

    public void setDiagnosticAidCost(BigDecimal diagnosticAidCost) {
        this.diagnosticAidCost = diagnosticAidCost;
    }

    public List<String> getOrderSummaries() {
        return orderSummaries;
    }

    public void setOrderSummaries(List<String> orderSummaries) {
        this.orderSummaries = orderSummaries;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return String.format("BillingDetailsEntity{patientCedula='%s', invoiceNumber='%s', patientName='%s'}",
                           patientCedula, invoiceNumber, patientFullName);
    }
}