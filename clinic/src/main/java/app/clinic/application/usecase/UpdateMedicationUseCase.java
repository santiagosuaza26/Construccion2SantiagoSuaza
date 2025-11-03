package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.service.InventoryService;

@Service
public class UpdateMedicationUseCase {
    private final InventoryService inventoryService;

    public UpdateMedicationUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Medication execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        inventoryService.updateMedication(id, name, cost, requiresSpecialist, specialistType);
        return inventoryService.findMedicationById(new Id(id)).orElseThrow(() -> new IllegalArgumentException("Medication not found"));
    }
}