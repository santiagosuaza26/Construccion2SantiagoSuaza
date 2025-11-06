package app.clinic.domain.service;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.User;

public interface BillingService {
    Billing generateBilling(Order order, Patient patient, User doctor, String generatedBy);
    Billing generateBillingFromOrder(String orderNumber, String adminId);
    String generatePrintableInvoice(Billing billing);
    double calculateCopay(double totalCost, boolean hasActiveInsurance, double currentAnnualCopayTotal);
    boolean isCopayLimitExceeded(double currentAnnualCopayTotal);
    java.util.List<Billing> findBillingByPatientId(String patientId);
}
