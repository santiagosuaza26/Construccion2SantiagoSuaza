package app.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.Medication;
import app.domain.port.MedicationRepository;
import app.infrastructure.adapter.jpa.entity.MedicationEntity;
import app.infrastructure.adapter.jpa.repository.SpringMedicationRepository;

@Component
public class MedicationRepositoryAdapter implements MedicationRepository {

    private final SpringMedicationRepository springMedicationRepository;

    public MedicationRepositoryAdapter(SpringMedicationRepository springMedicationRepository) {
        this.springMedicationRepository = springMedicationRepository;
    }

    @Override
    public Optional<Medication> findById(String medicationId) {
        return springMedicationRepository.findById(medicationId)
            .map(this::toDomain);
    }

    @Override
    public Medication save(Medication medication) {
        MedicationEntity entity = toEntity(medication);
        MedicationEntity savedEntity = springMedicationRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String medicationId) {
        springMedicationRepository.deleteById(medicationId);
    }

    @Override
    public List<Medication> findAll() {
        return springMedicationRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private MedicationEntity toEntity(Medication medication) {
        MedicationEntity entity = new MedicationEntity();
        entity.setId(medication.getMedicationId());
        entity.setName(medication.getName());
        entity.setDescription(medication.getDescription());
        entity.setStock(medication.getStock());
        entity.setPrice(medication.getPrice());
        return entity;
    }

    private Medication toDomain(MedicationEntity entity) {
        return new Medication(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getStock(),
            entity.getPrice()
        );
    }
}