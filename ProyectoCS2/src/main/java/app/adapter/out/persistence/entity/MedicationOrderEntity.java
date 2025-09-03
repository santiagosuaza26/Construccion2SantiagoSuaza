package app.adapter.out.persistence.entity;

import app.domain.model.MedicationOrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medication_orders")
//@IdClass(OrderItemEntityId.class)
public class MedicationOrderEntity {
    @Id
    @Column(name = "order_number", length = 6)
    private String orderNumber;
    
    @Id
    @Column(name = "item_number")
    private Integer itemNumber;
    
    @Column(name = "medication_name", nullable = false)
    private String medicationName;
    
    @Column(name = "dosage", nullable = false)
    private String dosage;
    
    @Column(name = "treatment_duration", nullable = false)
    private String treatmentDuration;
    
    @Column(name = "cost", nullable = false)
    private Long cost;

    public MedicationOrderEntity() {}

    public MedicationOrderEntity(String orderNumber, Integer itemNumber, String medicationName, 
                               String dosage, String treatmentDuration, Long cost) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.cost = cost;
    }

    public MedicationOrderItem toDomain(String medicationId) {
        return new MedicationOrderItem(orderNumber, itemNumber, medicationId, medicationName,
                                     dosage, treatmentDuration, cost);
    }

    public static MedicationOrderEntity fromDomain(MedicationOrderItem item) {
        return new MedicationOrderEntity(
            item.getOrderNumber(),
            item.getItemNumber(),
            item.getMedicationName(),
            item.getDosage(),
            item.getTreatmentDuration(),
            item.getCost()
        );
    }

    // Getters and setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Integer getItemNumber() { return itemNumber; }
    public void setItemNumber(Integer itemNumber) { this.itemNumber = itemNumber; }
    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getTreatmentDuration() { return treatmentDuration; }
    public void setTreatmentDuration(String treatmentDuration) { this.treatmentDuration = treatmentDuration; }
    public Long getCost() { return cost; }
    public void setCost(Long cost) { this.cost = cost; }
}