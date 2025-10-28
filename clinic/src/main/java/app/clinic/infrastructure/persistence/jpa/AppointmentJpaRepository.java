package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentJpaEntity, String> {
    List<AppointmentJpaEntity> findByPatientId(String patientId);
    List<AppointmentJpaEntity> findByDoctorId(String doctorId);
    List<AppointmentJpaEntity> findByAppointmentDate(LocalDateTime dateTime);
    boolean existsByPatientIdAndAppointmentDate(String patientId, LocalDateTime dateTime);
}