package app.domain.model;

public class MedicationOrder extends Order {

    private int dosage;
    private int durationDays;

    public MedicationOrder(String orderNumber, String patientId, String doctorId, String creationDate,
        int dosage, int durationDays) {
        super(orderNumber, patientId, doctorId, creationDate);
        this.dosage = dosage;
        this.durationDays = durationDays;
    }

    public int getDosage() {
        return dosage;
    }

    public int getDurationDays() {
        return durationDays;
    }
}