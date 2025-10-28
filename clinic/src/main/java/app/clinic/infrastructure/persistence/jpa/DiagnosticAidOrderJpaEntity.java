package app.clinic.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnostic_aid_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticAidOrderJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "item")
    private int item;

    @Column(name = "diagnostic_aid_id")
    private String diagnosticAidId;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "requires_specialist")
    private boolean requiresSpecialist;

    @Column(name = "specialist_id")
    private String specialistId;

    @Column(name = "cost")
    private double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_number", insertable = false, updatable = false)
    private OrderJpaEntity order;
}