package com.example.easy;

import java.util.Objects;

public class UserTransaction {
    private String userId;

    private String transactionId;

    public UserTransaction() {

    }

    public UserTransaction(String userId, String transactionId) {
        this.userId = Objects.requireNonNull(userId, "userId");
        this.transactionId = Objects.requireNonNull(transactionId, "transactionId");
    }

    public String getUserId() {
        return userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTransaction that = (UserTransaction) o;
        return Objects.equals(userId, that.userId) && Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, transactionId);
    }

    @Override
    public String toString() {
        return "UserTransaction{" +
                "userId='" + userId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
