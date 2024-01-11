package com.example.homegrown;

import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private UUID uuid;
    private String timestamp;
    private String vendor;
    private String amount;

    public Transaction() {

    }

    public Transaction(String csv) {
        Objects.requireNonNull(csv, "csv");
        String[] fields = csv.split(",");
        this.uuid = UUID.fromString(fields[0]);
        this.timestamp = fields[1];
        this.vendor = fields[2];
        this.amount = fields[3];
    }

    public Transaction(UUID uuid, String timestamp, String vendor, String amount) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.vendor = vendor;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(timestamp, that.timestamp) && Objects.equals(vendor, that.vendor) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, timestamp, vendor, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "uuid=" + uuid +
                ", timestamp='" + timestamp + '\'' +
                ", vendor='" + vendor + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
