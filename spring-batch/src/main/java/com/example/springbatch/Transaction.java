package com.example.springbatch;

import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private int id;
    private String transactionId;
    private String timestamp;
    private String vendor;
    private String amount;

    Transaction() {

    }

    Transaction(int id, String transactionId, String timestamp, String vendor, String amount) {
        this.id = id;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.vendor = vendor;
        this.amount = amount;
    }

    Transaction(String transactionId, String timestamp, String vendor, String amount) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.vendor = vendor;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(transactionId, that.transactionId) && Objects.equals(timestamp, that.timestamp) && Objects.equals(vendor, that.vendor) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionId, timestamp, vendor, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", vendor='" + vendor + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
