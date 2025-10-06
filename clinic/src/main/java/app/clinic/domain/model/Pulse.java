package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing heart rate (pulse) in beats per minute.
 * Validates normal pulse range (30-250 BPM).
 */
public class Pulse {
    private final int value;

    private Pulse(int value) {
        if (!isValidPulse(value)) {
            throw new IllegalArgumentException("Pulse must be between 30 and 250 BPM: " + value);
        }
        this.value = value;
    }

    public static Pulse of(int value) {
        return new Pulse(value);
    }

    private boolean isValidPulse(int pulse) {
        return pulse >= 30 && pulse <= 250;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pulse pulse = (Pulse) o;
        return value == pulse.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + " BPM";
    }
}