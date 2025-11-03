package app.clinic.application.usecase;

import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.InventoryService;
import app.clinic.domain.service.RoleBasedAccessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListMedicationsUseCase {

    private final InventoryService inventoryService;
    private final RoleBasedAccessService roleBasedAccessService;

    public ListMedicationsUseCase(InventoryService inventoryService, RoleBasedAccessService roleBasedAccessService) {
        this.inventoryService = inventoryService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public List<Medication> execute(Role userRole) {
        roleBasedAccessService.checkAccess(userRole, "MEDICATION");
        return inventoryService.getAllMedications();
    }
}