package app.clinic.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.repository.MedicalRecordRepository;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    @Override
    public Optional<MedicalRecord> findByPatientIdentificationNumber(String patientId) {
        // TODO: Implementar persistencia real con JPA
        // Por ahora, devolver vacío para evitar errores de compilación
        return Optional.empty();
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        // TODO: Implementar persistencia real con JPA
        // Por ahora, no hacer nada para evitar errores de compilación
    }

    @Override
    public boolean existsByPatientIdentificationNumber(String patientId) {
        // TODO: Implementar persistencia real con JPA
        // Por ahora, devolver false para evitar errores de compilación
        return false;
    }
}