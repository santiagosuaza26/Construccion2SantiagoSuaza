package app.clinic.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing nurse records table in the database.
 * Maps domain NurseRecord objects to database records.
 */
@Entity
@Table(name = "nurse_records")
public class NurseRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "administered_medications", columnDefinition = "TEXT")
    private String administeredMedications;

    @Column(name = "performed_procedures", columnDefinition = "TEXT")
    private String performedProcedures;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    // Default constructor
    public NurseRecordEntity() {}

    // Constructor with parameters
    public NurseRecordEntity(String administeredMedications, String performedProcedures, String observations) {
        this.administeredMedications = administeredMedications;
        this.performedProcedures = performedProcedures;
        this.observations = observations;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdministeredMedications() {
        return administeredMedications;
    }

    public void setAdministeredMedications(String administeredMedications) {
        this.administeredMedications = administeredMedications;
    }

    public String getPerformedProcedures() {
        return performedProcedures;
    }

    public void setPerformedProcedures(String performedProcedures) {
        this.performedProcedures = performedProcedures;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}