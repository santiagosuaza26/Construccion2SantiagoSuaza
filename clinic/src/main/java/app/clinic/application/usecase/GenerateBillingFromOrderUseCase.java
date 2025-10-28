package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.service.BillingService;

@Service
public class GenerateBillingFromOrderUseCase {
    private final BillingService billingService;

    public GenerateBillingFromOrderUseCase(BillingService billingService) {
        this.billingService = billingService;
    }

    public Billing execute(String orderNumber, String adminId) {
        return billingService.generateBillingFromOrder(orderNumber, adminId);
    }
}