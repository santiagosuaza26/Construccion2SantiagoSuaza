package app.clinic.application.dto.order;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new medical orders.
 * Contains validation annotations for input data.
 */
public class CreateOrderDTO {

    @NotBlank(message = "Patient cedula is required")
    @Size(max = 20, message = "Patient cedula must not exceed 20 characters")
    private String patientCedula;

    @NotBlank(message = "Doctor cedula is required")
    @Size(max = 10, message = "Doctor cedula must not exceed 10 characters")
    private String doctorCedula;

    @NotBlank(message = "Order number is required")
    @Size(max = 10, message = "Order number must not exceed 10 characters")
    private String orderNumber;

    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> items;

    // Default constructor
    public CreateOrderDTO() {}

    // Constructor with parameters
    public CreateOrderDTO(String patientCedula, String doctorCedula, String orderNumber, List<OrderItemDTO> items) {
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.orderNumber = orderNumber;
        this.items = items;
    }

    // Getters and Setters
    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return String.format("CreateOrderDTO{patientCedula='%s', doctorCedula='%s', orderNumber='%s', items=%d}",
                           patientCedula, doctorCedula, orderNumber, items != null ? items.size() : 0);
    }
}