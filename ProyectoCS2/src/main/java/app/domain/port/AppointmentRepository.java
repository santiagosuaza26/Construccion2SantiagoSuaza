package app.domain.port;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import app.domain.model.Appointment;

/**
 * Puerto para operaciones de citas médicas
 */
public interface AppointmentRepository {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(String id);
    List<Appointment> findByPatientIdCard(String patientIdCard);
    List<Appointment> findByDoctorIdCard(String doctorIdCard);
    List<Appointment> findByDate(LocalDate date);
    boolean existsByDoctorAndDateTime(String doctorIdCard, java.time.LocalDateTime dateTime);
    void deleteById(String id);
}