package app.clinic.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Id;

public interface InventoryRepository {
    void saveMedication(Medication medication);
    Optional<Medication> findMedicationById(Id id);
    List<Medication> findAllMedications();
    boolean existsMedicationById(Id id);

    void saveProcedure(Procedure procedure);
    Optional<Procedure> findProcedureById(Id id);
    List<Procedure> findAllProcedures();
    boolean existsProcedureById(Id id);

    void saveDiagnosticAid(DiagnosticAid diagnosticAid);
    Optional<DiagnosticAid> findDiagnosticAidById(Id id);
    List<DiagnosticAid> findAllDiagnosticAids();
    boolean existsDiagnosticAidById(Id id);
}