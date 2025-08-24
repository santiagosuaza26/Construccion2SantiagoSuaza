package app.domain.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Order {

    private String orderNumber;
    private String patientId;
    private String doctorId;
    private String creationDate;
    private List<InventoryItem> items;

    public Order(String orderNumber, String patientId, String doctorId, String creationDate) {
        this.orderNumber = orderNumber;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.creationDate = creationDate;
        this.items = new ArrayList<>();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }
}

