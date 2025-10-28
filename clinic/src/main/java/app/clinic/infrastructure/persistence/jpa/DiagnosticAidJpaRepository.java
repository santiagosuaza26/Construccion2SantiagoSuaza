package app.clinic.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticAidJpaRepository extends JpaRepository<DiagnosticAidJpaEntity, String> {
}