package app.domain.port;

import java.util.Optional;

import app.domain.model.ProcedureType;

public interface ProcedureTypeRepository {
    Optional<ProcedureType> findById(String procedureId);
}