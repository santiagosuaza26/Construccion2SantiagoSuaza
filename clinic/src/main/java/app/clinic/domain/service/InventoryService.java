package app.clinic.domain.service;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.InventoryRepository;

public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Medication addMedication(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        Id medId = new Id(id);
        if (inventoryRepository.existsMedicationById(medId)) {
            throw new IllegalArgumentException("Medication already exists");
        }
        Medication medication = new Medication(medId, name, cost, requiresSpecialist, requiresSpecialist ? new Id(specialistType) : null);
        inventoryRepository.saveMedication(medication);
        return medication;
    }

    public Procedure addProcedure(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        Id procId = new Id(id);
        if (inventoryRepository.existsProcedureById(procId)) {
            throw new IllegalArgumentException("Procedure already exists");
        }
        Procedure procedure = new Procedure(procId, name, cost, requiresSpecialist, requiresSpecialist ? new Id(specialistType) : null);
        inventoryRepository.saveProcedure(procedure);
        return procedure;
    }

    public DiagnosticAid addDiagnosticAid(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        Id aidId = new Id(id);
        if (inventoryRepository.existsDiagnosticAidById(aidId)) {
            throw new IllegalArgumentException("Diagnostic aid already exists");
        }
        DiagnosticAid diagnosticAid = new DiagnosticAid(aidId, name, cost, requiresSpecialist, requiresSpecialist ? new Id(specialistType) : null);
        inventoryRepository.saveDiagnosticAid(diagnosticAid);
        return diagnosticAid;
    }

    public void updateMedication(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        Id medId = new Id(id);
        inventoryRepository.findMedicationById(medId).orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        Medication updated = new Medication(medId, name, cost, requiresSpecialist, requiresSpecialist ? new Id(specialistType) : null);
        inventoryRepository.saveMedication(updated);
    }

    public void updateProcedure(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        Id procId = new Id(id);
        inventoryRepository.findProcedureById(procId).orElseThrow(() -> new IllegalArgumentException("Procedure not found"));
        Procedure updated = new Procedure(procId, name, cost, requiresSpecialist, requiresSpecialist ? new Id(specialistType) : null);
        inventoryRepository.saveProcedure(updated);
    }

    public void updateDiagnosticAid(String id, String name, double cost, boolean requiresSpecialist, String specialistType) {
        Id aidId = new Id(id);
        inventoryRepository.findDiagnosticAidById(aidId).orElseThrow(() -> new IllegalArgumentException("Diagnostic aid not found"));
        DiagnosticAid updated = new DiagnosticAid(aidId, name, cost, requiresSpecialist, requiresSpecialist ? new Id(specialistType) : null);
        inventoryRepository.saveDiagnosticAid(updated);
    }

    public Optional<Medication> findMedicationById(Id id) {
        return inventoryRepository.findMedicationById(id);
    }

    public void deleteMedication(String id) {
        Id medId = new Id(id);
        if (!inventoryRepository.existsMedicationById(medId)) {
            throw new IllegalArgumentException("Medication not found");
        }
        // Note: In practice, check if in use before deleting
        // For now, assume deletion is allowed
    }

    public Optional<Procedure> findProcedureById(Id id) {
        return inventoryRepository.findProcedureById(id);
    }

    public void deleteProcedure(String id) {
        Id procId = new Id(id);
        if (!inventoryRepository.existsProcedureById(procId)) {
            throw new IllegalArgumentException("Procedure not found");
        }
    }

    public Optional<DiagnosticAid> findDiagnosticAidById(Id id) {
        return inventoryRepository.findDiagnosticAidById(id);
    }

    public void deleteDiagnosticAid(String id) {
        Id aidId = new Id(id);
        if (!inventoryRepository.existsDiagnosticAidById(aidId)) {
            throw new IllegalArgumentException("Diagnostic aid not found");
        }
    }

    public List<Medication> getAllMedications() {
        return inventoryRepository.findAllMedications();
    }

    public List<Procedure> getAllProcedures() {
        return inventoryRepository.findAllProcedures();
    }

    public List<DiagnosticAid> getAllDiagnosticAids() {
        return inventoryRepository.findAllDiagnosticAids();
    }
}