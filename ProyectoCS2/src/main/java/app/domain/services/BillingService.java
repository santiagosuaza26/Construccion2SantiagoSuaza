package app.domain.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import app.domain.exception.DomainValidationException;
import app.domain.model.Invoice;
import app.domain.model.InvoiceLine;
import app.domain.model.Patient;
import app.domain.port.InvoiceRepository;
import app.domain.port.PatientRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

// Constantes para evitar duplicación de literales
class BillingConstants {
    static final String PATIENT_NOT_EXISTS_MSG = " does not exist";
    static final String INVOICE_PREFIX = "Invoice ";
}

/**
 * Servicio especializado para gestión de facturación
 * Sigue principios SOLID:
 * - SRP: Una sola responsabilidad (gestionar facturación)
 * - DIP: Depende de abstracciones (repositorios)
 */
public class BillingService {
    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;

    public BillingService(InvoiceRepository invoiceRepository, PatientRepository patientRepository) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Genera una factura para un paciente
     */
    public Invoice generateInvoice(String patientIdCard, List<InvoiceLine> invoiceLines,
                                  AuthenticatedUser generatedBy) {
        // Validar que el paciente existe
        Patient patient = patientRepository.findByIdCard(patientIdCard)
            .orElseThrow(() -> new DomainValidationException("Patient with ID card " + patientIdCard + BillingConstants.PATIENT_NOT_EXISTS_MSG));

        // Validar que hay líneas de factura
        if (invoiceLines == null || invoiceLines.isEmpty()) {
            throw new DomainValidationException("Invoice must have at least one line item");
        }

        // Calcular totales
        BigDecimal subtotal = calculateSubtotal(invoiceLines);
        BigDecimal taxes = calculateTaxes(subtotal);
        BigDecimal total = subtotal.add(taxes);

        // Crear factura
        String invoiceNumber = generateInvoiceNumber();
        Invoice invoice = new Invoice(
            invoiceNumber,
            patientIdCard,
            invoiceLines,
            subtotal,
            taxes,
            total,
            "PENDING",
            LocalDateTime.now(),
            generatedBy.getIdCard()
        );

        return invoiceRepository.save(invoice);
    }

    /**
     * Marca una factura como pagada
     */
    public Invoice markInvoiceAsPaid(String invoiceNumber, AuthenticatedUser processedBy) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new DomainValidationException(BillingConstants.INVOICE_PREFIX + invoiceNumber + BillingConstants.PATIENT_NOT_EXISTS_MSG));

        if ("PAID".equals(invoice.getStatus())) {
            throw new DomainValidationException("Invoice is already paid");
        }

        // Crear nueva instancia con estado pagado
        Invoice paidInvoice = new Invoice(
            invoice.getInvoiceNumber(),
            invoice.getPatientIdCard(),
            invoice.getInvoiceLines(),
            invoice.getSubtotal(),
            invoice.getTaxes(),
            invoice.getTotalAmount(),
            "PAID",
            invoice.getIssueDate(),
            processedBy.getIdCard()
        );

        return invoiceRepository.save(paidInvoice);
    }

    /**
     * Cancela una factura
     */
    public Invoice cancelInvoice(String invoiceNumber, AuthenticatedUser cancelledBy) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new DomainValidationException(BillingConstants.INVOICE_PREFIX + invoiceNumber + BillingConstants.PATIENT_NOT_EXISTS_MSG));

        if ("CANCELLED".equals(invoice.getStatus()) || "PAID".equals(invoice.getStatus())) {
            throw new DomainValidationException("Cannot cancel invoice in status: " + invoice.getStatus());
        }

        // Crear nueva instancia con estado cancelado
        Invoice cancelledInvoice = new Invoice(
            invoice.getInvoiceNumber(),
            invoice.getPatientIdCard(),
            invoice.getInvoiceLines(),
            invoice.getSubtotal(),
            invoice.getTaxes(),
            invoice.getTotalAmount(),
            "CANCELLED",
            invoice.getIssueDate(),
            cancelledBy.getIdCard()
        );

        return invoiceRepository.save(cancelledInvoice);
    }

    /**
     * Obtiene todas las facturas de un paciente
     */
    public List<Invoice> getPatientInvoices(String patientIdCard) {
        if (!patientRepository.existsByIdCard(patientIdCard)) {
            throw new DomainValidationException("Patient with ID card " + patientIdCard + BillingConstants.PATIENT_NOT_EXISTS_MSG);
        }

        return invoiceRepository.findByPatientIdCard(patientIdCard);
    }

    /**
     * Obtiene factura por número
     */
    public Invoice getInvoiceByNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new DomainValidationException(BillingConstants.INVOICE_PREFIX + invoiceNumber + BillingConstants.PATIENT_NOT_EXISTS_MSG));
    }

    /**
     * Calcula el copago de un paciente basado en su póliza
     */
    public BigDecimal calculateCopayment(String patientIdCard, BigDecimal totalAmount) {
        Patient patient = patientRepository.findByIdCard(patientIdCard)
            .orElseThrow(() -> new DomainValidationException("Patient with ID card " + patientIdCard + BillingConstants.PATIENT_NOT_EXISTS_MSG));

        if (patient.getInsurancePolicy() == null) {
            throw new DomainValidationException("Patient does not have insurance policy");
        }

        // Lógica básica de cálculo de copago (puede ser más compleja según reglas específicas)
        BigDecimal coveragePercentage = patient.getInsurancePolicy().isActive() ?
            new BigDecimal("0.20") : BigDecimal.ONE; // 20% si está activo, 100% si no

        return totalAmount.multiply(coveragePercentage);
    }

    /**
     * Valida que un usuario pueda generar facturas
     */
    public void validateCanGenerateInvoices(AuthenticatedUser user) {
        if (!user.canGenerateInvoices()) {
            throw new DomainValidationException("User " + user.getIdCard() + " is not authorized to generate invoices");
        }
    }

    /**
     * Valida que un usuario pueda acceder a información de facturación
     */
    public void validateCanAccessBilling(AuthenticatedUser user, String patientIdCard) {
        // El paciente puede ver sus propias facturas
        if (user.getRole() == app.domain.model.Role.PATIENT &&
            user.getIdCard().equals(patientIdCard)) {
            return;
        }

        // Personal administrativo puede ver todas las facturas
        if (user.canGenerateInvoices()) {
            return;
        }

        throw new DomainValidationException("User " + user.getIdCard() + " is not authorized to access billing information");
    }

    /**
     * Calcula subtotal de líneas de factura
     */
    private BigDecimal calculateSubtotal(List<InvoiceLine> invoiceLines) {
        return invoiceLines.stream()
            .map(line -> line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula impuestos (ejemplo: 19% IVA)
     */
    private BigDecimal calculateTaxes(BigDecimal subtotal) {
        return subtotal.multiply(new BigDecimal("0.19"));
    }

    /**
     * Genera número único de factura
     */
    private String generateInvoiceNumber() {
        return "INV" + System.currentTimeMillis();
    }
}