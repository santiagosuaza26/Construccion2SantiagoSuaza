package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "consultation_date", nullable = false)
    private LocalDate consultationDate;

    @Column(name = "reason", columnDefinition = "CLOB")
    private String reason;

    @Column(name = "symptoms", columnDefinition = "CLOB")
    private String symptoms;

    @Column(name = "diagnosis", columnDefinition = "CLOB")
    private String diagnosis;

    @Column(name = "prescriptions", columnDefinition = "CLOB")
    private String prescriptions;

    @Column(name = "procedures", columnDefinition = "CLOB")
    private String procedures;

    @Column(name = "diagnostic_aids", columnDefinition = "CLOB")
    private String diagnosticAids;

    @Column(name = "order_number")
    private String orderNumber;
}