package app.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.domain.model.MedicalRecord;
import app.domain.repository.MedicalRecordRepository;

@Repository
public class JpaMedicalRecordRepositoryAdapter implements MedicalRecordRepository {

    private final SpringDataMedicalRecordRepository jpa;

    public JpaMedicalRecordRepositoryAdapter(SpringDataMedicalRecordRepository jpa) {
        this.jpa = jpa;
    }

    @Override public void save(MedicalRecord record) { jpa.save(record); }
    @Override public Optional<MedicalRecord> findById(String id) { return jpa.findById(id); }
    @Override public List<MedicalRecord> findAll() { return jpa.findAll(); }
    @Override public void deleteById(String id) { jpa.deleteById(id); }
}

