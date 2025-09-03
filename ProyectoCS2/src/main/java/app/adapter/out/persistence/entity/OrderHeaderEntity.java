package app.adapter.out.persistence.entity;

import java.time.LocalDate;

import app.domain.model.OrderHeader;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_headers")
public class OrderHeaderEntity {
    @Id
    @Column(name = "order_number", length = 6)
    private String orderNumber;
    
    @Column(name = "patient_id_card", nullable = false)
    private String patientIdCard;
    
    @Column(name = "doctor_id_card", nullable = false)
    private String doctorIdCard;
    
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    public OrderHeaderEntity() {}

    public OrderHeaderEntity(String orderNumber, String patientIdCard, String doctorIdCard, LocalDate creationDate) {
        this.orderNumber = orderNumber;
        this.patientIdCard = patientIdCard;
        this.doctorIdCard = doctorIdCard;
        this.creationDate = creationDate;
    }

    public OrderHeader toDomain() {
        return new OrderHeader(orderNumber, patientIdCard, doctorIdCard, creationDate);
    }

    public static OrderHeaderEntity fromDomain(OrderHeader orderHeader) {
        return new OrderHeaderEntity(
            orderHeader.getOrderNumber(),
            orderHeader.getPatientIdCard(),
            orderHeader.getDoctorIdCard(),
            orderHeader.getCreationDate()
        );
    }

    // Getters and setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getPatientIdCard() { return patientIdCard; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public void setDoctorIdCard(String doctorIdCard) { this.doctorIdCard = doctorIdCard; }
    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
}