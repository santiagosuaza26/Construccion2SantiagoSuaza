package app.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import app.domain.model.MedicalRecord;
import app.domain.model.Order;
import app.domain.repository.MedicalRecordRepository;
import app.domain.repository.OrderRepository;

@Service
public class DoctorService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final OrderRepository orderRepository;

    public DoctorService(MedicalRecordRepository medicalRecordRepository, OrderRepository orderRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.orderRepository = orderRepository;
    }

    public void addDiagnosis(String patientId, String diagnosis) {
        MedicalRecord record = medicalRecordRepository.findById(patientId)
                .orElse(new MedicalRecord(patientId));
        record.addEntry(LocalDateTime.now().toString(), "Diagnosis: " + diagnosis);
        medicalRecordRepository.save(record);
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }
}