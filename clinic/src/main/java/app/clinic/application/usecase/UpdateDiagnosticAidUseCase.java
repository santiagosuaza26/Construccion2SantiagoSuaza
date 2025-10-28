package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.InventoryService;

@Service
public class UpdateDiagnosticAidUseCase {
    private final InventoryService inventoryService;

    public UpdateDiagnosticAidUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        inventoryService.updateDiagnosticAid(id, name, cost, requiresSpecialist, specialistType);
    }
}