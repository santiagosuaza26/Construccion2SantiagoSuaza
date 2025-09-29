package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.SpecialtyEntity;

public interface SpringSpecialtyRepository extends JpaRepository<SpecialtyEntity, String> {
}