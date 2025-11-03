package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.service.InventoryService;

@Service
public class UpdateDiagnosticAidUseCase {
    private final InventoryService inventoryService;

    public UpdateDiagnosticAidUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public DiagnosticAid execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        inventoryService.updateDiagnosticAid(id, name, cost, requiresSpecialist, specialistType);
        return inventoryService.findDiagnosticAidById(new Id(id)).orElseThrow(() -> new IllegalArgumentException("Diagnostic aid not found"));
    }
}