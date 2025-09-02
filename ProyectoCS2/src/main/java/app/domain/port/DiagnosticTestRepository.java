package app.domain.port;

import java.util.Optional;

import app.domain.model.DiagnosticTest;

public interface DiagnosticTestRepository {
    Optional<DiagnosticTest> findById(String diagnosticId);
}
