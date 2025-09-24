package app.domain.services;

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
import app.domain.model.OrderHeader;
import app.domain.model.OrderItem;
import app.domain.model.Patient;
import app.domain.model.ProcedureOrderItem;
import app.domain.port.InvoiceRepository;
import app.domain.port.OrderItemRepository;
import app.domain.port.PatientRepository;

public class BillingService {
    private static final Money COPAY_AMOUNT = Money.of(50_000L);
    private static final Money COPAY_YEARLY_CAP = Money.of(1_000_000L);

    private final InvoiceRepository invoiceRepository;
    private final OrderItemRepository orderItemRepository;
    private final PatientRepository patientRepository;
    private final InsuranceCalculationService insuranceCalculationService;

    public BillingService(InvoiceRepository invoiceRepository,
                        OrderItemRepository orderItemRepository,
                        PatientRepository patientRepository,
                        InsuranceCalculationService insuranceCalculationService) {
        this.invoiceRepository = invoiceRepository;
        this.orderItemRepository = orderItemRepository;
        this.patientRepository = patientRepository;
        this.insuranceCalculationService = insuranceCalculationService;
    }

    public Invoice generateInvoice(String orderNumber, OrderHeader header, 
                                    String patientName, String doctorName, LocalDate today) {
        Patient patient = findPatientOrThrow(header.getPatientIdCard());
        
        List<OrderItem> items = orderItemRepository.findByOrderNumber(orderNumber);
        validateOrderItemsNotEmpty(items, orderNumber);
        
        List<InvoiceLine> invoiceLines = createInvoiceLines(items);
        Money subtotal = calculateSubtotal(invoiceLines);
        
        InsurancePolicy policy = patient.getInsurancePolicy();
        Money currentYearCopayPaid = getCurrentYearCopayPaid(patient.getIdCard(), today.getYear());
        Money copay = calculateCopay(policy, subtotal, currentYearCopayPaid, today);
        
        String insuranceCompany = extractInsuranceCompany(policy);
        String policyNumber = extractPolicyNumber(policy);
        int remainingDays = calculateRemainingDays(policy, today);
        LocalDate policyEndDate = extractPolicyEndDate(policy);
        
        int patientAge = calculateAge(patient.getBirthDate(), today);
        
        String invoiceId = invoiceRepository.nextInvoiceId();
        
        Invoice invoice = createInvoiceEntity(
            invoiceId, patientName, patientAge, header.getPatientIdCard(),
            doctorName, insuranceCompany, policyNumber, remainingDays,
            policyEndDate, invoiceLines, copay, subtotal
        );
        
        return invoiceRepository.save(invoice);
    }
    
    public Money calculateTotalForOrder(String orderNumber) {
        List<OrderItem> items = orderItemRepository.findByOrderNumber(orderNumber);
        List<InvoiceLine> lines = createInvoiceLines(items);
        return calculateSubtotal(lines);
    }
    
    public Money calculateCopayForPatient(String patientIdCard, Money orderTotal, LocalDate date) {
        Patient patient = findPatientOrThrow(patientIdCard);
        InsurancePolicy policy = patient.getInsurancePolicy();
        Money currentYearCopayPaid = getCurrentYearCopayPaid(patientIdCard, date.getYear());
        
        return calculateCopay(policy, orderTotal, currentYearCopayPaid, date);
    }
    
    private Patient findPatientOrThrow(String patientIdCard) {
        return patientRepository.findByIdCard(patientIdCard)
            .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientIdCard));
    }
    
    private void validateOrderItemsNotEmpty(List<OrderItem> items, String orderNumber) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("No items found for order: " + orderNumber);
        }
    }
    
    private List<InvoiceLine> createInvoiceLines(List<OrderItem> items) {
        List<InvoiceLine> lines = new ArrayList<>();
        
        for (OrderItem item : items) {
            InvoiceLine line = createInvoiceLineFromItem(item);
            lines.add(line);
        }
        
        return lines;
    }
    
    private InvoiceLine createInvoiceLineFromItem(OrderItem item) {
        if (item instanceof MedicationOrderItem) {
            MedicationOrderItem med = (MedicationOrderItem) item;
            return new InvoiceLine(
                String.format("Medication: %s (Dosage: %s, Duration: %s)", 
                    med.getMedicationName(), med.getDosage(), med.getTreatmentDuration()), 
                med.getCost()
            );
        } else if (item instanceof ProcedureOrderItem) {
            ProcedureOrderItem proc = (ProcedureOrderItem) item;
            return new InvoiceLine(
                String.format("Procedure: %s (Qty: %d, Frequency: %s)", 
                    proc.getProcedureName(), proc.getQuantity(), proc.getFrequency()), 
                proc.getCost()
            );
        } else if (item instanceof DiagnosticOrderItem) {
            DiagnosticOrderItem diag = (DiagnosticOrderItem) item;
            return new InvoiceLine(
                String.format("Diagnostic: %s (Qty: %d)", 
                    diag.getDiagnosticName(), diag.getQuantity()), 
                diag.getCost()
            );
        } else {
            throw new IllegalArgumentException("Unknown order item type: " + item.getClass());
        }
    }
    
    private Money calculateSubtotal(List<InvoiceLine> lines) {
        return lines.stream()
            .map(line -> Money.of(line.getAmount()))
            .reduce(Money.of(0), Money::add);
    }
    
    private Money getCurrentYearCopayPaid(String patientIdCard, int year) {
        long copayPaidLong = patientRepository.totalCopayPaidInYear(patientIdCard, year);
        return Money.of(copayPaidLong);
    }
    
    private Money calculateCopay(InsurancePolicy policy, Money subtotal, 
                            Money currentYearCopayPaid, LocalDate today) {
        long subtotalLong = subtotal.getAmount();
        long copayPaidLong = currentYearCopayPaid.getAmount();
        long copay = insuranceCalculationService.calculateCopay(
            policy, subtotalLong, copayPaidLong, today
        );
        return Money.of(copay);
    }
    
    private String extractInsuranceCompany(InsurancePolicy policy) {
        return policy != null ? policy.getCompany() : null;
    }
    
    private String extractPolicyNumber(InsurancePolicy policy) {
        return policy != null ? policy.getPolicyNumber() : null;
    }
    
    private int calculateRemainingDays(InsurancePolicy policy, LocalDate today) {
        return policy != null ? (int) policy.remainingDaysFrom(today) : 0;
    }
    
    private LocalDate extractPolicyEndDate(InsurancePolicy policy) {
        return policy != null ? policy.getEndDate() : null;
    }
    
    private int calculateAge(LocalDate birthDate, LocalDate referenceDate) {
        return Period.between(birthDate, referenceDate).getYears();
    }
    
    private Invoice createInvoiceEntity(String invoiceId, String patientName, int patientAge,
                                        String patientIdCard, String doctorName, String insuranceCompany,
                                        String policyNumber, int remainingDays, LocalDate policyEndDate,
                                        List<InvoiceLine> lines, Money copay, Money total) {
        return new Invoice(
            invoiceId,
            patientName,
            patientAge,
            patientIdCard,
            doctorName,
            insuranceCompany,
            policyNumber,
            remainingDays,
            policyEndDate,
            lines,
            copay.getAmount(), // Convert back to long for Invoice entity
            total.getAmount()  // Convert back to long for Invoice entity
        );
    }
    
    // Métodos de utilidad para reportes y consultas
    
    public Money calculateMonthlyRevenue(int year, int month) {
        // Este método requeriría un repositorio adicional para consultar facturas
        // por período de tiempo - se implementaría en la capa de aplicación
        throw new UnsupportedOperationException("Requires InvoiceQueryRepository implementation");
    }
    
    public Money calculateInsuranceCoverage(String orderNumber) {
        Money total = calculateTotalForOrder(orderNumber);
        // Necesitaríamos el patientIdCard del header para calcular el copago
        // Este es un ejemplo de cómo se usaría
        return total; // Simplified - real implementation would calculate coverage
    }
    
    // Método para validar reglas de facturación específicas
    
    private void validateBillingRules(Patient patient, List<OrderItem> items, LocalDate billingDate) {
        // Validar que el paciente tiene seguro activo si es requerido
        InsurancePolicy policy = patient.getInsurancePolicy();
        
        // Validar que no se facture después de vencimiento de póliza
        if (policy != null && policy.getEndDate() != null && 
            billingDate.isAfter(policy.getEndDate())) {
            throw new IllegalArgumentException("Cannot bill after policy expiration");
        }
        
        // Validar que hay items para facturar
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create invoice without items");
        }
        
        // Validar que todos los items tienen costos válidos
        boolean hasInvalidCosts = items.stream()
            .anyMatch(this::hasInvalidCost);
            
        if (hasInvalidCosts) {
            throw new IllegalArgumentException("All items must have valid costs");
        }
    }
    
    private boolean hasInvalidCost(OrderItem item) {
        if (item instanceof MedicationOrderItem) {
            return ((MedicationOrderItem) item).getCost() < 0;
        } else if (item instanceof ProcedureOrderItem) {
            return ((ProcedureOrderItem) item).getCost() < 0;
        } else if (item instanceof DiagnosticOrderItem) {
            return ((DiagnosticOrderItem) item).getCost() < 0;
        } else {
            return true;
        }
    }
}