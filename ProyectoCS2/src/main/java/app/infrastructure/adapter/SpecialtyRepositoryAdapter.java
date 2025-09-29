package app.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.Specialty;
import app.domain.port.SpecialtyRepository;
import app.infrastructure.adapter.jpa.entity.SpecialtyEntity;
import app.infrastructure.adapter.jpa.repository.SpringSpecialtyRepository;

@Component
public class SpecialtyRepositoryAdapter implements SpecialtyRepository {

    private final SpringSpecialtyRepository springSpecialtyRepository;

    public SpecialtyRepositoryAdapter(SpringSpecialtyRepository springSpecialtyRepository) {
        this.springSpecialtyRepository = springSpecialtyRepository;
    }

    @Override
    public Optional<Specialty> findById(String specialtyId) {
        return springSpecialtyRepository.findById(specialtyId)
            .map(this::toDomain);
    }

    @Override
    public Specialty save(Specialty specialty) {
        SpecialtyEntity entity = toEntity(specialty);
        SpecialtyEntity savedEntity = springSpecialtyRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String specialtyId) {
        springSpecialtyRepository.deleteById(specialtyId);
    }

    @Override
    public List<Specialty> findAll() {
        return springSpecialtyRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private SpecialtyEntity toEntity(Specialty specialty) {
        SpecialtyEntity entity = new SpecialtyEntity();
        entity.setId(specialty.getSpecialtyId());
        entity.setName(specialty.getName());
        entity.setDescription(specialty.getDescription());
        return entity;
    }

    private Specialty toDomain(SpecialtyEntity entity) {
        return new Specialty(
            entity.getId(),
            entity.getName(),
            entity.getDescription()
        );
    }
}