package app.domain.port;

import java.util.List;
import java.util.Optional;

import app.domain.model.VitalSigns;

/**
 * Puerto para operaciones de signos vitales
 */
public interface VitalSignsRepository {
    VitalSigns save(VitalSigns vitalSigns);
    Optional<VitalSigns> findLatestByPatientIdCard(String patientIdCard);
    List<VitalSigns> findByPatientIdCard(String patientIdCard);
    List<VitalSigns> findByPatientIdCardAndDateRange(String patientIdCard, java.time.LocalDateTime start, java.time.LocalDateTime end);
    void deleteById(String id);
}