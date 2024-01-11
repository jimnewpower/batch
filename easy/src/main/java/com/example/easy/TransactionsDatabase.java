package com.example.easy;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TransactionsDatabase implements Queryable<Transaction> {

    private final Map<UUID, Transaction> transactions;

    public TransactionsDatabase(Map<UUID, Transaction> transactions) {
        this.transactions = Objects.requireNonNull(transactions, "transactions");
    }

    @Override
    public Transaction get(UUID key) {
        return transactions.get(key);
    }
}
