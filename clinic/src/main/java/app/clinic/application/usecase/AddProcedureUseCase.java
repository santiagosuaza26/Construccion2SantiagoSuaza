package app.clinic.application.usecase;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.InventoryService;
import app.clinic.domain.service.RoleBasedAccessService;

@Component
public class AddProcedureUseCase {

    private final InventoryService inventoryService;
    private final RoleBasedAccessService roleBasedAccessService;

    public AddProcedureUseCase(InventoryService inventoryService, RoleBasedAccessService roleBasedAccessService) {
        this.inventoryService = inventoryService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Procedure execute(Role userRole, String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        if (userRole != null) {
            roleBasedAccessService.checkAccess(userRole, "PROCEDURE");
        }
        return inventoryService.addProcedure(id, name, cost, requiresSpecialist, specialistType);
    }
}