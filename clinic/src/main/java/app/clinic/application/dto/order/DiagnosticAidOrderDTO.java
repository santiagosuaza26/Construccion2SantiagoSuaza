package app.clinic.application.dto.order;

/**
 * Data Transfer Object for diagnostic aid order information.
 * Used for API responses containing diagnostic aid order data.
 */
public class DiagnosticAidOrderDTO {
    private String orderNumber;
    private Integer itemNumber;
    private String diagnosticAidName;
    private Integer quantity;
    private String cost;
    private boolean requiresSpecialistAssistance;
    private String specialistTypeId;

    // Default constructor
    public DiagnosticAidOrderDTO() {}

    // Constructor with parameters
    public DiagnosticAidOrderDTO(String orderNumber, Integer itemNumber, String diagnosticAidName,
                                Integer quantity, String cost, boolean requiresSpecialistAssistance,
                                String specialistTypeId) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
        this.diagnosticAidName = diagnosticAidName;
        this.quantity = quantity;
        this.cost = cost;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistTypeId = specialistTypeId;
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getDiagnosticAidName() {
        return diagnosticAidName;
    }

    public void setDiagnosticAidName(String diagnosticAidName) {
        this.diagnosticAidName = diagnosticAidName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
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

    @Override
    public String toString() {
        return String.format("DiagnosticAidOrderDTO{orderNumber='%s', itemNumber=%d, diagnosticAidName='%s'}",
                           orderNumber, itemNumber, diagnosticAidName);
    }
}