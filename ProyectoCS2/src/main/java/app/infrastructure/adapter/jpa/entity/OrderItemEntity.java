package app.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private int itemNumber;
    private String type; // MEDICATION, PROCEDURE, DIAGNOSTIC
    private String referenceId;
    private String name;
    private String dose;
    private int quantity;
    private String frequency;
    private boolean specialistRequired;
    private String specialtyId;
    private long cost;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public int getItemNumber() { return itemNumber; }
    public void setItemNumber(int itemNumber) { this.itemNumber = itemNumber; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public boolean isSpecialistRequired() { return specialistRequired; }
    public void setSpecialistRequired(boolean specialistRequired) { this.specialistRequired = specialistRequired; }
    public String getSpecialtyId() { return specialtyId; }
    public void setSpecialtyId(String specialtyId) { this.specialtyId = specialtyId; }
    public long getCost() { return cost; }
    public void setCost(long cost) { this.cost = cost; }
}