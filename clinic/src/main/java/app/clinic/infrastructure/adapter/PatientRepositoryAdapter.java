package app.clinic.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientId;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.port.PatientRepository;
import app.clinic.infrastructure.entity.PatientEntity;
import app.clinic.infrastructure.repository.PatientJpaRepository;

/**
 * Adapter that implements the PatientRepository port using JPA.
 * Converts between domain objects and JPA entities.
 */
@Repository
public class PatientRepositoryAdapter implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    public PatientRepositoryAdapter(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = toEntity(patient);
        PatientEntity savedEntity = patientJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Patient> findById(PatientId patientId) {
        return patientJpaRepository.findById(Long.valueOf(patientId.getValue()))
                .map(this::toDomain);
    }

    @Override
    public Optional<Patient> findByCedula(PatientCedula cedula) {
        return patientJpaRepository.findByCedula(cedula.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<Patient> findByUsername(PatientUsername username) {
        return patientJpaRepository.findByUsername(username.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return patientJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCedula(PatientCedula cedula) {
        return patientJpaRepository.existsByCedula(cedula.getValue());
    }

    @Override
    public boolean existsByUsername(PatientUsername username) {
        return patientJpaRepository.existsByUsername(username.getValue());
    }

    @Override
    public void deleteById(PatientId patientId) {
        patientJpaRepository.deleteById(Long.valueOf(patientId.getValue()));
    }

    @Override
    public void deleteByCedula(PatientCedula cedula) {
        patientJpaRepository.findByCedula(cedula.getValue())
                .ifPresent(patientJpaRepository::delete);
    }

    @Override
    public long count() {
        return patientJpaRepository.count();
    }

    // Métodos de conversión entre dominio y entidad

    private PatientEntity toEntity(Patient patient) {
        return new PatientEntity(
                patient.getCedula().getValue(),
                patient.getUsername().getValue(),
                patient.getPassword().getHashedValue(),
                patient.getFullName().getFirstNames(),
                patient.getFullName().getLastNames(),
                patient.getBirthDate().getValue(),
                toEntityGender(patient.getGender()),
                patient.getAddress().getValue(),
                patient.getPhoneNumber().getValue(),
                patient.getEmail().getValue(),
                patient.getEmergencyContacts() != null && !patient.getEmergencyContacts().isEmpty()
                    ? toEntityEmergencyContact(patient.getEmergencyContacts().get(0)) : null,
                patient.getInsurancePolicy() != null ? toEntityInsurancePolicy(patient.getInsurancePolicy()) : null
        );
    }

    private Patient toDomain(PatientEntity entity) {
        return Patient.of(
                PatientCedula.of(entity.getCedula()),
                PatientUsername.of(entity.getUsername()),
                app.clinic.domain.model.PatientPassword.ofHashed(entity.getPassword()),
                app.clinic.domain.model.PatientFullName.of(entity.getFirstNames(), entity.getLastNames()),
                app.clinic.domain.model.PatientBirthDate.of(entity.getBirthDate()),
                toDomainGender(entity.getGender()),
                app.clinic.domain.model.PatientAddress.of(entity.getAddress()),
                app.clinic.domain.model.PatientPhoneNumber.of(entity.getPhoneNumber()),
                app.clinic.domain.model.PatientEmail.of(entity.getEmail()),
                entity.getEmergencyContact() != null ? List.of(toDomainEmergencyContact(entity.getEmergencyContact())) : List.of(),
                entity.getInsurancePolicy() != null ? toDomainInsurancePolicy(entity.getInsurancePolicy()) : null
        );
    }

    private PatientEntity.PatientGender toEntityGender(app.clinic.domain.model.PatientGender gender) {
        return switch (gender) {
            case MASCULINO -> PatientEntity.PatientGender.MASCULINO;
            case FEMENINO -> PatientEntity.PatientGender.FEMENINO;
            case OTRO -> PatientEntity.PatientGender.OTRO;
        };
    }

    private app.clinic.domain.model.PatientGender toDomainGender(PatientEntity.PatientGender gender) {
        return switch (gender) {
            case MASCULINO -> app.clinic.domain.model.PatientGender.MASCULINO;
            case FEMENINO -> app.clinic.domain.model.PatientGender.FEMENINO;
            case OTRO -> app.clinic.domain.model.PatientGender.OTRO;
        };
    }

    private app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship toEntityRelationship(app.clinic.domain.model.Relationship relationship) {
        return switch (relationship) {
            case PADRE -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.PADRE;
            case MADRE -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.MADRE;
            case HIJO -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.HIJO;
            case HIJA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.HIJA;
            case HERMANO -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.HERMANO;
            case HERMANA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.HERMANA;
            case ESPOSO -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.ESPOSO;
            case ESPOSA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.ESPOSA;
            case PAREJA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.PAREJA;
            case AMIGO -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.AMIGO;
            case AMIGA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.AMIGA;
            case TIO -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.TIO;
            case TIA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.TIA;
            case ABUEL -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.ABUEL;
            case ABUELA -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.ABUELA;
            case OTRO -> app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship.OTRO;
        };
    }

    private app.clinic.domain.model.Relationship toDomainRelationship(app.clinic.infrastructure.entity.EmergencyContactEntity.Relationship relationship) {
        return switch (relationship) {
            case PADRE -> app.clinic.domain.model.Relationship.PADRE;
            case MADRE -> app.clinic.domain.model.Relationship.MADRE;
            case HIJO -> app.clinic.domain.model.Relationship.HIJO;
            case HIJA -> app.clinic.domain.model.Relationship.HIJA;
            case HERMANO -> app.clinic.domain.model.Relationship.HERMANO;
            case HERMANA -> app.clinic.domain.model.Relationship.HERMANA;
            case ESPOSO -> app.clinic.domain.model.Relationship.ESPOSO;
            case ESPOSA -> app.clinic.domain.model.Relationship.ESPOSA;
            case PAREJA -> app.clinic.domain.model.Relationship.PAREJA;
            case AMIGO -> app.clinic.domain.model.Relationship.AMIGO;
            case AMIGA -> app.clinic.domain.model.Relationship.AMIGA;
            case TIO -> app.clinic.domain.model.Relationship.TIO;
            case TIA -> app.clinic.domain.model.Relationship.TIA;
            case ABUEL -> app.clinic.domain.model.Relationship.ABUEL;
            case ABUELA -> app.clinic.domain.model.Relationship.ABUELA;
            case OTRO -> app.clinic.domain.model.Relationship.OTRO;
        };
    }

    private app.clinic.infrastructure.entity.EmergencyContactEntity toEntityEmergencyContact(app.clinic.domain.model.EmergencyContact emergencyContact) {
        return new app.clinic.infrastructure.entity.EmergencyContactEntity(
                emergencyContact.getName().getFirstNames(),
                emergencyContact.getName().getLastNames(),
                toEntityRelationship(emergencyContact.getRelationship()),
                emergencyContact.getPhoneNumber().getValue()
        );
    }

    private app.clinic.domain.model.EmergencyContact toDomainEmergencyContact(app.clinic.infrastructure.entity.EmergencyContactEntity entity) {
        return app.clinic.domain.model.EmergencyContact.of(
                app.clinic.domain.model.EmergencyContactName.of(entity.getFirstNames(), entity.getLastNames()),
                toDomainRelationship(entity.getRelationship()),
                app.clinic.domain.model.EmergencyContactPhoneNumber.of(entity.getPhoneNumber())
        );
    }

    private app.clinic.infrastructure.entity.InsurancePolicyEntity toEntityInsurancePolicy(app.clinic.domain.model.InsurancePolicy insurancePolicy) {
        return new app.clinic.infrastructure.entity.InsurancePolicyEntity(
                insurancePolicy.getCompanyName().getValue(),
                insurancePolicy.getPolicyNumber().getValue(),
                toEntityPolicyStatus(insurancePolicy.getStatus()),
                insurancePolicy.getExpirationDate().getValue()
        );
    }

    private app.clinic.domain.model.InsurancePolicy toDomainInsurancePolicy(app.clinic.infrastructure.entity.InsurancePolicyEntity entity) {
        return app.clinic.domain.model.InsurancePolicy.of(
                app.clinic.domain.model.InsuranceCompanyName.of(entity.getCompanyName()),
                app.clinic.domain.model.PolicyNumber.of(entity.getPolicyNumber()),
                toDomainPolicyStatus(entity.getStatus()),
                app.clinic.domain.model.PolicyExpirationDate.of(entity.getExpirationDate())
        );
    }

    private app.clinic.infrastructure.entity.InsurancePolicyEntity.PolicyStatus toEntityPolicyStatus(app.clinic.domain.model.PolicyStatus status) {
        return switch (status) {
            case ACTIVA -> app.clinic.infrastructure.entity.InsurancePolicyEntity.PolicyStatus.ACTIVE;
            case INACTIVA, VENCIDA, CANCELADA -> app.clinic.infrastructure.entity.InsurancePolicyEntity.PolicyStatus.INACTIVE;
        };
    }

    private app.clinic.domain.model.PolicyStatus toDomainPolicyStatus(app.clinic.infrastructure.entity.InsurancePolicyEntity.PolicyStatus status) {
        if (status == app.clinic.infrastructure.entity.InsurancePolicyEntity.PolicyStatus.ACTIVE) {
            return app.clinic.domain.model.PolicyStatus.ACTIVA;
        } else {
            return app.clinic.domain.model.PolicyStatus.INACTIVA;
        }
    }
}