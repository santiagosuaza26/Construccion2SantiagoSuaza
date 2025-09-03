package app.domain.port;

import java.util.Optional;

import app.domain.model.Patient;

public interface PatientRepository {
    Optional<Patient> findByIdCard(String idCard);
    Patient save(Patient patient);
    void deleteByIdCard(String idCard);
    long totalCopayPaidInYear(String idCard, int year); // para regla de tope de copago:contentReference[oaicite:3]{index=3}
}