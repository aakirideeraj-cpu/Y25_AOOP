public class Main {
    public static void main(String[] args) {
        BankAccount accountA = new BankAccount("1001", Money.rupees(10000), Money.rupees(2000));
        BankAccount accountB = new BankAccount("1002", Money.rupees(3000), Money.rupees(1000));

        System.out.println("Initial A balance: " + accountA.getBalance());
        System.out.println("Initial B balance: " + accountB.getBalance());

        accountA.deposit(Money.rupees(500));
        accountA.withdraw(Money.rupees(1000));

        new FundTransfer(accountA, accountB, Money.rupees(5000)).transfer();

        System.out.println("\nAfter transactions:");
        System.out.println("A balance: " + accountA.getBalance());
        System.out.println("B balance: " + accountB.getBalance());

        System.out.println("\nAccount A ledger:");
        for (LedgerEntry entry : accountA.getLedger()) System.out.println(entry);

        System.out.println("\nTesting frozen account:");
        accountB.freeze();
        try {
            accountB.withdraw(Money.rupees(100));
        } catch (IllegalStateException ex) {
            System.out.println("Expected validation: " + ex.getMessage());
        }
        accountB.unfreeze();

        System.out.println("\nPractical executed successfully.");
    }
}
