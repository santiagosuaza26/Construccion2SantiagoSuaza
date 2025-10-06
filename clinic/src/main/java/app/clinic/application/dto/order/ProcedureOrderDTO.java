package app.clinic.application.dto.order;

/**
 * Data Transfer Object for procedure order information.
 * Used for API responses containing procedure order data.
 */
public class ProcedureOrderDTO {
    private String orderNumber;
    private Integer itemNumber;
    private String procedureName;
    private Integer numberOfTimes;
    private String frequency;
    private String cost;
    private boolean requiresSpecialistAssistance;
    private String specialistTypeId;

    // Default constructor
    public ProcedureOrderDTO() {}

    // Constructor with parameters
    public ProcedureOrderDTO(String orderNumber, Integer itemNumber, String procedureName,
                            Integer numberOfTimes, String frequency, String cost,
                            boolean requiresSpecialistAssistance, String specialistTypeId) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
        this.procedureName = procedureName;
        this.numberOfTimes = numberOfTimes;
        this.frequency = frequency;
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
        return String.format("ProcedureOrderDTO{orderNumber='%s', itemNumber=%d, procedureName='%s'}",
                           orderNumber, itemNumber, procedureName);
    }
}