package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.BillingCalculationResult;
import app.clinic.domain.model.BillingDetails;
import app.clinic.domain.model.BillingSummary;
import app.clinic.domain.model.Invoice;
import app.clinic.domain.model.InvoiceId;
import app.clinic.domain.model.InvoiceNumber;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.Year;

/**
 * Port interface for billing repository operations.
 * Defines the contract for billing data access in the domain layer.
 */
public interface BillingRepository {

    /**
     * Saves an invoice.
     */
    Invoice save(Invoice invoice);

    /**
     * Finds an invoice by its unique identifier.
     */
    Optional<Invoice> findById(InvoiceId invoiceId);

    /**
     * Finds an invoice by its invoice number.
     */
    Optional<Invoice> findByInvoiceNumber(InvoiceNumber invoiceNumber);

    /**
     * Finds all invoices for a specific patient.
     */
    List<Invoice> findByPatientCedula(PatientCedula patientCedula);

    /**
     * Finds all invoices.
     */
    List<Invoice> findAll();

    /**
     * Checks if an invoice exists with the given invoice number.
     */
    boolean existsByInvoiceNumber(InvoiceNumber invoiceNumber);

    /**
     * Deletes an invoice by its identifier.
     */
    void deleteById(InvoiceId invoiceId);

    /**
     * Counts total number of invoices.
     */
    long count();

    /**
     * Counts invoices by patient.
     */
    long countByPatient(PatientCedula patientCedula);

    /**
     * Calculates accumulated copayment for a patient in a specific year.
     */
    BillingCalculationResult calculateAccumulatedCopayment(PatientCedula patientCedula, Year year);

    /**
     * Generates billing summary for a patient.
     */
    BillingSummary generateBillingSummary(PatientCedula patientCedula);

    /**
     * Generates billing details for invoice generation.
     */
    BillingDetails generateBillingDetails(PatientCedula patientCedula);
}