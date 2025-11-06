package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.Appointment;
import app.clinic.domain.model.valueobject.AppointmentStatus;
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
        AppointmentJpaEntity entity = new AppointmentJpaEntity();
        entity.setId(id);
        entity.setPatientId(appointment.getPatientId().getValue());
        entity.setAdminId(null); // adminId - not in domain model, set to null or handle separately
        entity.setDoctorId(appointment.getDoctorId().getValue());
        entity.setAppointmentDate(appointment.getDateTime());
        entity.setReason(appointment.getReason());
        entity.setStatus(appointment.getStatus().toString());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
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
        Appointment appointment = new Appointment(
            new Id(entity.getId()),
            new Id(entity.getPatientId()),
            new Id(entity.getDoctorId()),
            entity.getAppointmentDate(),
            entity.getReason()
        );
        // Set status from entity
        AppointmentStatus status = AppointmentStatus.valueOf(entity.getStatus());
        // Note: We can't directly set status, but we can use the methods to transition
        switch (status) {
            case CONFIRMED:
                appointment.confirm();
                break;
            case IN_PROGRESS:
                appointment.confirm();
                appointment.start();
                break;
            case COMPLETED:
                appointment.confirm();
                appointment.start();
                appointment.complete();
                break;
            case CANCELLED:
                appointment.cancel();
                break;
            case SCHEDULED:
            default:
                // Already scheduled by default
                break;
        }
        return appointment;
    }

    @Override
    public java.util.Optional<Appointment> findById(Id appointmentId) {
        return appointmentJpaRepository.findById(appointmentId.getValue())
            .map(this::toDomain);
    }

    @Override
    public void delete(Id appointmentId) {
        appointmentJpaRepository.deleteById(appointmentId.getValue());
    }

    private String generateAppointmentId() {
        // Simple ID generation - in production, use a proper ID generation strategy
        return "APPT-" + System.currentTimeMillis();
    }
}