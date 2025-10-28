package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.InventoryService;

@Service
public class UpdateProcedureUseCase {
    private final InventoryService inventoryService;

    public UpdateProcedureUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        inventoryService.updateProcedure(id, name, cost, requiresSpecialist, specialistType);
    }
}