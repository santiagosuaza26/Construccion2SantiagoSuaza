package app.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.Patient;
import app.domain.port.PatientRepository;
import app.infrastructure.adapter.entity.PatientEntity;
import app.infrastructure.adapter.mapper.PatientMapper;
import app.infrastructure.adapter.repository.PatientJpaRepository;

@Component
public class PatientRepositoryAdapter implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientMapper patientMapper;

    public PatientRepositoryAdapter(PatientJpaRepository patientJpaRepository, PatientMapper patientMapper) {
        this.patientJpaRepository = patientJpaRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public Optional<Patient> findByIdCard(String idCard) {
        return patientJpaRepository.findByIdCard(idCard)
                .map(patientMapper::toDomain);
    }

    @Override
    public Optional<Patient> findByUsername(String username) {
        return patientJpaRepository.findByCredentials_Username(username)
                .map(patientMapper::toDomain);
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = patientMapper.toEntity(patient);
        PatientEntity savedEntity = patientJpaRepository.save(entity);
        return patientMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteByIdCard(String idCard) {
        patientJpaRepository.deleteByIdCard(idCard);
    }

    @Override
    public long totalCopayPaidInYear(String idCard, int year) {
        Long result = patientJpaRepository.sumCopayByPatientAndYear(idCard, year);
        return result != null ? result : 0L;
    }

    @Override
    public boolean existsByIdCard(String idCard) {
        return patientJpaRepository.existsByIdCard(idCard);
    }

    @Override
    public boolean existsByUsername(String username) {
        return patientJpaRepository.existsByCredentials_Username(username);
    }

    @Override
    public List<Patient> findAll() {
        return patientJpaRepository.findAll().stream()
                .map(patientMapper::toDomain)
                .collect(Collectors.toList());
    }
}