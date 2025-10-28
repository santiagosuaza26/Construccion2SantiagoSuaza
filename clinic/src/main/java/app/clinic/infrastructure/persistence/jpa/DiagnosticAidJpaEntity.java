package app.clinic.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnostic_aids")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticAidJpaEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private double cost;

    @Column(name = "requires_specialist")
    private boolean requiresSpecialist;

    @Column(name = "specialist_type")
    private String specialistType;
}