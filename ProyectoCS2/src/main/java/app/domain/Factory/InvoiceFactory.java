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
import app.domain.model.Money;
import app.domain.model.OrderItem;
import app.domain.model.Patient;
import app.domain.model.ProcedureOrderItem;

public class InvoiceFactory {
    
    public Invoice createInvoice(String invoiceId, Patient patient, String doctorName,
                                List<OrderItem> orderItems, Money totalCopayPaidThisYear,
                                LocalDate invoiceDate) {
        
        List<InvoiceLine> lines = createInvoiceLines(orderItems);
        Money subtotal = calculateSubtotal(lines);
        
        InsurancePolicy policy = patient.getInsurancePolicy();
        Money copay = calculateCopay(policy, subtotal, totalCopayPaidThisYear);
        
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
            copay.getAmount(), // Convertir a long para Invoice
            subtotal.getAmount() // Convertir a long para Invoice
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
    
    private Money calculateSubtotal(List<InvoiceLine> lines) {
        return lines.stream()
            .map(line -> Money.of(line.getAmount()))
            .reduce(Money.zero(), Money::add);
    }
    
    private Money calculateCopay(InsurancePolicy policy, Money subtotal, Money copayPaidThisYear) {
        if (policy == null || !policy.isActive()) {
            return subtotal; // Patient pays everything
        }
        
        Money copayLimit = Money.of(1_000_000L); // 1 million pesos
        Money standardCopay = Money.of(50_000L); // 50k pesos
        
        if (copayPaidThisYear.isGreaterThanOrEqual(copayLimit)) {
            return Money.zero(); // No more copay this year
        }
        
        Money remainingCapacity = copayLimit.subtract(copayPaidThisYear);
        
        // Return minimum of: standard copay, remaining capacity, or subtotal
        Money result = standardCopay;
        if (result.isGreaterThan(remainingCapacity)) {
            result = remainingCapacity;
        }
        if (result.isGreaterThan(subtotal)) {
            result = subtotal;
        }
        
        return result;
    }
}