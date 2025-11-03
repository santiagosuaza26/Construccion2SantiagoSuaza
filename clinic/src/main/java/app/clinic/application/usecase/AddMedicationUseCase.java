package app.clinic.application.usecase;

import org.springframework.stereotype.Component;

import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.InventoryService;
import app.clinic.domain.service.RoleBasedAccessService;

@Component
public class AddMedicationUseCase {

    private final InventoryService inventoryService;
    private final RoleBasedAccessService roleBasedAccessService;

    public AddMedicationUseCase(InventoryService inventoryService, RoleBasedAccessService roleBasedAccessService) {
        this.inventoryService = inventoryService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Medication execute(Role userRole, String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        if (userRole != null) {
            roleBasedAccessService.checkAccess(userRole, "MEDICATION");
        }
        return inventoryService.addMedication(id, name, cost, requiresSpecialist, specialistType);
    }
}