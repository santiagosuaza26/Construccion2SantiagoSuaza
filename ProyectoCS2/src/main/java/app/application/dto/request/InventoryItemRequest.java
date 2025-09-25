package app.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class InventoryItemRequest {
    
    @NotBlank(message = "Item type is required")
    @Pattern(regexp = "MEDICATION|PROCEDURE|DIAGNOSTIC|SPECIALTY", 
                message = "Item type must be: MEDICATION, PROCEDURE, DIAGNOSTIC, or SPECIALTY")
    private String itemType;
    
    @NotBlank(message = "Item ID is required")
    private String itemId;
    
    @NotBlank(message = "Item name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be at least 0")
    private Long price;
    
    // Solo para medicamentos
    @Min(value = 0, message = "Stock must be at least 0")
    private Integer stock;
    
    // Para operaciones de actualización de stock
    private Integer stockChange;
    
    @Pattern(regexp = "ADD|SUBTRACT", message = "Stock operation must be ADD or SUBTRACT")
    private String stockOperation;
    
    // Default constructor
    public InventoryItemRequest() {}
    
    // Constructor para crear item básico
    public InventoryItemRequest(String itemType, String itemId, String name, String description, Long price) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Constructor para crear medicamento con stock
    public InventoryItemRequest(String itemType, String itemId, String name, String description, 
                              Long price, Integer stock) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }
    
    // Constructor para actualización de stock
    public InventoryItemRequest(String itemId, Integer stockChange, String stockOperation) {
        this.itemId = itemId;
        this.stockChange = stockChange;
        this.stockOperation = stockOperation;
    }
    
    // Getters
    public String getItemType() { return itemType; }
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Long getPrice() { return price; }
    public Integer getStock() { return stock; }
    public Integer getStockChange() { return stockChange; }
    public String getStockOperation() { return stockOperation; }
    
    // Setters
    public void setItemType(String itemType) { this.itemType = itemType; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Long price) { this.price = price; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setStockChange(Integer stockChange) { this.stockChange = stockChange; }
    public void setStockOperation(String stockOperation) { this.stockOperation = stockOperation; }
    
    // Utility methods
    public boolean isMedication() {
        return "MEDICATION".equals(itemType);
    }
    
    public boolean isProcedure() {
        return "PROCEDURE".equals(itemType);
    }
    
    public boolean isDiagnostic() {
        return "DIAGNOSTIC".equals(itemType);
    }
    
    public boolean isSpecialty() {
        return "SPECIALTY".equals(itemType);
    }
    
    public boolean isStockOperation() {
        return stockChange != null && stockOperation != null;
    }
    
    public boolean isAddOperation() {
        return "ADD".equals(stockOperation);
    }
    
    public boolean isSubtractOperation() {
        return "SUBTRACT".equals(stockOperation);
    }
    
    @Override
    public String toString() {
        return "InventoryItemRequest{" +
                "itemType='" + itemType + '\'' +
                ", itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", stockChange=" + stockChange +
                ", stockOperation='" + stockOperation + '\'' +
                '}';
    }
    
    // Clases internas específicas para cada tipo de inventario
    public static class MedicationRequest extends InventoryItemRequest {
        public MedicationRequest() {
            super();
            setItemType("MEDICATION");
        }
        
        public MedicationRequest(String itemId, String name, String description, Long price, Integer stock) {
            super("MEDICATION", itemId, name, description, price, stock);
        }
    }
    
    public static class ProcedureRequest extends InventoryItemRequest {
        public ProcedureRequest() {
            super();
            setItemType("PROCEDURE");
        }
        
        public ProcedureRequest(String itemId, String name, String description, Long price) {
            super("PROCEDURE", itemId, name, description, price);
        }
    }
    
    public static class DiagnosticRequest extends InventoryItemRequest {
        public DiagnosticRequest() {
            super();
            setItemType("DIAGNOSTIC");
        }
        
        public DiagnosticRequest(String itemId, String name, String description, Long price) {
            super("DIAGNOSTIC", itemId, name, description, price);
        }
    }
    
    public static class SpecialtyRequest extends InventoryItemRequest {
        public SpecialtyRequest() {
            super();
            setItemType("SPECIALTY");
        }
        
        public SpecialtyRequest(String itemId, String name, String description) {
            super("SPECIALTY", itemId, name, description, 0L);
        }
    }
    
    public static class StockUpdateRequest extends InventoryItemRequest {
        public StockUpdateRequest(String itemId, Integer stockChange, String stockOperation) {
            super(itemId, stockChange, stockOperation);
        }
    }
}