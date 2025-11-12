package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.Insurance;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.repository.PatientRepository;

@Repository
public class PatientRepositoryImpl implements PatientRepository {
    private final PatientJpaRepository patientJpaRepository;
    private final VitalSignsJpaRepository vitalSignsJpaRepository;

    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepository, VitalSignsJpaRepository vitalSignsJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
        this.vitalSignsJpaRepository = vitalSignsJpaRepository;
    }

    @Override
    public void save(Patient patient) {
        PatientJpaEntity entity = new PatientJpaEntity(
            patient.getIdentificationNumber().getValue(),
            patient.getFullName(),
            patient.getDateOfBirth().toString(),
            patient.getGender(),
            patient.getAddress().getValue(),
            patient.getPhone().getValue(),
            patient.getEmail().getValue(),
            patient.getEmergencyContact().getName(),
            patient.getEmergencyContact().getRelation(),
            patient.getEmergencyContact().getPhone().getValue(),
            patient.getInsurance().getCompanyName(),
            patient.getInsurance().getPolicyNumber(),
            patient.getInsurance().isActive(),
            patient.getInsurance().getValidityDate() != null ? patient.getInsurance().getValidityDate().toString() : null,
            patient.getAnnualCopayTotal()
        );
        patientJpaRepository.save(entity);
    }

    @Override
    public Optional<Patient> findByIdentificationNumber(Id identificationNumber) {
        return patientJpaRepository.findByIdentificationNumber(identificationNumber.getValue())
            .map(this::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return patientJpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdentificationNumber(Id identificationNumber) {
        return patientJpaRepository.existsByIdentificationNumber(identificationNumber.getValue());
    }

    @Override
    public boolean existsByUsername(String username) {
        // Pacientes no tienen username único, siempre retorna false
        return false;
    }

    @Override
    public void deleteByIdentificationNumber(Id identificationNumber) {
        patientJpaRepository.deleteById(identificationNumber.getValue());
    }

    @Override
    public void saveVitalSigns(VitalSigns vitalSigns) {
        VitalSignsJpaEntity entity = new VitalSignsJpaEntity(
            vitalSigns.getPatientIdentificationNumber(),
            vitalSigns.getDateTime(),
            vitalSigns.getBloodPressure(),
            vitalSigns.getTemperature(),
            vitalSigns.getPulse(),
            vitalSigns.getOxygenLevel(),
            vitalSigns.getObservations()
        );
        vitalSignsJpaRepository.save(entity);
    }

    @Override
    public List<VitalSigns> findVitalSignsByPatientId(Id patientId) {
        return vitalSignsJpaRepository.findByPatientIdentificationNumber(patientId.getValue()).stream()
            .map(this::toVitalSignsDomain)
            .collect(Collectors.toList());
    }

    private Patient toDomain(PatientJpaEntity entity) {
        EmergencyContact emergencyContact = new EmergencyContact(
            entity.getEmergencyContactName(),
            entity.getEmergencyContactRelation(),
            new Phone(entity.getEmergencyContactPhone())
        );

        LocalDate validityDate = null;
        if (entity.getInsuranceValidityDate() != null) {
            try {
                validityDate = LocalDate.parse(entity.getInsuranceValidityDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                validityDate = LocalDate.parse(entity.getInsuranceValidityDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        }

        Insurance insurance = new Insurance(
            entity.getInsuranceCompanyName(),
            entity.getInsurancePolicyNumber(),
            entity.isInsuranceActive(),
            validityDate
        );

        return new Patient(
            new Id(entity.getIdentificationNumber()),
            entity.getFullName(),
            new DateOfBirth(entity.getDateOfBirth()),
            entity.getGender(),
            new Address(entity.getAddress()),
            new Phone(entity.getPhone()),
            new Email(entity.getEmail()),
            emergencyContact,
            insurance
        );
    }

    private VitalSigns toVitalSignsDomain(VitalSignsJpaEntity entity) {
        return new VitalSigns(
            entity.getPatientIdentificationNumber(),
            entity.getDateTime(),
            entity.getBloodPressure(),
            entity.getTemperature(),
            entity.getPulse(),
            entity.getOxygenLevel(),
            entity.getObservations()
        );
    }
}