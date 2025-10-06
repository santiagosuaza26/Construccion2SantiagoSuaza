package app.clinic.infrastructure.adapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.BillingCalculationResult;
import app.clinic.domain.model.BillingDetails;
import app.clinic.domain.model.BillingSummary;
import app.clinic.domain.model.InsuranceCompanyName;
import app.clinic.domain.model.Invoice;
import app.clinic.domain.model.InvoiceId;
import app.clinic.domain.model.InvoiceNumber;
import app.clinic.domain.model.OrderNumber;
import app.clinic.domain.model.OrderSummary;
import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PolicyExpirationDate;
import app.clinic.domain.model.PolicyNumber;
import app.clinic.domain.model.PolicyValidityDays;
import app.clinic.domain.model.Year;
import app.clinic.domain.port.BillingRepository;
import app.clinic.domain.port.PatientRepository;
import app.clinic.infrastructure.entity.InvoiceEntity;
import app.clinic.infrastructure.repository.InvoiceJpaRepository;

/**
 * Adapter that implements the BillingRepository port.
 * Provides implementation for billing operations using JPA repository.
 */
@Component
public class BillingRepositoryAdapter implements BillingRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final PatientRepository patientRepository;

    public BillingRepositoryAdapter(InvoiceJpaRepository invoiceJpaRepository, PatientRepository patientRepository) {
        this.invoiceJpaRepository = invoiceJpaRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        // Convert domain Invoice to InvoiceEntity
        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceNumber(invoice.getInvoiceNumber().getValue());
        entity.setPatientCedula(invoice.getBillingDetails().getPatientCedula().getValue());
        entity.setTotalAmount(invoice.getTotalAmount().getAmount());

        // Set additional fields with calculated values
        LocalDateTime now = LocalDateTime.now();
        entity.setBillingDate(now);
        entity.setDueDate(now.plusDays(30));
        entity.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        entity.setYear(java.time.Year.now().getValue());

        // Set default values for new fields
        entity.setCopaymentAmount(java.math.BigDecimal.ZERO);
        entity.setInsuranceCoverage(java.math.BigDecimal.ZERO);
        entity.setPatientResponsibility(invoice.getTotalAmount().getAmount());
        entity.setNotes("Factura creada automáticamente");

        InvoiceEntity savedEntity = invoiceJpaRepository.save(entity);

        // Convert back to domain Invoice with saved entity data
        return convertToDomainInvoice(savedEntity);
    }

    @Override
    public Optional<Invoice> findById(InvoiceId invoiceId) {
        // Since InvoiceId is a String but JPA uses Long as primary key,
        // we'll need to implement this differently or use a different approach
        // For now, return empty as this needs further design consideration
        return Optional.empty();
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(InvoiceNumber invoiceNumber) {
        InvoiceEntity entity = invoiceJpaRepository.findByInvoiceNumber(invoiceNumber.getValue());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(convertToDomainInvoice(entity));
    }

    @Override
    public List<Invoice> findByPatientCedula(PatientCedula patientCedula) {
        List<InvoiceEntity> entities = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(
            patientCedula.getValue());
        return entities.stream()
            .map(this::convertToDomainInvoice)
            .toList();
    }

    @Override
    public List<Invoice> findAll() {
        List<InvoiceEntity> entities = invoiceJpaRepository.findAll();
        return entities.stream()
            .map(this::convertToDomainInvoice)
            .toList();
    }

    @Override
    public boolean existsByInvoiceNumber(InvoiceNumber invoiceNumber) {
        return invoiceJpaRepository.existsByInvoiceNumber(invoiceNumber.getValue());
    }

    @Override
    public void deleteById(InvoiceId invoiceId) {
        // Since InvoiceId is String but JPA uses Long, we need a different approach
        // For now, this remains a no-op as it needs architectural consideration
    }

    @Override
    public long count() {
        return invoiceJpaRepository.count();
    }

    @Override
    public long countByPatient(PatientCedula patientCedula) {
        return invoiceJpaRepository.countByPatientCedula(patientCedula.getValue());
    }

    @Override
    public BillingCalculationResult calculateAccumulatedCopayment(PatientCedula patientCedula, Year year) {
        // Calculate accumulated copayment using JPA repository
        BigDecimal totalCopayment = invoiceJpaRepository.getTotalCopaymentForYear(
            patientCedula.getValue(), year.getValue());

        BigDecimal totalBilled = invoiceJpaRepository.getTotalBilledForYear(
            patientCedula.getValue(), year.getValue());

        // Create result with accumulated values
        return BillingCalculationResult.of(
            app.clinic.domain.model.Money.of(totalBilled != null ? totalBilled : BigDecimal.ZERO),
            app.clinic.domain.model.Money.of(totalCopayment != null ? totalCopayment : BigDecimal.ZERO),
            app.clinic.domain.model.Money.of(BigDecimal.ZERO), // Insurance coverage
            app.clinic.domain.model.Money.of(totalCopayment != null ? totalCopayment : BigDecimal.ZERO), // Patient responsibility
            false
        );
    }

    @Override
    public BillingSummary generateBillingSummary(PatientCedula patientCedula) {
        // Get patient information
        Optional<Patient> patientOpt = patientRepository.findByCedula(patientCedula);
        if (patientOpt.isEmpty()) {
            return null;
        }

        Patient patient = patientOpt.get();

        // Get patient invoices
        List<InvoiceEntity> invoices = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(
            patientCedula.getValue());

        // Create order summaries from invoices (simplified implementation)
        List<OrderSummary> orderSummaries = invoices.stream()
            .map(invoice -> OrderSummary.of(
                app.clinic.domain.model.OrderNumber.of(invoice.getInvoiceNumber()),
                List.of(), // medications - would need separate query
                List.of(), // procedures - would need separate query
                List.of()  // diagnostic aids - would need separate query
            ))
            .toList();

        // Extract insurance information
        InsuranceCompanyName insuranceCompany = null;
        PolicyNumber policyNumber = null;
        PolicyValidityDays validityDays = null;
        PolicyExpirationDate expirationDate = null;

        if (patient.getInsurancePolicy() != null) {
            insuranceCompany = patient.getInsurancePolicy().getCompanyName();
            policyNumber = patient.getInsurancePolicy().getPolicyNumber();
            expirationDate = patient.getInsurancePolicy().getExpirationDate();
            // validityDays would need to be calculated from expiration date
            validityDays = app.clinic.domain.model.PolicyValidityDays.of(365); // Default 1 year
        }

        // Create and return billing summary
        return BillingSummary.of(
            patient.getFullName(),
            patient.getAge(),
            patientCedula,
            insuranceCompany,
            policyNumber,
            validityDays,
            expirationDate,
            orderSummaries
        );
    }

    @Override
    public BillingDetails generateBillingDetails(PatientCedula patientCedula) {
        // Get patient information
        Optional<Patient> patientOpt = patientRepository.findByCedula(patientCedula);
        if (patientOpt.isEmpty()) {
            return null;
        }

        Patient patient = patientOpt.get();

        // Get patient invoices
        List<InvoiceEntity> invoices = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(
            patientCedula.getValue());

        // Create order summaries from invoices (simplified implementation)
        List<OrderSummary> orderSummaries = invoices.stream()
            .map(invoice -> OrderSummary.of(
                OrderNumber.of(invoice.getInvoiceNumber()),
                List.of(), // medications - would need separate query
                List.of(), // procedures - would need separate query
                List.of()  // diagnostic aids - would need separate query
            ))
            .toList();

        // Extract insurance information
        InsuranceCompanyName insuranceCompany = null;
        PolicyNumber policyNumber = null;
        PolicyValidityDays validityDays = null;
        PolicyExpirationDate expirationDate = null;

        if (patient.getInsurancePolicy() != null) {
            insuranceCompany = patient.getInsurancePolicy().getCompanyName();
            policyNumber = patient.getInsurancePolicy().getPolicyNumber();
            expirationDate = patient.getInsurancePolicy().getExpirationDate();
            // validityDays would need to be calculated from expiration date
            validityDays = PolicyValidityDays.of(365); // Default 1 year
        }

        // Create and return billing details
        return BillingDetails.of(
            patient.getFullName(),
            patient.getAge(),
            patientCedula,
            insuranceCompany,
            policyNumber,
            validityDays,
            expirationDate,
            orderSummaries
        );
    }

    /**
     * Helper method to convert InvoiceEntity to domain Invoice model
     */
    private Invoice convertToDomainInvoice(InvoiceEntity entity) {
        // Create billing details from entity data (simplified version)
        BillingDetails billingDetails = app.clinic.domain.model.BillingDetails.of(
            app.clinic.domain.model.PatientFullName.of("Unknown", "Patient"), // Would need patient lookup
            app.clinic.domain.model.PatientAge.of(0), // Would need patient lookup
            app.clinic.domain.model.PatientCedula.of(entity.getPatientCedula()),
            null, // Insurance company - would need patient lookup
            null, // Policy number - would need patient lookup
            null, // Validity days - would need patient lookup
            null, // Expiration date - would need patient lookup
            List.of() // Order summaries - would need separate query
        );

        return app.clinic.domain.model.Invoice.of(
            app.clinic.domain.model.InvoiceNumber.of(entity.getInvoiceNumber()),
            billingDetails,
            app.clinic.domain.model.Money.of(entity.getTotalAmount()),
            app.clinic.domain.model.BillingDate.of(entity.getBillingDate().toLocalDate())
        );
    }
}