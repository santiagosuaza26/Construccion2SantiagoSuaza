package app.adapter.out.persistence;

import app.domain.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPatientRepository extends JpaRepository<Patient, String> {
}
