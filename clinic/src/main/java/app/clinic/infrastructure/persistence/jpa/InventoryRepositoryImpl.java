package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.domain.model.entities.Medication;
import app.clinic.domain.model.entities.Procedure;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.InventoryRepository;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {
    private final MedicationJpaRepository medicationJpaRepository;
    private final ProcedureJpaRepository procedureJpaRepository;
    private final DiagnosticAidJpaRepository diagnosticAidJpaRepository;

    public InventoryRepositoryImpl(MedicationJpaRepository medicationJpaRepository,
                                  ProcedureJpaRepository procedureJpaRepository,
                                  DiagnosticAidJpaRepository diagnosticAidJpaRepository) {
        this.medicationJpaRepository = medicationJpaRepository;
        this.procedureJpaRepository = procedureJpaRepository;
        this.diagnosticAidJpaRepository = diagnosticAidJpaRepository;
    }

    @Override
    public void saveMedication(Medication medication) {
        MedicationJpaEntity entity = new MedicationJpaEntity(
            medication.getId().getValue(),
            medication.getName(),
            medication.getCost(),
            medication.isRequiresSpecialist(),
            medication.getSpecialistType().getValue()
        );
        medicationJpaRepository.save(entity);
    }

    @Override
    public Optional<Medication> findMedicationById(Id id) {
        return medicationJpaRepository.findById(id.getValue())
            .map(this::toMedicationDomain);
    }

    @Override
    public List<Medication> findAllMedications() {
        return medicationJpaRepository.findAll().stream()
            .map(this::toMedicationDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsMedicationById(Id id) {
        return medicationJpaRepository.existsById(id.getValue());
    }

    @Override
    public void saveProcedure(Procedure procedure) {
        ProcedureJpaEntity entity = new ProcedureJpaEntity(
            procedure.getId().getValue(),
            procedure.getName(),
            procedure.getCost(),
            procedure.isRequiresSpecialist(),
            procedure.getSpecialistType().getValue()
        );
        procedureJpaRepository.save(entity);
    }

    @Override
    public Optional<Procedure> findProcedureById(Id id) {
        return procedureJpaRepository.findById(id.getValue())
            .map(this::toProcedureDomain);
    }

    @Override
    public List<Procedure> findAllProcedures() {
        return procedureJpaRepository.findAll().stream()
            .map(this::toProcedureDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsProcedureById(Id id) {
        return procedureJpaRepository.existsById(id.getValue());
    }

    @Override
    public void saveDiagnosticAid(DiagnosticAid diagnosticAid) {
        DiagnosticAidJpaEntity entity = new DiagnosticAidJpaEntity(
            diagnosticAid.getId().getValue(),
            diagnosticAid.getName(),
            diagnosticAid.getCost(),
            diagnosticAid.isRequiresSpecialist(),
            diagnosticAid.getSpecialistType().getValue()
        );
        diagnosticAidJpaRepository.save(entity);
    }

    @Override
    public Optional<DiagnosticAid> findDiagnosticAidById(Id id) {
        return diagnosticAidJpaRepository.findById(id.getValue())
            .map(this::toDiagnosticAidDomain);
    }

    @Override
    public List<DiagnosticAid> findAllDiagnosticAids() {
        return diagnosticAidJpaRepository.findAll().stream()
            .map(this::toDiagnosticAidDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsDiagnosticAidById(Id id) {
        return diagnosticAidJpaRepository.existsById(id.getValue());
    }

    private Medication toMedicationDomain(MedicationJpaEntity entity) {
        return new Medication(
            new Id(entity.getId()),
            entity.getName(),
            entity.getCost(),
            entity.isRequiresSpecialist(),
            new Id(entity.getSpecialistType())
        );
    }

    private Procedure toProcedureDomain(ProcedureJpaEntity entity) {
        return new Procedure(
            new Id(entity.getId()),
            entity.getName(),
            entity.getCost(),
            entity.isRequiresSpecialist(),
            new Id(entity.getSpecialistType())
        );
    }

    private DiagnosticAid toDiagnosticAidDomain(DiagnosticAidJpaEntity entity) {
        return new DiagnosticAid(
            new Id(entity.getId()),
            entity.getName(),
            entity.getCost(),
            entity.isRequiresSpecialist(),
            new Id(entity.getSpecialistType())
        );
    }
}