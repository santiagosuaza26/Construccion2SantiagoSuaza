package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.service.InventoryService;

@Service
public class AddProcedureUseCase {
    private final InventoryService inventoryService;

    public AddProcedureUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Procedure execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        return inventoryService.addProcedure(id, name, cost, requiresSpecialist, specialistType);
    }
}