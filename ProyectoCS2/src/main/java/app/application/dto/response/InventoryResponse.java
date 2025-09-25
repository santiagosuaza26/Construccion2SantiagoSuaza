package app.application.dto.response;

import java.util.List;

public class InventoryResponse {
    
    private String itemType;
    private String itemId;
    private String name;
    private String description;
    private long price;
    private Integer stock;
    private String lastUpdated;
    private String createdBy;
    
    // Para respuestas de listas de inventario
    private List<InventoryItemInfo> items;
    private int totalItems;
    private String inventoryType;
    
    // Default constructor
    public InventoryResponse() {}
    
    // Constructor para item individual
    public InventoryResponse(String itemType, String itemId, String name, String description, long price) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Constructor para medicamento con stock
    public InventoryResponse(String itemType, String itemId, String name, String description, 
                           long price, Integer stock, String lastUpdated) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.lastUpdated = lastUpdated;
    }
    
    // Constructor para listas de inventario
    public InventoryResponse(String inventoryType, List<InventoryItemInfo> items, int totalItems) {
        this.inventoryType = inventoryType;
        this.items = items;
        this.totalItems = totalItems;
    }
    
    // Getters
    public String getItemType() { return itemType; }
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public long getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getLastUpdated() { return lastUpdated; }
    public String getCreatedBy() { return createdBy; }
    public List<InventoryItemInfo> getItems() { return items; }
    public int getTotalItems() { return totalItems; }
    public String getInventoryType() { return inventoryType; }
    
    // Setters
    public void setItemType(String itemType) { this.itemType = itemType; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(long price) { this.price = price; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setItems(List<InventoryItemInfo> items) { this.items = items; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public void setInventoryType(String inventoryType) { this.inventoryType = inventoryType; }
    
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
    
    public boolean hasStock() {
        return stock != null;
    }
    
    public boolean isInStock() {
        return hasStock() && stock > 0;
    }
    
    public boolean isLowStock() {
        return hasStock() && stock <= 10 && stock > 0;
    }
    
    public boolean isOutOfStock() {
        return hasStock() && stock <= 0;
    }
    
    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }
    
    public int getItemsCount() {
        return items != null ? items.size() : 0;
    }
    
    public String getFormattedPrice() {
        return String.format("$%,d", price);
    }
    
    public String getStockStatus() {
        if (!hasStock()) return "N/A";
        if (isOutOfStock()) return "OUT_OF_STOCK";
        if (isLowStock()) return "LOW_STOCK";
        return "IN_STOCK";
    }
    
    // Clase interna para items de inventario en listas
    public static class InventoryItemInfo {
        private String itemType;
        private String itemId;
        private String name;
        private String description;
        private long price;
        private Integer stock;
        private String stockStatus;
        private boolean available;
        private String lastUpdated;
        
        public InventoryItemInfo() {}
        
        // Constructor básico
        public InventoryItemInfo(String itemType, String itemId, String name, long price, boolean available) {
            this.itemType = itemType;
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.available = available;
        }
        
        // Constructor con stock (para medicamentos)
        public InventoryItemInfo(String itemType, String itemId, String name, String description,
                               long price, Integer stock, String stockStatus, boolean available, String lastUpdated) {
            this.itemType = itemType;
            this.itemId = itemId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
            this.stockStatus = stockStatus;
            this.available = available;
            this.lastUpdated = lastUpdated;
        }
        
        // Getters and Setters
        public String getItemType() { return itemType; }
        public void setItemType(String itemType) { this.itemType = itemType; }
        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getPrice() { return price; }
        public void setPrice(long price) { this.price = price; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public String getStockStatus() { return stockStatus; }
        public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        public String getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
        
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
        
        public boolean hasStock() {
            return stock != null;
        }
        
        public boolean isInStock() {
            return hasStock() && stock > 0;
        }
        
        public boolean isLowStock() {
            return "LOW_STOCK".equals(stockStatus);
        }
        
        public boolean isOutOfStock() {
            return "OUT_OF_STOCK".equals(stockStatus);
        }
        
        public String getFormattedPrice() {
            return String.format("$%,d", price);
        }
        
        @Override
        public String toString() {
            return "InventoryItemInfo{" +
                    "itemType='" + itemType + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", name='" + name + '\'' +
                    ", price=" + getFormattedPrice() +
                    ", stock=" + stock +
                    ", stockStatus='" + stockStatus + '\'' +
                    ", available=" + available +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        if (items != null) {
            // Es una lista de inventario
            return "InventoryResponse{" +
                    "inventoryType='" + inventoryType + '\'' +
                    ", totalItems=" + totalItems +
                    ", itemsCount=" + getItemsCount() +
                    '}';
        } else {
            // Es un item individual
            return "InventoryResponse{" +
                    "itemType='" + itemType + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", name='" + name + '\'' +
                    ", price=" + getFormattedPrice() +
                    ", stock=" + stock +
                    ", stockStatus='" + getStockStatus() + '\'' +
                    ", lastUpdated='" + lastUpdated + '\'' +
                    '}';
        }
    }
}