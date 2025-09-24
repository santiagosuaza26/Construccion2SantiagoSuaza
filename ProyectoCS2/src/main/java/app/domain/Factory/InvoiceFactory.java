package app.domain.factory;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import app.domain.model.DiagnosticOrderItem;
import app.domain.model.InsurancePolicy;
import app.domain.model.Invoice;
import app.domain.model.InvoiceLine;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderItem;
import app.domain.model.Patient;
import app.domain.model.ProcedureOrderItem;

public class InvoiceFactory {
    
    public Invoice createInvoice(String invoiceId, Patient patient, String doctorName,
                                List<OrderItem> orderItems, long totalCopayPaidThisYear,
                                LocalDate invoiceDate) {
        
        List<InvoiceLine> lines = createInvoiceLines(orderItems);
        long subtotal = calculateSubtotal(lines);
        
        InsurancePolicy policy = patient.getInsurancePolicy();
        long copay = calculateCopay(policy, subtotal, totalCopayPaidThisYear);
        
        int age = Period.between(patient.getBirthDate(), invoiceDate).getYears();
        
        return new Invoice(
            invoiceId,
            patient.getFullName(),
            age,
            patient.getIdCard(),
            doctorName,
            policy != null ? policy.getCompany() : null,
            policy != null ? policy.getPolicyNumber() : null,
            policy != null ? (int) policy.remainingDaysFrom(invoiceDate) : 0,
            policy != null ? policy.getEndDate() : null,
            lines,
            copay,
            subtotal
        );
    }
    
    private List<InvoiceLine> createInvoiceLines(List<OrderItem> items) {
        List<InvoiceLine> lines = new ArrayList<>();

        for (OrderItem item : items) {
            if (item instanceof MedicationOrderItem) {
                MedicationOrderItem med = (MedicationOrderItem) item;
                lines.add(new InvoiceLine("Medication: " + med.getMedicationName(), med.getCost()));
            } else if (item instanceof ProcedureOrderItem) {
                ProcedureOrderItem proc = (ProcedureOrderItem) item;
                lines.add(new InvoiceLine("Procedure: " + proc.getProcedureName(), proc.getCost()));
            } else if (item instanceof DiagnosticOrderItem) {
                DiagnosticOrderItem diag = (DiagnosticOrderItem) item;
                lines.add(new InvoiceLine("Diagnostic: " + diag.getDiagnosticName(), diag.getCost()));
            }
        }
        return lines;
    }
    
    private long calculateSubtotal(List<InvoiceLine> lines) {
        return lines.stream().mapToLong(InvoiceLine::getAmount).sum();
    }
    
    private long calculateCopay(InsurancePolicy policy, long subtotal, long copayPaidThisYear) {
        if (policy == null || !policy.isActive()) {
            return subtotal; // Patient pays everything
        }
        
        long copayLimit = 1_000_000L; // 1 million pesos
        long standardCopay = 50_000L; // 50k pesos
        
        if (copayPaidThisYear >= copayLimit) {
            return 0; // No more copay this year
        }
        
        return Math.min(standardCopay, copayLimit - copayPaidThisYear);
    }
}