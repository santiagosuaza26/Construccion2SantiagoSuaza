package app.domain.port;

import java.util.List;
import java.util.Optional;

import app.domain.model.ProcedureType;

public interface ProcedureTypeRepository {
    Optional<ProcedureType> findById(String procedureId);
    ProcedureType save(ProcedureType procedure);
    void deleteById(String procedureId);
    List<ProcedureType> findAll();
}