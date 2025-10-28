package app.clinic.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vital_signs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VitalSignsId.class)
public class VitalSignsJpaEntity {
    @Id
    @Column(name = "patient_identification_number")
    private String patientIdentificationNumber;

    @Id
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "pulse")
    private int pulse;

    @Column(name = "oxygen_level")
    private int oxygenLevel;

    @Column(name = "observations")
    private String observations;
}