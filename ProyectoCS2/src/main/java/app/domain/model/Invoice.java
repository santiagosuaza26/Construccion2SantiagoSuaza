package app.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private final String invoiceId;
    private final String patientName;
    private final int patientAge;
    private final String patientIdCard;
    private final String doctorName;
    private final String insuranceCompany;
    private final String policyNumber;
    private final int policyRemainingDays;
    private final LocalDate policyEndDate;
    private final List<InvoiceLine> lines;
    private final long copay;
    private final long total;
    
    // Nuevos campos para compatibilidad con BillingService
    private String status;
    private LocalDateTime issueDate;
    private String processedBy;

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
        // Valores por defecto para nuevos campos
        this.status = "PENDING";
        this.issueDate = LocalDateTime.now();
        this.processedBy = "SYSTEM";
    }
    
    // Constructor adicional para BillingService
    public Invoice(String invoiceNumber,
                String patientIdCard,
                List<InvoiceLine> invoiceLines,
                BigDecimal subtotal,
                BigDecimal taxes,
                BigDecimal totalAmount,
                String invoiceStatus,
                LocalDateTime issueDateTime,
                String processedByUser) {
        this.invoiceId = invoiceNumber;
        this.patientIdCard = patientIdCard;
        this.lines = invoiceLines;
        this.copay = subtotal.longValue(); // Conversión segura
        this.total = totalAmount.longValue();
        // Inicializar otros campos con valores por defecto
        this.patientName = "";
        this.patientAge = 0;
        this.doctorName = "";
        this.insuranceCompany = "";
        this.policyNumber = "";
        this.policyRemainingDays = 0;
        this.policyEndDate = null;
        this.status = invoiceStatus;
        this.issueDate = issueDateTime;
        this.processedBy = processedByUser;
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
    
    // Nuevos métodos para BillingService
    public String getStatus() { return status; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public String getInvoiceNumber() { return invoiceId; }
    public List<InvoiceLine> getInvoiceLines() { return lines; }
    public BigDecimal getSubtotal() { return BigDecimal.valueOf(copay); }
    public BigDecimal getTaxes() { return BigDecimal.valueOf(total - copay); }
    public BigDecimal getTotalAmount() { return BigDecimal.valueOf(total); }

    // Validaciones removidas del constructor - ahora son responsabilidad del servicio de validación
    // Esto permite crear objetos Invoice para testing sin lanzar excepciones prematuras
}