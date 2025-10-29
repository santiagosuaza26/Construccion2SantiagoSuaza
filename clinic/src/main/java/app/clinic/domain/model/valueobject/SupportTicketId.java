package app.clinic.domain.model.valueobject;

import java.util.regex.Pattern;

public class SupportTicketId {
    private static final Pattern SUPPORT_TICKET_ID_PATTERN = Pattern.compile("^\\d{1,10}$");
    private final String value;

    public SupportTicketId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Support ticket ID cannot be null or empty");
        }
        if (!SUPPORT_TICKET_ID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Support ticket ID must contain only digits and be maximum 10 characters");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportTicketId that = (SupportTicketId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}