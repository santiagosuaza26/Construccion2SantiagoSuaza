package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.service.InventoryService;

@Service
public class AddMedicationUseCase {
    private final InventoryService inventoryService;

    public AddMedicationUseCase(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public Medication execute(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        return inventoryService.addMedication(id, name, cost, requiresSpecialist, specialistType);
    }
}