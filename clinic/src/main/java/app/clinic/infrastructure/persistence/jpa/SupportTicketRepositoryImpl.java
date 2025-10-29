package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.domain.model.valueobject.SupportTicketId;
import app.clinic.domain.repository.SupportTicketRepository;

@Repository
public class SupportTicketRepositoryImpl implements SupportTicketRepository {
    private final SupportTicketJpaRepository jpaRepository;

    public SupportTicketRepositoryImpl(SupportTicketJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(SupportTicket supportTicket) {
        SupportTicketJpaEntity entity = new SupportTicketJpaEntity(
            supportTicket.getId().getValue(),
            supportTicket.getUserId(),
            supportTicket.getIssueDescription(),
            supportTicket.getCreatedAt(),
            supportTicket.getStatus(),
            supportTicket.getAssignedTo(),
            supportTicket.getUpdatedAt()
        );
        jpaRepository.save(entity);
    }

    @Override
    public Optional<SupportTicket> findById(SupportTicketId id) {
        return jpaRepository.findById(id.getValue())
            .map(this::toDomain);
    }

    @Override
    public List<SupportTicket> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupportTicket> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(SupportTicketId id) {
        return jpaRepository.existsById(id.getValue());
    }

    private SupportTicket toDomain(SupportTicketJpaEntity entity) {
        SupportTicket supportTicket = new SupportTicket(
            new SupportTicketId(entity.getId()),
            entity.getUserId(),
            entity.getIssueDescription(),
            entity.getCreatedAt()
        );

        // Set additional fields if they exist
        if (entity.getAssignedTo() != null) {
            supportTicket.assignTo(entity.getAssignedTo());
        }

        // Update status and timestamps
        if (entity.getStatus() != null) {
            switch (entity.getStatus()) {
                case IN_PROGRESS:
                    supportTicket.assignTo(entity.getAssignedTo() != null ? entity.getAssignedTo() : "system");
                    break;
                case CLOSED:
                    supportTicket.close();
                    break;
                case OPEN:
                default:
                    // Already set to OPEN by constructor
                    break;
            }
        }

        return supportTicket;
    }
}