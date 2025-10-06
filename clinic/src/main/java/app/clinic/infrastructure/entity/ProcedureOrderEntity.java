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
 * JPA entity representing procedure orders table in the database.
 * Specialized table for procedure order items.
 */
@Entity
@Table(name = "procedure_orders")
public class ProcedureOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "item_number", nullable = false)
    private Integer itemNumber;

    @Column(name = "procedure_name", nullable = false, length = 100)
    private String procedureName;

    @Column(name = "number_of_times", nullable = false)
    private Integer numberOfTimes;

    @Column(name = "frequency", nullable = false, length = 50)
    private String frequency;

    @Column(name = "cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "requires_specialist_assistance", nullable = false)
    private boolean requiresSpecialistAssistance;

    @Column(name = "specialist_type_id", length = 20)
    private String specialistTypeId;

    // Default constructor
    public ProcedureOrderEntity() {}

    // Constructor with parameters
    public ProcedureOrderEntity(OrderEntity order, Integer itemNumber, String procedureName,
                              Integer numberOfTimes, String frequency, BigDecimal cost,
                              boolean requiresSpecialistAssistance, String specialistTypeId) {
        this.order = order;
        this.itemNumber = itemNumber;
        this.procedureName = procedureName;
        this.numberOfTimes = numberOfTimes;
        this.frequency = frequency;
        this.cost = cost;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistTypeId = specialistTypeId;
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

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public Integer getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(Integer numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public boolean isRequiresSpecialistAssistance() {
        return requiresSpecialistAssistance;
    }

    public void setRequiresSpecialistAssistance(boolean requiresSpecialistAssistance) {
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
    }

    public String getSpecialistTypeId() {
        return specialistTypeId;
    }

    public void setSpecialistTypeId(String specialistTypeId) {
        this.specialistTypeId = specialistTypeId;
    }
}