package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.InventoryService;
import app.clinic.domain.service.RoleBasedAccessService;

@Component
public class ListDiagnosticAidsUseCase {

    private final InventoryService inventoryService;
    private final RoleBasedAccessService roleBasedAccessService;

    public ListDiagnosticAidsUseCase(InventoryService inventoryService, RoleBasedAccessService roleBasedAccessService) {
        this.inventoryService = inventoryService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public List<DiagnosticAid> execute(Role userRole) {
        roleBasedAccessService.checkAccess(userRole, "DIAGNOSTIC_AID");
        return inventoryService.getAllDiagnosticAids();
    }
}