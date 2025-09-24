package app.domain.model;

public final class Money {
    private final long amount; // en pesos colombianos
    
    public Money(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }
    
    public static Money of(long pesos) {
        return new Money(pesos);
    }
    
    public static Money zero() {
        return new Money(0);
    }
    
    public Money add(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add null money");
        }
        return new Money(this.amount + other.amount);
    }
    
    public Money subtract(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot subtract null money");
        }
        if (this.amount < other.amount) {
            throw new IllegalArgumentException("Insufficient funds for subtraction");
        }
        return new Money(this.amount - other.amount);
    }
    
    public Money multiply(int factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Cannot multiply by negative factor");
        }
        return new Money(this.amount * factor);
    }
    
    public Money multiply(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Cannot multiply by negative factor");
        }
        return new Money(Math.round(this.amount * factor));
    }
    
    public Money divide(int divisor) {
        if (divisor <= 0) {
            throw new IllegalArgumentException("Cannot divide by zero or negative number");
        }
        return new Money(this.amount / divisor);
    }
    
    public boolean isGreaterThan(Money other) {
        if (other == null) return true;
        return this.amount > other.amount;
    }
    
    public boolean isGreaterThanOrEqual(Money other) {
        if (other == null) return true;
        return this.amount >= other.amount;
    }
    
    public boolean isLessThan(Money other) {
        if (other == null) return false;
        return this.amount < other.amount;
    }
    
    public boolean isLessThanOrEqual(Money other) {
        if (other == null) return false;
        return this.amount <= other.amount;
    }
    
    public boolean isZero() {
        return this.amount == 0;
    }
    
    public boolean isPositive() {
        return this.amount > 0;
    }
    
    public Money percentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        return new Money(Math.round(this.amount * percentage / 100));
    }
    
    public long getAmount() { 
        return amount; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount == money.amount;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(amount);
    }
    
    @Override
    public String toString() {
        return String.format("$%,d COP", amount);
    }
    
    public String toFormattedString() {
        return String.format("$%,d", amount);
    }
}