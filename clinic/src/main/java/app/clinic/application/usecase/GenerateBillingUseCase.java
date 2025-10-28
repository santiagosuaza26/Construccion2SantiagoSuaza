package app.clinic.application.usecase;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.service.BillingService;

public class GenerateBillingUseCase {
    private final BillingService billingService;

    public GenerateBillingUseCase(BillingService billingService) {
        this.billingService = billingService;
    }

    public Billing execute(String patientId, String doctorName, String orderNumber, double totalCost,
                          String appliedMedications, String appliedProcedures, String appliedDiagnosticAids) {
        return billingService.generateBilling(patientId, doctorName, orderNumber, totalCost,
                                            appliedMedications, appliedProcedures, appliedDiagnosticAids);
    }
}