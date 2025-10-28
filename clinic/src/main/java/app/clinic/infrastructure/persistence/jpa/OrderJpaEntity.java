package app.clinic.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderJpaEntity {
    @Id
    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "patient_identification_number")
    private String patientIdentificationNumber;

    @Column(name = "doctor_identification_number")
    private String doctorIdentificationNumber;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "diagnosis")
    private String diagnosis;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicationOrderJpaEntity> medications;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProcedureOrderJpaEntity> procedures;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticAidOrderJpaEntity> diagnosticAids;
}