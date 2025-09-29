package app.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.ProcedureType;
import app.domain.port.ProcedureTypeRepository;
import app.infrastructure.adapter.jpa.entity.ProcedureTypeEntity;
import app.infrastructure.adapter.jpa.repository.SpringProcedureTypeRepository;

@Component
public class ProcedureTypeRepositoryAdapter implements ProcedureTypeRepository {

    private final SpringProcedureTypeRepository springProcedureTypeRepository;

    public ProcedureTypeRepositoryAdapter(SpringProcedureTypeRepository springProcedureTypeRepository) {
        this.springProcedureTypeRepository = springProcedureTypeRepository;
    }

    @Override
    public Optional<ProcedureType> findById(String procedureId) {
        return springProcedureTypeRepository.findById(procedureId)
            .map(this::toDomain);
    }

    @Override
    public ProcedureType save(ProcedureType procedure) {
        ProcedureTypeEntity entity = toEntity(procedure);
        ProcedureTypeEntity savedEntity = springProcedureTypeRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String procedureId) {
        springProcedureTypeRepository.deleteById(procedureId);
    }

    @Override
    public List<ProcedureType> findAll() {
        return springProcedureTypeRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private ProcedureTypeEntity toEntity(ProcedureType procedure) {
        ProcedureTypeEntity entity = new ProcedureTypeEntity();
        entity.setId(procedure.getProcedureId());
        entity.setName(procedure.getName());
        entity.setDescription(procedure.getDescription());
        entity.setPrice(procedure.getPrice());
        return entity;
    }

    private ProcedureType toDomain(ProcedureTypeEntity entity) {
        return new ProcedureType(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice()
        );
    }
}