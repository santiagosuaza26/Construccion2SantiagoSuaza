package app.clinic.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.Appointment;
import app.clinic.domain.model.AppointmentDateTime;
import app.clinic.domain.model.AppointmentId;
import app.clinic.domain.model.AppointmentStatus;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.port.AppointmentRepository;
import app.clinic.infrastructure.entity.AppointmentEntity;
import app.clinic.infrastructure.repository.AppointmentJpaRepository;

/**
 * Adapter that implements the AppointmentRepository port using JPA.
 * Converts between domain objects and JPA entities for appointment management.
 */
@Repository
public class AppointmentRepositoryAdapter implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;

    public AppointmentRepositoryAdapter(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = toEntity(appointment);
        AppointmentEntity savedEntity = appointmentJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Appointment> findById(AppointmentId appointmentId) {
        return appointmentJpaRepository.findById(Long.valueOf(appointmentId.getValue()))
                .map(this::toDomain);
    }

    @Override
    public List<Appointment> findByPatientCedula(PatientCedula patientCedula) {
        return appointmentJpaRepository.findByPatientCedula(patientCedula.getValue())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDoctorCedula(DoctorCedula doctorCedula) {
        return appointmentJpaRepository.findByDoctorCedula(doctorCedula.getValue())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDateTime(AppointmentDateTime appointmentDateTime) {
        return appointmentJpaRepository.findByAppointmentDateTime(appointmentDateTime.getValue())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentJpaRepository.findByStatus(toEntityStatus(status))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(AppointmentId appointmentId) {
        return appointmentJpaRepository.existsById(Long.valueOf(appointmentId.getValue()));
    }

    @Override
    public void deleteById(AppointmentId appointmentId) {
        appointmentJpaRepository.deleteById(Long.valueOf(appointmentId.getValue()));
    }

    @Override
    public long count() {
        return appointmentJpaRepository.count();
    }

    @Override
    public long countByStatus(AppointmentStatus status) {
        return appointmentJpaRepository.countByStatus(toEntityStatus(status));
    }

    @Override
    public long countByPatient(PatientCedula patientCedula) {
        return appointmentJpaRepository.countByPatientCedula(patientCedula.getValue());
    }

    @Override
    public long countByDoctor(DoctorCedula doctorCedula) {
        return appointmentJpaRepository.countByDoctorCedula(doctorCedula.getValue());
    }

    // Métodos de conversión entre dominio y entidad

    private AppointmentEntity toEntity(Appointment appointment) {
        return new AppointmentEntity(
                appointment.getId(),
                appointment.getPatientCedula().getValue(),
                appointment.getDoctorCedula().getValue(),
                appointment.getAppointmentDateTime().getValue(),
                toEntityStatus(appointment.getStatus()),
                appointment.getReason() != null ? appointment.getReason().getValue() : null,
                null // notes field can be added later if needed
        );
    }

    private Appointment toDomain(AppointmentEntity entity) {
        return Appointment.of(
                entity.getAppointmentId(),
                PatientCedula.of(entity.getPatientCedula()),
                DoctorCedula.of(entity.getDoctorCedula()),
                AppointmentDateTime.of(entity.getAppointmentDateTime()),
                toDomainStatus(entity.getStatus()),
                entity.getReason() != null ? app.clinic.domain.model.ConsultationReason.of(entity.getReason()) : null
        );
    }

    private AppointmentEntity.AppointmentStatus toEntityStatus(AppointmentStatus status) {
        return switch (status) {
            case PROGRAMADA -> AppointmentEntity.AppointmentStatus.PROGRAMADA;
            case CONFIRMADA -> AppointmentEntity.AppointmentStatus.CONFIRMADA;
            case EN_CURSO -> AppointmentEntity.AppointmentStatus.EN_CURSO;
            case COMPLETADA -> AppointmentEntity.AppointmentStatus.COMPLETADA;
            case CANCELADA -> AppointmentEntity.AppointmentStatus.CANCELADA;
            case NO_ASISTIO -> AppointmentEntity.AppointmentStatus.NO_ASISTIO;
        };
    }

    private AppointmentStatus toDomainStatus(AppointmentEntity.AppointmentStatus status) {
        return switch (status) {
            case PROGRAMADA -> AppointmentStatus.PROGRAMADA;
            case CONFIRMADA -> AppointmentStatus.CONFIRMADA;
            case EN_CURSO -> AppointmentStatus.EN_CURSO;
            case COMPLETADA -> AppointmentStatus.COMPLETADA;
            case CANCELADA -> AppointmentStatus.CANCELADA;
            case NO_ASISTIO -> AppointmentStatus.NO_ASISTIO;
        };
    }
}