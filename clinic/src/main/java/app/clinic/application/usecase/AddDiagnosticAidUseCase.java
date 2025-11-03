package app.clinic.application.usecase;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.InventoryService;
import app.clinic.domain.service.RoleBasedAccessService;

@Component
public class AddDiagnosticAidUseCase {

    private final InventoryService inventoryService;
    private final RoleBasedAccessService roleBasedAccessService;

    public AddDiagnosticAidUseCase(InventoryService inventoryService, RoleBasedAccessService roleBasedAccessService) {
        this.inventoryService = inventoryService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public DiagnosticAid execute(Role userRole, String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        if (userRole != null) {
            roleBasedAccessService.checkAccess(userRole, "DIAGNOSTIC_AID");
        }
        return inventoryService.addDiagnosticAid(id, name, cost, requiresSpecialist, specialistType);
    }
}