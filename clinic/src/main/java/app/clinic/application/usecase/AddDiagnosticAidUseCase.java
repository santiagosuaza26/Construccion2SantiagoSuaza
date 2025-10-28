package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.service.InventoryService;

@Service
public class AddDiagnosticAidUseCase {
    private final InventoryService inventoryService;

    public AddDiagnosticAidUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public DiagnosticAid execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        return inventoryService.addDiagnosticAid(id, name, cost, requiresSpecialist, specialistType);
    }
}