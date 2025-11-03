package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.InventoryService;
import app.clinic.domain.service.RoleBasedAccessService;

@Component
public class ListProceduresUseCase {

    private final InventoryService inventoryService;
    private final RoleBasedAccessService roleBasedAccessService;

    public ListProceduresUseCase(InventoryService inventoryService, RoleBasedAccessService roleBasedAccessService) {
        this.inventoryService = inventoryService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public List<Procedure> execute(Role userRole) {
        roleBasedAccessService.checkAccess(userRole, "PROCEDURE");
        return inventoryService.getAllProcedures();
    }
}