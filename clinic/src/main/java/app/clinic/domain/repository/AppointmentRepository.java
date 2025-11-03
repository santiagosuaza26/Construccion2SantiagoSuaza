package app.clinic.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import app.clinic.domain.model.entities.Appointment;
import app.clinic.domain.model.valueobject.Id;

public interface AppointmentRepository {
    void save(Appointment appointment);
    List<Appointment> findByPatientId(Id patientId);
    List<Appointment> findByDoctorId(Id doctorId);
    List<Appointment> findByDateTime(LocalDateTime dateTime);
    boolean existsByPatientIdAndDateTime(Id patientId, LocalDateTime dateTime);
    java.util.Optional<Appointment> findById(Id appointmentId);
    void delete(Id appointmentId);
}