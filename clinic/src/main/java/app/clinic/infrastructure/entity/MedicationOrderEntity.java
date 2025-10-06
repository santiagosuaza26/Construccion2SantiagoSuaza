package app.clinic.infrastructure.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA entity representing medication orders table in the database.
 * Specialized table for medication order items.
 */
@Entity
@Table(name = "medication_orders")
public class MedicationOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "item_number", nullable = false)
    private Integer itemNumber;

    @Column(name = "medication_name", nullable = false, length = 100)
    private String medicationName;

    @Column(name = "dosage", nullable = false, length = 50)
    private String dosage;

    @Column(name = "treatment_duration", nullable = false, length = 50)
    private String treatmentDuration;

    @Column(name = "cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    // Default constructor
    public MedicationOrderEntity() {}

    // Constructor with parameters
    public MedicationOrderEntity(OrderEntity order, Integer itemNumber, String medicationName,
                               String dosage, String treatmentDuration, BigDecimal cost) {
        this.order = order;
        this.itemNumber = itemNumber;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.cost = cost;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTreatmentDuration() {
        return treatmentDuration;
    }

    public void setTreatmentDuration(String treatmentDuration) {
        this.treatmentDuration = treatmentDuration;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}