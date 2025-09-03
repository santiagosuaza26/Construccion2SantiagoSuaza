package app.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medications")
public class MedicationEntity {
    @Id
    private String id;

    private String name;
    private String description;
    private int stock;
    private long price;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
}