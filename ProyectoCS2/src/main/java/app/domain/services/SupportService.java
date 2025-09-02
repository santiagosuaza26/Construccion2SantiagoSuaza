package app.domain.services;

import app.domain.model.DiagnosticTest;
import app.domain.model.Medication;
import app.domain.model.ProcedureType;
import app.domain.port.DiagnosticTestRepository;
import app.domain.port.MedicationRepository;
import app.domain.port.ProcedureTypeRepository;

public class SupportService {
    private final MedicationRepository medicationRepository;
    private final ProcedureTypeRepository procedureTypeRepository;
    private final DiagnosticTestRepository diagnosticTestRepository;

    public SupportService(MedicationRepository medicationRepository,
                        ProcedureTypeRepository procedureTypeRepository,
                        DiagnosticTestRepository diagnosticTestRepository) {
        this.medicationRepository = medicationRepository;
        this.procedureTypeRepository = procedureTypeRepository;
        this.diagnosticTestRepository = diagnosticTestRepository;
    }

    public Medication ensureMedicationExists(String id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found: " + id));
    }

    public ProcedureType ensureProcedureExists(String id) {
        return procedureTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Procedure not found: " + id));
    }

    public DiagnosticTest ensureDiagnosticExists(String id) {
        return diagnosticTestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Diagnostic test not found: " + id));
    }
}
