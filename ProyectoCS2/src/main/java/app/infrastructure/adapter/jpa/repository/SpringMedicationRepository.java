package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.MedicationEntity;

public interface SpringMedicationRepository extends JpaRepository<MedicationEntity, String> {
}