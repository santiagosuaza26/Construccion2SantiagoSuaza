package app.clinic.infrastructure.dto;

import java.time.LocalDateTime;

import app.clinic.domain.model.valueobject.SupportTicketStatus;

public class SupportTicketDTO {
    private String id;
    private String userId;
    private String issueDescription;
    private LocalDateTime createdAt;
    private SupportTicketStatus status;
    private String assignedTo;
    private LocalDateTime updatedAt;

    public SupportTicketDTO() {}

    public SupportTicketDTO(String id, String userId, String issueDescription,
                           LocalDateTime createdAt, SupportTicketStatus status,
                           String assignedTo, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.issueDescription = issueDescription;
        this.createdAt = createdAt;
        this.status = status;
        this.assignedTo = assignedTo;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public SupportTicketStatus getStatus() {
        return status;
    }

    public void setStatus(SupportTicketStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}