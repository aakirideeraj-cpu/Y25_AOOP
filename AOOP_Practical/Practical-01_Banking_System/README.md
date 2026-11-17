# AOOP Practical 1 – Bank Account and Money Transfers

## Problem
A Java banking domain model supporting deposits, withdrawals, account states, fund transfers and an immutable transaction ledger.

## Requirements
- JDK 17 or later
- VS Code Java Extension Pack or another Java IDE

## Run in VS Code terminal
From this folder, compile and run:

```bash
javac -d out src/*.java
java -cp out Main
```

## Classes
- **Money:** immutable value object, stores amount in paise and currency.
- **AccountStatus:** ACTIVE, FROZEN and CLOSED states.
- **BankAccount:** encapsulates balance, overdraft limit, status and ledger; validates all state-changing operations.
- **LedgerEntry:** immutable transaction record.
- **FundTransfer:** coordinates a transfer between two accounts.
- **Main:** demonstration and basic validation.

## Business invariants demonstrated
1. Transaction amounts must be positive.
2. Only ACTIVE accounts can deposit or withdraw.
3. Withdrawal cannot exceed the permitted overdraft limit.
4. Account number is required and does not change.
5. Account and transaction currencies must match.
6. Completed ledger entries are immutable.
7. A CLOSED account cannot be reactivated.
8. An account cannot be closed while an overdraft remains outstanding.

## Transfer failure discussion
This is an in-memory teaching example. The transfer method attempts a compensating reversal if a runtime error occurs after debit. A process crash cannot be made safe by this technique alone. A production system should use a database transaction with atomic commit/rollback, durable transfer state, idempotency and reconciliation. Transfers across different banks require a distributed settlement workflow rather than one local database transaction.

## Limitation
This practical does not persist data after program exit and is not production banking software.
