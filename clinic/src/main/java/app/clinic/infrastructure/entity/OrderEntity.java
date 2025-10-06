package app.clinic.infrastructure.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing orders table in the database.
 * Maps domain Order objects to database records.
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 10)
    private String orderNumber;

    @Column(name = "patient_cedula", nullable = false, length = 20)
    private String patientCedula;

    @Column(name = "doctor_cedula", nullable = false, length = 10)
    private String doctorCedula;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    // Temporarily commenting out complex relationships to resolve class loading issues
    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<MedicationOrderEntity> medications;

    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<ProcedureOrderEntity> procedures;

    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<DiagnosticAidOrderEntity> diagnosticAids;

    // Default constructor
    public OrderEntity() {}

    // Constructor with parameters
    public OrderEntity(String orderNumber, String patientCedula, String doctorCedula, LocalDate creationDate) {
        this.orderNumber = orderNumber;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.creationDate = creationDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    // Temporarily commenting out getters and setters for complex relationships
    // public List<MedicationOrderEntity> getMedications() {
    //     return medications;
    // }

    // public void setMedications(List<MedicationOrderEntity> medications) {
    //     this.medications = medications;
    // }

    // public List<ProcedureOrderEntity> getProcedures() {
    //     return procedures;
    // }

    // public void setProcedures(List<ProcedureOrderEntity> procedures) {
    //     this.procedures = procedures;
    // }

    // public List<DiagnosticAidOrderEntity> getDiagnosticAids() {
    //     return diagnosticAids;
    // }

    // public void setDiagnosticAids(List<DiagnosticAidOrderEntity> diagnosticAids) {
    //     this.diagnosticAids = diagnosticAids;
    // }
}