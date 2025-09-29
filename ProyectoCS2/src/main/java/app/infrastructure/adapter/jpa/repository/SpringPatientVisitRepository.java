package app.infrastructure.adapter.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.infrastructure.adapter.jpa.entity.PatientVisitEntity;

@Repository
public interface SpringPatientVisitRepository extends JpaRepository<PatientVisitEntity, String> {

    List<PatientVisitEntity> findByPatientIdCard(String patientIdCard);

    List<PatientVisitEntity> findByAttendingStaffIdCard(String attendingStaffIdCard);

    List<PatientVisitEntity> findByAttendingStaffRole(app.domain.model.Role role);
}