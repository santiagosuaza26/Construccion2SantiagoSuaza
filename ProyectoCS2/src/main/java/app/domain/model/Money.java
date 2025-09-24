package app.domain.model;

public final class Money {
    private final long amount; // en pesos colombianos (centavos)
    
    public Money(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }
    
    public static Money of(long pesos) {
        return new Money(pesos);
    }
    
    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }
    
    public Money subtract(Money other) {
        if (this.amount < other.amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        return new Money(this.amount - other.amount);
    }
    
    public Money multiply(int factor) {
        return new Money(this.amount * factor);
    }
    
    public boolean isGreaterThan(Money other) {
        return this.amount > other.amount;
    }
    
    public boolean isLessThanOrEqual(Money other) {
        return this.amount <= other.amount;
    }
    
    public long getAmount() { return amount; }
    
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
}