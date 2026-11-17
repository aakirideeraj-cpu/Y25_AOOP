import java.util.UUID;

public final class FundTransfer {
    private final String transferId = UUID.randomUUID().toString();
    private final BankAccount source;
    private final BankAccount destination;
    private final Money amount;

    public FundTransfer(BankAccount source, BankAccount destination, Money amount) {
        if (source == null || destination == null || amount == null) throw new IllegalArgumentException("All transfer values are required.");
        if (source == destination) throw new IllegalArgumentException("Source and destination must differ.");
        if (!amount.isPositive()) throw new IllegalArgumentException("Transfer amount must be positive.");
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public synchronized void transfer() {
        if (source.getStatus() != AccountStatus.ACTIVE || destination.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Both accounts must be active.");
        }
        boolean debited = false;
        try {
            source.withdraw(amount);
            debited = true;
            destination.deposit(amount);
            System.out.println("Transfer successful. Transfer ID: " + transferId);
        } catch (RuntimeException ex) {
            if (debited) source.deposit(amount); // compensating action for in-process failure
            throw new IllegalStateException("Transfer failed; source debit was reversed.", ex);
        }
    }
}
