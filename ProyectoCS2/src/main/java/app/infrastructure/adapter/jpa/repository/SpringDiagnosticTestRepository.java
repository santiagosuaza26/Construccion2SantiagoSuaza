package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.DiagnosticTestEntity;

public interface SpringDiagnosticTestRepository extends JpaRepository<DiagnosticTestEntity, String> {
}