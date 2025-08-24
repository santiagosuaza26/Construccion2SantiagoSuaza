package app.domain.repository;

import app.domain.model.MedicalRecord;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository {
    void save(MedicalRecord record);

    Optional<MedicalRecord> findById(String id);

    List<MedicalRecord> findAll();

    void deleteById(String id);
}
