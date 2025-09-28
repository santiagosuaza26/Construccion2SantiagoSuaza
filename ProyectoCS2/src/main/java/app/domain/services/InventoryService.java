package app.domain.services;

import java.util.List;

import app.domain.model.DiagnosticTest;
import app.domain.model.Medication;
import app.domain.model.ProcedureType;
import app.domain.model.Specialty;
import app.domain.port.DiagnosticTestRepository;
import app.domain.port.MedicationRepository;
import app.domain.port.ProcedureTypeRepository;
import app.domain.port.SpecialtyRepository;

public class InventoryService {
    private final MedicationRepository medicationRepository;
    private final ProcedureTypeRepository procedureTypeRepository;
    private final DiagnosticTestRepository diagnosticTestRepository;
    private final SpecialtyRepository specialtyRepository;
    
    public InventoryService(MedicationRepository medicationRepository,
                            ProcedureTypeRepository procedureTypeRepository,
                            DiagnosticTestRepository diagnosticTestRepository,
                            SpecialtyRepository specialtyRepository) {
        this.medicationRepository = medicationRepository;
        this.procedureTypeRepository = procedureTypeRepository;
        this.diagnosticTestRepository = diagnosticTestRepository;
        this.specialtyRepository = specialtyRepository;
    }
    
    // Gestión de Medicamentos
    public Medication createMedication(Medication medication) {
        if (medicationRepository.findById(medication.getMedicationId()).isPresent()) {
            throw new IllegalArgumentException("Medication ID already exists");
        }
        return medicationRepository.save(medication);
    }
    
    public Medication updateMedication(Medication medication) {
        medicationRepository.findById(medication.getMedicationId())
            .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
        return medicationRepository.save(medication);
    }
    
    public void deleteMedication(String medicationId) {
        medicationRepository.deleteById(medicationId);
    }
    
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
    
    public void updateMedicationStock(String medicationId, int quantity, boolean isAddition) {
        Medication medication = medicationRepository.findById(medicationId)
            .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
            
        int newStock = isAddition ? 
            medication.getStock() + quantity : 
            medication.getStock() - quantity;
            
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        Medication updated = new Medication(
            medication.getMedicationId(),
            medication.getName(),
            medication.getDescription(),
            newStock,
            medication.getPrice()
        );
        
        medicationRepository.save(updated);
    }
    
    // Gestión de Procedimientos
    public ProcedureType createProcedure(ProcedureType procedure) {
        if (procedureTypeRepository.findById(procedure.getProcedureId()).isPresent()) {
            throw new IllegalArgumentException("Procedure ID already exists");
        }
        return procedureTypeRepository.save(procedure);
    }
    
    public ProcedureType updateProcedure(ProcedureType procedure) {
        procedureTypeRepository.findById(procedure.getProcedureId())
            .orElseThrow(() -> new IllegalArgumentException("Procedure not found"));
        return procedureTypeRepository.save(procedure);
    }
    
    public void deleteProcedure(String procedureId) {
        procedureTypeRepository.deleteById(procedureId);
    }
    
    public List<ProcedureType> getAllProcedures() {
        return procedureTypeRepository.findAll();
    }
    
    // Gestión de Ayudas Diagnósticas
    public DiagnosticTest createDiagnosticTest(DiagnosticTest diagnostic) {
        if (diagnosticTestRepository.findById(diagnostic.getDiagnosticId()).isPresent()) {
            throw new IllegalArgumentException("Diagnostic test ID already exists");
        }
        return diagnosticTestRepository.save(diagnostic);
    }
    
    public DiagnosticTest updateDiagnosticTest(DiagnosticTest diagnostic) {
        diagnosticTestRepository.findById(diagnostic.getDiagnosticId())
            .orElseThrow(() -> new IllegalArgumentException("Diagnostic test not found"));
        return diagnosticTestRepository.save(diagnostic);
    }
    
    public void deleteDiagnosticTest(String diagnosticId) {
        diagnosticTestRepository.deleteById(diagnosticId);
    }
    
    public List<DiagnosticTest> getAllDiagnosticTests() {
        return diagnosticTestRepository.findAll();
    }
    
    // Gestión de Especialidades
    public Specialty createSpecialty(Specialty specialty) {
        if (specialtyRepository.findById(specialty.getSpecialtyId()).isPresent()) {
            throw new IllegalArgumentException("Specialty ID already exists");
        }
        return specialtyRepository.save(specialty);
    }
    
    public Specialty updateSpecialty(Specialty specialty) {
        specialtyRepository.findById(specialty.getSpecialtyId())
            .orElseThrow(() -> new IllegalArgumentException("Specialty not found"));
        return specialtyRepository.save(specialty);
    }
    
    public void deleteSpecialty(String specialtyId) {
        specialtyRepository.deleteById(specialtyId);
    }
    
    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }
    
    // Validaciones de inventario
    public void validateMedicationAvailability(String medicationId, int requiredQuantity) {
        Medication medication = medicationRepository.findById(medicationId)
            .orElseThrow(() -> new IllegalArgumentException("Medication not found"));
            
        if (medication.getStock() < requiredQuantity) {
            throw new IllegalStateException(
                String.format("Insufficient stock for %s. Available: %d, Required: %d",
                    medication.getName(), medication.getStock(), requiredQuantity)
            );
        }
    }
    
    public boolean isProcedureAvailable(String procedureId) {
        return procedureTypeRepository.findById(procedureId).isPresent();
    }
    
    public boolean isDiagnosticTestAvailable(String diagnosticId) {
        return diagnosticTestRepository.findById(diagnosticId).isPresent();
    }
    
    public boolean isSpecialtyAvailable(String specialtyId) {
        return specialtyRepository.findById(specialtyId).isPresent();
    }

    // Métodos de búsqueda
    public List<Medication> searchMedications(String query) {
        List<Medication> allMedications = getAllMedications();
        return allMedications.stream()
            .filter(medication ->
                medication.getName().toLowerCase().contains(query.toLowerCase()) ||
                medication.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                medication.getMedicationId().toLowerCase().contains(query.toLowerCase())
            )
            .toList();
    }

    public List<ProcedureType> searchProcedures(String query) {
        List<ProcedureType> allProcedures = getAllProcedures();
        return allProcedures.stream()
            .filter(procedure ->
                procedure.getName().toLowerCase().contains(query.toLowerCase()) ||
                procedure.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                procedure.getProcedureId().toLowerCase().contains(query.toLowerCase())
            )
            .toList();
    }

    public List<DiagnosticTest> searchDiagnostics(String query) {
        List<DiagnosticTest> allDiagnostics = getAllDiagnosticTests();
        return allDiagnostics.stream()
            .filter(diagnostic ->
                diagnostic.getName().toLowerCase().contains(query.toLowerCase()) ||
                diagnostic.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                diagnostic.getDiagnosticId().toLowerCase().contains(query.toLowerCase())
            )
            .toList();
    }

    public List<Medication> getLowStockMedications(int threshold) {
        List<Medication> allMedications = getAllMedications();
        return allMedications.stream()
            .filter(medication -> medication.getStock() <= threshold)
            .toList();
    }
}