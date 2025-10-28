package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.Appointment;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.AppointmentRepository;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final AppointmentJpaRepository appointmentJpaRepository;

    public AppointmentRepositoryImpl(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public void save(Appointment appointment) {
        // Generate a unique ID for the appointment (you might want to use a UUID or similar)
        String id = generateAppointmentId();
        AppointmentJpaEntity entity = new AppointmentJpaEntity(
            id,
            appointment.getPatientId().getValue(),
            null, // adminId - not in domain model, set to null or handle separately
            appointment.getDoctorId().getValue(),
            appointment.getDateTime(),
            appointment.getReason(),
            "SCHEDULED", // default status
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        appointmentJpaRepository.save(entity);
    }

    @Override
    public List<Appointment> findByPatientId(Id patientId) {
        return appointmentJpaRepository.findByPatientId(patientId.getValue())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDoctorId(Id doctorId) {
        return appointmentJpaRepository.findByDoctorId(doctorId.getValue())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDateTime(LocalDateTime dateTime) {
        return appointmentJpaRepository.findByAppointmentDate(dateTime)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByPatientIdAndDateTime(Id patientId, LocalDateTime dateTime) {
        return appointmentJpaRepository.existsByPatientIdAndAppointmentDate(patientId.getValue(), dateTime);
    }

    private Appointment toDomain(AppointmentJpaEntity entity) {
        return new Appointment(
            new Id(entity.getPatientId()),
            new Id(entity.getDoctorId()),
            entity.getAppointmentDate(),
            entity.getReason()
        );
    }

    private String generateAppointmentId() {
        // Simple ID generation - in production, use a proper ID generation strategy
        return "APPT-" + System.currentTimeMillis();
    }
}