package app.infrastructure.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import app.domain.model.PatientVisit;
import app.domain.port.PatientVisitRepository;
import app.infrastructure.adapter.jpa.entity.PatientVisitEntity;
import app.infrastructure.adapter.jpa.repository.SpringPatientVisitRepository;
import app.infrastructure.adapter.mapper.PatientVisitMapper;

@Component
public class PatientVisitRepositoryAdapter implements PatientVisitRepository {

    private final SpringPatientVisitRepository springPatientVisitRepository;
    private final PatientVisitMapper patientVisitMapper;

    public PatientVisitRepositoryAdapter(
            SpringPatientVisitRepository springPatientVisitRepository,
            @Qualifier("infrastructurePatientVisitMapper") PatientVisitMapper patientVisitMapper) {
        this.springPatientVisitRepository = springPatientVisitRepository;
        this.patientVisitMapper = patientVisitMapper;
    }

    @Override
    public PatientVisit save(PatientVisit visit) {
        PatientVisitEntity entity = patientVisitMapper.toEntity(visit);
        PatientVisitEntity savedEntity = springPatientVisitRepository.save(entity);
        return patientVisitMapper.toDomain(savedEntity);
    }

    @Override
    public List<PatientVisit> findByPatientIdCard(String patientIdCard) {
        return springPatientVisitRepository.findByPatientIdCard(patientIdCard)
                .stream()
                .map(patientVisitMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public String nextVisitId() {
        // Generar un ID único para la visita
        // En un escenario real, podrías usar un sequence o UUID
        return "VISIT-" + System.currentTimeMillis();
    }
}