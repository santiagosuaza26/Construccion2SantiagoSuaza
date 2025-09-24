package app.domain.port;

import java.util.List;
import java.util.Optional;

import app.domain.model.DiagnosticTest;

public interface DiagnosticTestRepository {
    Optional<DiagnosticTest> findById(String diagnosticId);
    DiagnosticTest save(DiagnosticTest diagnostic);
    void deleteById(String diagnosticId);
    List<DiagnosticTest> findAll();
}