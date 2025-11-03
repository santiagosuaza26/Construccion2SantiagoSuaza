package app.clinic.application.usecase;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.BillingService;
import app.clinic.domain.service.RoleBasedAccessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultPatientBillingUseCase {

    private final BillingService billingService;
    private final RoleBasedAccessService roleBasedAccessService;

    public ConsultPatientBillingUseCase(BillingService billingService, RoleBasedAccessService roleBasedAccessService) {
        this.billingService = billingService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public List<Billing> execute(Role userRole, String patientId) {
        roleBasedAccessService.validatePatientDataAccess(userRole, false);
        return billingService.findBillingByPatientId(patientId);
    }
}