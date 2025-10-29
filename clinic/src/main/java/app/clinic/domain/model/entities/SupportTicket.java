package app.clinic.domain.model.entities;

import java.time.LocalDateTime;

import app.clinic.domain.model.valueobject.SupportTicketId;
import app.clinic.domain.model.valueobject.SupportTicketStatus;

public class SupportTicket {
    private final SupportTicketId id;
    private final String userId;
    private final String issueDescription;
    private final LocalDateTime createdAt;
    private SupportTicketStatus status;
    private String assignedTo;
    private LocalDateTime updatedAt;

    public SupportTicket(SupportTicketId id, String userId, String issueDescription, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.issueDescription = issueDescription;
        this.createdAt = createdAt;
        this.status = SupportTicketStatus.OPEN;
        this.updatedAt = createdAt;
    }

    public SupportTicketId getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SupportTicketStatus getStatus() {
        return status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void assignTo(String assignedTo) {
        this.assignedTo = assignedTo;
        this.status = SupportTicketStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void close() {
        this.status = SupportTicketStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportTicket that = (SupportTicket) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SupportTicket{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", assignedTo='" + assignedTo + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}