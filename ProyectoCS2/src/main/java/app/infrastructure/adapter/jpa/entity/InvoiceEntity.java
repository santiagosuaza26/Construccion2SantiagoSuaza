package app.infrastructure.adapter.jpa.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {
    @Id
    private String invoiceId;

    private String patientName;
    private int patientAge;
    private String patientIdCard;
    private String doctorName;
    private String insuranceCompany;
    private String policyNumber;
    private int policyRemainingDays;
    private LocalDate policyEndDate;

    private long copay;
    private long total;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLineEntity> lines;

    // Getters and setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public int getPatientAge() { return patientAge; }
    public void setPatientAge(int patientAge) { this.patientAge = patientAge; }
    public String getPatientIdCard() { return patientIdCard; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getInsuranceCompany() { return insuranceCompany; }
    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public int getPolicyRemainingDays() { return policyRemainingDays; }
    public void setPolicyRemainingDays(int policyRemainingDays) { this.policyRemainingDays = policyRemainingDays; }
    public LocalDate getPolicyEndDate() { return policyEndDate; }
    public void setPolicyEndDate(LocalDate policyEndDate) { this.policyEndDate = policyEndDate; }
    public long getCopay() { return copay; }
    public void setCopay(long copay) { this.copay = copay; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public List<InvoiceLineEntity> getLines() { return lines; }
    public void setLines(List<InvoiceLineEntity> lines) { this.lines = lines; }
}