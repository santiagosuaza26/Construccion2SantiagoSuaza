package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "billings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingJpaEntity {
    @Id
    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "age")
    private int age;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "company")
    private String company;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "validity_days")
    private int validityDays;

    @Column(name = "validity_date")
    private LocalDate validityDate;

    @Column(name = "total_cost")
    private double totalCost;

    @Column(name = "copay")
    private double copay;

    @Column(name = "insurance_coverage")
    private double insuranceCoverage;

    @Column(name = "applied_medications")
    private String appliedMedications;

    @Column(name = "applied_procedures")
    private String appliedProcedures;

    @Column(name = "applied_diagnostic_aids")
    private String appliedDiagnosticAids;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "generated_by")
    private String generatedBy;
}