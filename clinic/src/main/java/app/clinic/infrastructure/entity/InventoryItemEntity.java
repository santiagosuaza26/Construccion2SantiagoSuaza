package app.clinic.infrastructure.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing inventory items table in the database.
 * Maps domain InventoryItem objects to database records.
 * Handles medications, procedures, and diagnostic aids.
 */
@Entity
@Table(name = "inventory_items")
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InventoryItemType type;

    @Column(name = "cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "description", length = 255)
    private String description;

    // Default constructor
    public InventoryItemEntity() {}

    // Constructor with parameters
    public InventoryItemEntity(String name, InventoryItemType type, BigDecimal cost, boolean active, String description) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.active = active;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InventoryItemType getType() {
        return type;
    }

    public void setType(InventoryItemType type) {
        this.type = type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Enumeration for inventory item types in the database.
     */
    public enum InventoryItemType {
        MEDICAMENTO,
        PROCEDIMIENTO,
        AYUDA_DIAGNOSTICA
    }
}