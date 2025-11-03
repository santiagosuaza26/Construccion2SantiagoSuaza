package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.service.InventoryService;

@Service
public class UpdateProcedureUseCase {
    private final InventoryService inventoryService;

    public UpdateProcedureUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Procedure execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        inventoryService.updateProcedure(id, name, cost, requiresSpecialist, specialistType);
        return inventoryService.findProcedureById(new Id(id)).orElseThrow(() -> new IllegalArgumentException("Procedure not found"));
    }
}