package app.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Invoice {
    private final String invoiceId;
    private final String patientName;
    private final int patientAge;
    private final String patientIdCard;
    private final String doctorName;
    private final String insuranceCompany; // nullable
    private final String policyNumber;     // nullable
    private final int policyRemainingDays; // computed at generation time
    private final LocalDate policyEndDate; // nullable
    private final List<InvoiceLine> lines;
    private final long copay;
    private final long total;

    public Invoice(String invoiceId,
                String patientName,
                int patientAge,
                String patientIdCard,
                String doctorName,
                String insuranceCompany,
                String policyNumber,
                int policyRemainingDays,
                LocalDate policyEndDate,
                List<InvoiceLine> lines,
                long copay,
                long total) {
        this.invoiceId = invoiceId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientIdCard = patientIdCard;
        this.doctorName = doctorName;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.policyRemainingDays = policyRemainingDays;
        this.policyEndDate = policyEndDate;
        this.lines = lines;
        this.copay = copay;
        this.total = total;
        validate();
    }

    public String getInvoiceId() { return invoiceId; }
    public String getPatientName() { return patientName; }
    public int getPatientAge() { return patientAge; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorName() { return doctorName; }
    public String getInsuranceCompany() { return insuranceCompany; }
    public String getPolicyNumber() { return policyNumber; }
    public int getPolicyRemainingDays() { return policyRemainingDays; }
    public LocalDate getPolicyEndDate() { return policyEndDate; }
    public List<InvoiceLine> getLines() { return lines; }
    public long getCopay() { return copay; }
    public long getTotal() { return total; }

    private void validate() {
        if (invoiceId == null || invoiceId.isBlank()) throw new IllegalArgumentException("Invoice id required");
        if (patientName == null || patientName.isBlank()) throw new IllegalArgumentException("Patient name required");
        if (patientAge < 0 || patientAge > 150) throw new IllegalArgumentException("Invalid age");
        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("Lines required");
        if (copay < 0 || total < 0) throw new IllegalArgumentException("Invalid amounts");
    }
}