package app.clinic.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.InvoiceEntity;

/**
 * Repositorio JPA para operaciones de facturas.
 * Proporciona métodos para consultar y gestionar facturas en la base de datos.
 */
@Repository
public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {

    /**
     * Busca una factura por su número único.
     */
    InvoiceEntity findByInvoiceNumber(String invoiceNumber);

    /**
     * Busca todas las facturas de un paciente específico.
     */
    List<InvoiceEntity> findByPatientCedulaOrderByBillingDateDesc(String patientCedula);

    /**
     * Busca facturas por estado.
     */
    List<InvoiceEntity> findByStatusOrderByBillingDateDesc(InvoiceEntity.InvoiceStatus status);

    /**
     * Busca facturas de un paciente en un año específico.
     */
    List<InvoiceEntity> findByPatientCedulaAndYearOrderByBillingDateDesc(String patientCedula, Integer invoiceYear);

    /**
     * Cuenta el número total de facturas de un paciente.
     */
    long countByPatientCedula(String patientCedula);

    /**
     * Calcula el total de copagos acumulados para un paciente en un año específico.
     */
    @Query("SELECT COALESCE(SUM(i.copaymentAmount), 0) FROM InvoiceEntity i WHERE i.patientCedula = :patientCedula AND i.year = :invoiceYear AND i.status != 'CANCELLED'")
    java.math.BigDecimal getTotalCopaymentForYear(@Param("patientCedula") String patientCedula, @Param("invoiceYear") Integer invoiceYear);

    /**
     * Calcula el total facturado para un paciente en un año específico.
     */
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM InvoiceEntity i WHERE i.patientCedula = :patientCedula AND i.year = :invoiceYear AND i.status != 'CANCELLED'")
    java.math.BigDecimal getTotalBilledForYear(@Param("patientCedula") String patientCedula, @Param("invoiceYear") Integer invoiceYear);

    /**
     * Busca facturas pendientes de pago.
     */
    List<InvoiceEntity> findByStatusAndDueDateBeforeOrderByDueDateAsc(InvoiceEntity.InvoiceStatus status, java.time.LocalDateTime dueDate);

    /**
     * Verifica si existe una factura con el número dado.
     */
    boolean existsByInvoiceNumber(String invoiceNumber);
}