package app.application.dto;

import app.domain.model.Invoice;
import app.domain.model.InvoiceLine;

import java.time.LocalDate;
import java.util.List;

public class InvoiceResponse {
    public String invoiceId;
    public String patientName;
    public int patientAge;
    public String patientIdCard;
    public String doctorName;
    public String insuranceCompany;
    public String policyNumber;
    public int policyRemainingDays;
    public LocalDate policyEndDate;
    public List<InvoiceLine> lines;
    public long copay;
    public long total;

    public static InvoiceResponse fromDomain(Invoice invoice) {
        InvoiceResponse dto = new InvoiceResponse();
        dto.invoiceId = invoice.getInvoiceId();
        dto.patientName = invoice.getPatientName();
        dto.patientAge = invoice.getPatientAge();
        dto.patientIdCard = invoice.getPatientIdCard();
        dto.doctorName = invoice.getDoctorName();
        dto.insuranceCompany = invoice.getInsuranceCompany();
        dto.policyNumber = invoice.getPolicyNumber();
        dto.policyRemainingDays = invoice.getPolicyRemainingDays();
        dto.policyEndDate = invoice.getPolicyEndDate();
        dto.lines = invoice.getLines();
        dto.copay = invoice.getCopay();
        dto.total = invoice.getTotal();
        return dto;
    }
}