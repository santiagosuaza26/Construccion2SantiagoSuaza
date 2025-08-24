package app.adapter.out.persistence;

import app.domain.model.Patient;
import app.domain.repository.PatientRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaPatientRepositoryAdapter implements PatientRepository {

    private final SpringDataPatientRepository springDataPatientRepository;

    public JpaPatientRepositoryAdapter(SpringDataPatientRepository springDataPatientRepository) {
        this.springDataPatientRepository = springDataPatientRepository;
    }

    @Override
    public void save(Patient patient) {
        springDataPatientRepository.save(patient);
    }

    @Override
    public Optional<Patient> findById(String id) {
        return springDataPatientRepository.findById(id);
    }

    @Override
    public List<Patient> findAll() {
        return springDataPatientRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        springDataPatientRepository.deleteById(id);
    }
}
