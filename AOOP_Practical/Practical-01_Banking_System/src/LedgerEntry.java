import java.time.LocalDateTime;
import java.util.UUID;

public final class LedgerEntry {
    private final String transactionId;
    private final String transactionType;
    private final Money amount;
    private final LocalDateTime timestamp;
    private final String description;

    public LedgerEntry(String transactionType, Money amount, String description) {
        this.transactionId = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public String getTransactionType() { return transactionType; }
    public Money getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return timestamp + " | " + transactionType + " | " + amount + " | " + description;
    }
}
