package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.ProcedureTypeEntity;

public interface SpringProcedureTypeRepository extends JpaRepository<ProcedureTypeEntity, String> {
}