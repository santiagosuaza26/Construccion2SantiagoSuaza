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

    /**
     * FACTURACIÓN SEGÚN REQUERIMIENTOS DEL DOCUMENTO:
     * - Muestra información del paciente (nombre, edad, cédula)
     * - Incluye información del médico tratante
     * - Detalla información del seguro y vigencia
     * - Aplica reglas de copago específicas
     */
    public Invoice generateInvoice(String orderNumber, OrderHeader header, 
                                    String patientName, String doctorName, LocalDate today) {
        Patient patient = findPatientOrThrow(header.getPatientIdCard());
        
        List<OrderItem> items = orderItemRepository.findByOrderNumber(orderNumber);
        validateOrderItemsNotEmpty(items, orderNumber);
        validateBillingRules(patient, items, today);
        
        List<InvoiceLine> invoiceLines = createDetailedInvoiceLines(items);
        Money subtotal = calculateSubtotal(invoiceLines);
        
        InsurancePolicy policy = patient.getInsurancePolicy();
        Money currentYearCopayPaid = getCurrentYearCopayPaid(patient.getIdCard(), today.getYear());
        Money copay = calculateCopayWithRules(policy, subtotal, currentYearCopayPaid, today);
        
        String invoiceId = invoiceRepository.nextInvoiceId();
        int patientAge = calculateAge(patient.getBirthDate(), today);
        
        Invoice invoice = createDetailedInvoice(
            invoiceId, patientName, patientAge, header.getPatientIdCard(),
            doctorName, policy, invoiceLines, copay, subtotal, today
        );
        
        return invoiceRepository.save(invoice);
    }
    
    /**
     * CREACIÓN DE LÍNEAS DE FACTURA DETALLADAS SEGÚN DOCUMENTO:
     * - En caso de medicamentos: incluir nombre, costo y dosis
     * - En caso de procedimientos: incluir nombre del procedimiento
     * - En caso de ayudas diagnósticas: incluir nombre del examen
     */
    private List<InvoiceLine> createDetailedInvoiceLines(List<OrderItem> items) {
        List<InvoiceLine> lines = new ArrayList<>();
        
        for (OrderItem item : items) {
            InvoiceLine line = createDetailedInvoiceLineFromItem(item);
            lines.add(line);
        }
        
        return lines;
    }
    
    private InvoiceLine createDetailedInvoiceLineFromItem(OrderItem item) {
        if (item instanceof MedicationOrderItem) {
            MedicationOrderItem med = (MedicationOrderItem) item;
            // "En caso de haber aplicado medicamentos incluir nombre del medicamento, costo y dosis aplicadas"
            String description = String.format("Medicamento: %s - Dosis: %s - Duración: %s", 
                med.getMedicationName(), 
                med.getDosage() != null ? med.getDosage() : "N/A", 
                med.getTreatmentDuration() != null ? med.getTreatmentDuration() : "N/A");
            return new InvoiceLine(description, med.getCost());
            
        } else if (item instanceof ProcedureOrderItem) {
            ProcedureOrderItem proc = (ProcedureOrderItem) item;
            // "En caso de haber recibido procedimientos debe incluir el nombre del procedimiento"
            String description = String.format("Procedimiento: %s - Cantidad: %d - Frecuencia: %s", 
                proc.getProcedureName(), 
                proc.getQuantity(), 
                proc.getFrequency() != null ? proc.getFrequency() : "N/A");
            return new InvoiceLine(description, proc.getCost());
            
        } else if (item instanceof DiagnosticOrderItem) {
            DiagnosticOrderItem diag = (DiagnosticOrderItem) item;
            // "En caso de aplicarse una ayuda diagnóstica, debe incluir el nombre del examen aplicado"
            String description = String.format("Ayuda Diagnóstica: %s - Cantidad: %d", 
                diag.getDiagnosticName(), diag.getQuantity());
            return new InvoiceLine(description, diag.getCost());
            
        } else {
            throw new IllegalArgumentException("Unknown order item type: " + item.getClass());
        }
    }
    
    /**
     * CÁLCULO DE COPAGO SEGÚN REGLAS DEL DOCUMENTO:
     * - Póliza activa: copago de $50,000 y resto se cobra a aseguradora
     * - Si copago anual > $1,000,000: no paga más copago hasta el siguiente año
     * - Póliza inactiva o sin póliza: paciente paga todo
     */
    private Money calculateCopayWithRules(InsurancePolicy policy, Money subtotal, 
                                            Money currentYearCopayPaid, LocalDate today) {
        
        // "Cuando la póliza del paciente se encuentra inactiva o no posee, 
        // este deberá pagar el total de los servicios prestados"
        if (!insuranceCalculationService.isPolicyActiveAndValid(policy, today)) {
            return subtotal; // Patient pays everything
        }
        
        // "Cuando en un mismo año, el paciente ha recibido atenciones que superan 
        // un copago total superior a un millón de pesos, no pagara más el copago 
        // hasta el inicio del siguiente año"
        if (currentYearCopayPaid.isGreaterThanOrEqual(COPAY_YEARLY_CAP)) {
            return Money.zero(); // No more copay this year, insurance covers all
        }
        
        // "Cuando la póliza se encuentra activa, se genera una facturación de un 
        // copago de $50.000 pesos y el resto se cobra a la aseguradora"
        Money remainingCapacity = COPAY_YEARLY_CAP.subtract(currentYearCopayPaid);
        
        // Calculate actual copay: minimum of standard copay, remaining capacity, or total amount
        Money actualCopay = COPAY_AMOUNT;
        if (actualCopay.isGreaterThan(remainingCapacity)) {
            actualCopay = remainingCapacity;
        }
        if (actualCopay.isGreaterThan(subtotal)) {
            actualCopay = subtotal;
        }
        
        return actualCopay;
    }
    
    /**
     * CREACIÓN DE FACTURA CON TODOS LOS DETALLES REQUERIDOS:
     * - Información del paciente (nombre, edad, cédula)
     * - Nombre del médico tratante  
     * - Información de la compañía de seguros
     * - Número de póliza
     * - Días de vigencia de la póliza
     * - Fecha de finalización de la póliza
     */
    private Invoice createDetailedInvoice(String invoiceId, String patientName, int patientAge,
                                            String patientIdCard, String doctorName, 
                                            InsurancePolicy policy, List<InvoiceLine> lines, 
                                            Money copay, Money total, LocalDate today) {
        
        String insuranceCompany = policy != null ? policy.getCompany() : "SIN SEGURO";
        String policyNumber = policy != null ? policy.getPolicyNumber() : "N/A";
        int remainingDays = policy != null ? (int) policy.remainingDaysFrom(today) : 0;
        LocalDate policyEndDate = policy != null ? policy.getEndDate() : null;
        
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
            copay.getAmount(),
            total.getAmount()
        );
    }
    
    // Métodos auxiliares existentes...
    
    private Patient findPatientOrThrow(String patientIdCard) {
        return patientRepository.findByIdCard(patientIdCard)
            .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientIdCard));
    }
    
    private void validateOrderItemsNotEmpty(List<OrderItem> items, String orderNumber) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("No items found for order: " + orderNumber);
        }
    }
    
    private Money calculateSubtotal(List<InvoiceLine> lines) {
        return lines.stream()
            .map(line -> Money.of(line.getAmount()))
            .reduce(Money.zero(), Money::add);
    }
    
    private Money getCurrentYearCopayPaid(String patientIdCard, int year) {
        long copayPaidLong = patientRepository.totalCopayPaidInYear(patientIdCard, year);
        return Money.of(copayPaidLong);
    }
    
    private int calculateAge(LocalDate birthDate, LocalDate referenceDate) {
        return Period.between(birthDate, referenceDate).getYears();
    }
    
    /**
     * VALIDACIONES ESPECÍFICAS DE FACTURACIÓN
     */
    private void validateBillingRules(Patient patient, List<OrderItem> items, LocalDate billingDate) {
        // Validar que hay items para facturar
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create invoice without items");
        }
        
        // Validar que todos los items tienen costos válidos
        boolean hasInvalidCosts = items.stream()
            .anyMatch(this::hasInvalidCost);
            
        if (hasInvalidCosts) {
            throw new IllegalArgumentException("All items must have valid costs for billing");
        }
        
        // Validar información del paciente necesaria para facturación
        if (patient.getFullName() == null || patient.getFullName().isBlank()) {
            throw new IllegalArgumentException("Patient name required for billing");
        }
        
        if (patient.getIdCard() == null || patient.getIdCard().isBlank()) {
            throw new IllegalArgumentException("Patient ID card required for billing");
        }
        
        // Si tiene seguro, validar que la información esté completa
        InsurancePolicy policy = patient.getInsurancePolicy();
        if (policy != null) {
            validateInsurancePolicyForBilling(policy, billingDate);
        }
    }
    
    private void validateInsurancePolicyForBilling(InsurancePolicy policy, LocalDate billingDate) {
        if (policy.getCompany() == null || policy.getCompany().isBlank()) {
            throw new IllegalArgumentException("Insurance company name required for billing");
        }
        
        if (policy.getPolicyNumber() == null || policy.getPolicyNumber().isBlank()) {
            throw new IllegalArgumentException("Policy number required for billing");
        }
        
        // Warning si la póliza está vencida (pero permitir facturación)
        if (policy.getEndDate() != null && billingDate.isAfter(policy.getEndDate())) {
            // Log warning but allow billing - patient will pay full amount
            System.out.println("WARNING: Billing after policy expiration. Patient will pay full amount.");
        }
    }
    
    private boolean hasInvalidCost(OrderItem item) {
        if (item instanceof MedicationOrderItem) {
            return ((MedicationOrderItem) item).getCost() < 0;
        } else if (item instanceof ProcedureOrderItem) {
            return ((ProcedureOrderItem) item).getCost() < 0;
        } else if (item instanceof DiagnosticOrderItem) {
            return ((DiagnosticOrderItem) item).getCost() < 0;
        }
        return true;
    }
    
    /**
     * MÉTODOS DE UTILIDAD PARA REPORTES
     */
    public Money calculateTotalForOrder(String orderNumber) {
        List<OrderItem> items = orderItemRepository.findByOrderNumber(orderNumber);
        List<InvoiceLine> lines = createDetailedInvoiceLines(items);
        return calculateSubtotal(lines);
    }
    
    public Money calculateCopayForPatient(String patientIdCard, Money orderTotal, LocalDate date) {
        Patient patient = findPatientOrThrow(patientIdCard);
        InsurancePolicy policy = patient.getInsurancePolicy();
        Money currentYearCopayPaid = getCurrentYearCopayPaid(patientIdCard, date.getYear());
        
        return calculateCopayWithRules(policy, orderTotal, currentYearCopayPaid, date);
    }
    
    /**
     * Calcular cobertura del seguro (lo que paga la aseguradora)
     */
    public Money calculateInsuranceCoverage(String orderNumber, String patientIdCard, LocalDate date) {
        Money totalCost = calculateTotalForOrder(orderNumber);
        Money copay = calculateCopayForPatient(patientIdCard, totalCost, date);
        
        return totalCost.subtract(copay);
    }
}