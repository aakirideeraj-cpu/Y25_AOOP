import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {
    private final String accountNumber;
    private Money balance;
    private final Money overdraftLimit;
    private AccountStatus status;
    private final List<LedgerEntry> ledger = new ArrayList<>();

    public BankAccount(String accountNumber, Money openingBalance, Money overdraftLimit) {
        if (accountNumber == null || accountNumber.isBlank()) throw new IllegalArgumentException("Account number is required.");
        if (openingBalance == null || overdraftLimit == null) throw new IllegalArgumentException("Balance and overdraft limit are required.");
        if (!openingBalance.getCurrency().equals(overdraftLimit.getCurrency())) throw new IllegalArgumentException("Currency mismatch.");
        this.accountNumber = accountNumber;
        this.balance = openingBalance;
        this.overdraftLimit = overdraftLimit;
        this.status = AccountStatus.ACTIVE;
        ledger.add(new LedgerEntry("OPENING_BALANCE", openingBalance, "Account opened"));
    }

    public synchronized void deposit(Money amount) {
        validatePositive(amount);
        ensureActive();
        balance = balance.add(amount);
        ledger.add(new LedgerEntry("DEPOSIT", amount, "Cash deposit"));
    }

    public synchronized void withdraw(Money amount) {
        validatePositive(amount);
        ensureActive();
        long minimumAllowed = -overdraftLimit.getAmountInPaise();
        long newBalance = Math.subtractExact(balance.getAmountInPaise(), amount.getAmountInPaise());
        if (newBalance < minimumAllowed) throw new IllegalStateException("Overdraft limit exceeded.");
        balance = new Money(newBalance, balance.getCurrency());
        ledger.add(new LedgerEntry("WITHDRAWAL", amount, "Cash withdrawal"));
    }

    public synchronized void freeze() {
        if (status == AccountStatus.CLOSED) throw new IllegalStateException("Closed account cannot be frozen.");
        status = AccountStatus.FROZEN;
    }

    public synchronized void unfreeze() {
        if (status == AccountStatus.CLOSED) throw new IllegalStateException("Closed account cannot be unfrozen.");
        status = AccountStatus.ACTIVE;
    }

    public synchronized void close() {
        if (status == AccountStatus.CLOSED) throw new IllegalStateException("Account is already closed.");
        if (balance.getAmountInPaise() < 0) throw new IllegalStateException("Outstanding overdraft must be cleared before closure.");
        status = AccountStatus.CLOSED;
    }

    public synchronized Money getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public synchronized AccountStatus getStatus() { return status; }
    public synchronized List<LedgerEntry> getLedger() {
        return Collections.unmodifiableList(new ArrayList<>(ledger));
    }

    private void ensureActive() {
        if (status != AccountStatus.ACTIVE) throw new IllegalStateException("Only active accounts can transact.");
    }

    private void validatePositive(Money amount) {
        if (amount == null || !amount.isPositive()) throw new IllegalArgumentException("Transaction amount must be greater than zero.");
        if (!balance.getCurrency().equals(amount.getCurrency())) throw new IllegalArgumentException("Currency mismatch.");
    }
}
