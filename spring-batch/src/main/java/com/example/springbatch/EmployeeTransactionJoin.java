package com.example.springbatch;

import java.util.Objects;

public class EmployeeTransactionJoin {

    private int id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String employeeNumber;
    private String transactionId;
    private String timestamp;
    private String vendor;
    private String amount;
    

    public EmployeeTransactionJoin() {

    }

    EmployeeTransactionJoin(
            int id,
            String employeeId,
            String firstName,
            String lastName,
            String employeeNumber,
            String transactionId,
            String timestamp,
            String vendor,
            String amount
    ) {
        this.id = id;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.vendor = vendor;
        this.amount = amount;
    }

    EmployeeTransactionJoin(
            String employeeId,
            String firstName,
            String lastName,
            String employeeNumber,
            String transactionId,
            String timestamp,
            String vendor,
            String amount
    ) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.vendor = vendor;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeTransactionJoin that = (EmployeeTransactionJoin) o;
        return id == that.id && Objects.equals(employeeId, that.employeeId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(employeeNumber, that.employeeNumber) && Objects.equals(transactionId, that.transactionId) && Objects.equals(timestamp, that.timestamp) && Objects.equals(vendor, that.vendor) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeId, firstName, lastName, employeeNumber, transactionId, timestamp, vendor, amount);
    }

    @Override
    public String toString() {
        return "EmployeeTransactionJoin{" +
                "id=" + id +
                ", employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
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
