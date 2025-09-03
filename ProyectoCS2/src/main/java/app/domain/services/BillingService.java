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
import app.domain.model.OrderHeader;
import app.domain.model.OrderItem;
import app.domain.model.Patient;
import app.domain.model.ProcedureOrderItem;
import app.domain.port.InvoiceRepository;
import app.domain.port.OrderItemRepository;
import app.domain.port.PatientRepository;

public class BillingService {
    private static final long COPAY_AMOUNT = 50_000L;
    private static final long COPAY_YEARLY_CAP = 1_000_000L;

    private final InvoiceRepository invoiceRepository;
    private final OrderItemRepository orderItemRepository;
    private final PatientRepository patientRepository;

    public BillingService(InvoiceRepository invoiceRepository,
                        OrderItemRepository orderItemRepository,
                        PatientRepository patientRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderItemRepository = orderItemRepository;
        this.patientRepository = patientRepository;
    }

    public Invoice generateInvoice(String orderNumber, OrderHeader header, String patientName, String doctorName, LocalDate today) {
        Patient patient = patientRepository.findByIdCard(header.getPatientIdCard())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        List<OrderItem> items = orderItemRepository.findByOrderNumber(orderNumber);
        List<InvoiceLine> lines = new ArrayList<>();
        long subtotal = 0;

        for (OrderItem item : items) {
            if (item instanceof MedicationOrderItem m) {
                lines.add(new InvoiceLine("Medication: " + m.getMedicationName(), m.getCost()));
                subtotal += m.getCost();
            } else if (item instanceof ProcedureOrderItem p) {
                lines.add(new InvoiceLine("Procedure: " + p.getProcedureName(), p.getCost()));
                subtotal += p.getCost();
            } else if (item instanceof DiagnosticOrderItem d) {
                lines.add(new InvoiceLine("Diagnostic: " + d.getDiagnosticName(), d.getCost()));
                subtotal += d.getCost();
            }
        }

        InsurancePolicy policy = patient.getInsurancePolicy();
        long copay = 0;
        String insuranceCompany = null;
        String policyNumber = null;
        int remainingDays = 0;
        LocalDate policyEnd = null;

        if (policy != null && policy.isActive()) {
            insuranceCompany = policy.getCompany();
            policyNumber = policy.getPolicyNumber();
            policyEnd = policy.getEndDate();
            remainingDays = (int) policy.remainingDaysFrom(today);

            long copayPaidYear = patientRepository.totalCopayPaidInYear(patient.getIdCard(), today.getYear());
            if (copayPaidYear < COPAY_YEARLY_CAP) {
                copay = Math.min(COPAY_AMOUNT, Math.max(0, COPAY_YEARLY_CAP - copayPaidYear));
            } else {
                copay = 0;
            }
        } else {
            // no insurance or inactive policy: copay equals total (patient pays all)
            copay = subtotal;
        }

        long total = subtotal; // mostramos el total del servicio (detalle completo) como exige el enunciado
        int age = Period.between(patient.getBirthDate(), today).getYears();

        String invoiceId = invoiceRepository.nextInvoiceId();
        Invoice invoice = new Invoice(
                invoiceId,
                patientName,
                age,
                header.getPatientIdCard(),
                doctorName,
                insuranceCompany,
                policyNumber,
                remainingDays,
                policyEnd,
                lines,
                copay,
                total
        );
        return invoiceRepository.save(invoice);
    }
}
