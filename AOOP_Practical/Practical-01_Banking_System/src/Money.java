import java.util.Objects;

public final class Money {
    private final long amountInPaise;
    private final String currency;

    public Money(long amountInPaise, String currency) {
        if (amountInPaise < 0) throw new IllegalArgumentException("Money cannot be negative.");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Currency is required.");
        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }

    public static Money rupees(long rupees) {
        if (rupees < 0) throw new IllegalArgumentException("Amount cannot be negative.");
        return new Money(Math.multiplyExact(rupees, 100), "INR");
    }

    public long getAmountInPaise() { return amountInPaise; }
    public String getCurrency() { return currency; }

    public Money add(Money other) {
        checkCurrency(other);
        return new Money(Math.addExact(amountInPaise, other.amountInPaise), currency);
    }

    public Money subtract(Money other) {
        checkCurrency(other);
        if (other.amountInPaise > amountInPaise) throw new IllegalArgumentException("Insufficient money for subtraction.");
        return new Money(amountInPaise - other.amountInPaise, currency);
    }

    public boolean isPositive() { return amountInPaise > 0; }

    private void checkCurrency(Money other) {
        Objects.requireNonNull(other, "Money cannot be null.");
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch.");
    }

    @Override
    public String toString() {
        return currency + " " + String.format("%.2f", amountInPaise / 100.0);
    }
}
