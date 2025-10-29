package app.clinic.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketJpaRepository extends JpaRepository<SupportTicketJpaEntity, String> {
    List<SupportTicketJpaEntity> findByUserId(String userId);
}