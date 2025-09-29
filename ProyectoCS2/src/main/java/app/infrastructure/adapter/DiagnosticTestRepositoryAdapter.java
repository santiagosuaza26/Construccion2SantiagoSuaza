package app.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.DiagnosticTest;
import app.domain.port.DiagnosticTestRepository;
import app.infrastructure.adapter.jpa.entity.DiagnosticTestEntity;
import app.infrastructure.adapter.jpa.repository.SpringDiagnosticTestRepository;

@Component
public class DiagnosticTestRepositoryAdapter implements DiagnosticTestRepository {

    private final SpringDiagnosticTestRepository springDiagnosticTestRepository;

    public DiagnosticTestRepositoryAdapter(SpringDiagnosticTestRepository springDiagnosticTestRepository) {
        this.springDiagnosticTestRepository = springDiagnosticTestRepository;
    }

    @Override
    public Optional<DiagnosticTest> findById(String diagnosticId) {
        return springDiagnosticTestRepository.findById(diagnosticId)
            .map(this::toDomain);
    }

    @Override
    public DiagnosticTest save(DiagnosticTest diagnostic) {
        DiagnosticTestEntity entity = toEntity(diagnostic);
        DiagnosticTestEntity savedEntity = springDiagnosticTestRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(String diagnosticId) {
        springDiagnosticTestRepository.deleteById(diagnosticId);
    }

    @Override
    public List<DiagnosticTest> findAll() {
        return springDiagnosticTestRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private DiagnosticTestEntity toEntity(DiagnosticTest diagnostic) {
        DiagnosticTestEntity entity = new DiagnosticTestEntity();
        entity.setId(diagnostic.getDiagnosticId());
        entity.setName(diagnostic.getName());
        entity.setDescription(diagnostic.getDescription());
        entity.setPrice(diagnostic.getPrice());
        return entity;
    }

    private DiagnosticTest toDomain(DiagnosticTestEntity entity) {
        return new DiagnosticTest(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice()
        );
    }
}