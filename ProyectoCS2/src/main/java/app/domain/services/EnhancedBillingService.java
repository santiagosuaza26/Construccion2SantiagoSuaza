package app.domain.services;

import java.time.LocalDate;
import java.util.List;

import app.domain.exception.DomainValidationException;
import app.domain.model.Invoice;
import app.domain.model.InvoiceLine;
import app.domain.model.Patient;
import app.domain.port.InvoiceRepository;
import app.domain.port.PatientRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Servicio mejorado de facturación compatible con el modelo Invoice existente
 * Sigue principios SOLID:
 * - SRP: Una sola responsabilidad (gestionar facturación)
 * - DIP: Depende de abstracciones (repositorios)
 */
public class EnhancedBillingService {
    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;

    public EnhancedBillingService(InvoiceRepository invoiceRepository, PatientRepository patientRepository) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Genera una factura para un paciente usando el modelo existente
     */
    public Invoice generateInvoice(String patientIdCard, String doctorName,
                                  List<InvoiceLine> invoiceLines, AuthenticatedUser generatedBy) {
        // Validar que el paciente existe
        Patient patient = patientRepository.findByIdCard(patientIdCard)
            .orElseThrow(() -> new DomainValidationException("Patient with ID card " + patientIdCard + " does not exist"));

        // Validar que hay líneas de factura
        if (invoiceLines == null || invoiceLines.isEmpty()) {
            throw new DomainValidationException("Invoice must have at least one line item");
        }

        // Calcular totales usando long como en el modelo existente
        long subtotal = calculateSubtotal(invoiceLines);
        long copay = calculateCopayment(patient, subtotal);

        // Crear factura usando el constructor existente
        String invoiceId = invoiceRepository.nextInvoiceId();
        Invoice invoice = new Invoice(
            invoiceId,
            patient.getFullName(),
            calculateAge(patient.getBirthDate()),
            patientIdCard,
            doctorName,
            patient.getInsurancePolicy() != null ? patient.getInsurancePolicy().getCompany() : "No Insurance",
            patient.getInsurancePolicy() != null ? patient.getInsurancePolicy().getPolicyNumber() : "N/A",
            patient.getInsurancePolicy() != null ? (int)patient.getInsurancePolicy().remainingDaysFrom(LocalDate.now()) : 0,
            patient.getInsurancePolicy() != null ? patient.getInsurancePolicy().getEndDate() : null,
            invoiceLines,
            copay,
            subtotal
        );

        return invoiceRepository.save(invoice);
    }

    /**
     * Obtiene todas las facturas de un paciente
     */
    public List<Invoice> getPatientInvoices(String patientIdCard) {
        if (!patientRepository.existsByIdCard(patientIdCard)) {
            throw new DomainValidationException("Patient with ID card " + patientIdCard + " does not exist");
        }

        return invoiceRepository.findByPatientIdCard(patientIdCard);
    }

    /**
     * Obtiene factura por ID
     */
    public Invoice getInvoiceById(String invoiceId) {
        return invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new DomainValidationException("Invoice " + invoiceId + " does not exist"));
    }

    /**
     * Calcula el copago de un paciente basado en su póliza
     */
    public long calculateCopayment(Patient patient, long totalAmount) {
        if (patient.getInsurancePolicy() == null || !patient.getInsurancePolicy().isActive()) {
            return totalAmount; // 100% si no tiene póliza activa
        }

        // Lógica básica: 20% de copago si tiene póliza activa
        return (long) (totalAmount * 0.20);
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
    private long calculateSubtotal(List<InvoiceLine> invoiceLines) {
        return invoiceLines.stream()
            .mapToLong(InvoiceLine::getAmount)
            .sum();
    }

    /**
     * Calcula edad del paciente
     */
    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return java.time.Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Genera ID único de factura
     */
    private String generateInvoiceId() {
        return "INV" + System.currentTimeMillis();
    }
}