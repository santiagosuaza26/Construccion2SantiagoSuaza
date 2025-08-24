package app.domain.model;

public class ProcedureOrder extends Order {

    private int frequency;
    private boolean requiresSpecialist;
    private String specialistType;

    public ProcedureOrder(String orderNumber, String patientId, String doctorId, String creationDate,
        int frequency, boolean requiresSpecialist, String specialistType) {
        super(orderNumber, patientId, doctorId, creationDate);
        this.frequency = frequency;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public int getFrequency() {
        return frequency;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public String getSpecialistType() {
        return specialistType;
    }
}

