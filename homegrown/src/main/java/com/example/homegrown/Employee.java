package com.example.homegrown;

import java.util.Objects;
import java.util.UUID;

public class Employee {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String employeeNumber;

    public Employee() {

    }

    public Employee(String csv) {
        Objects.requireNonNull(csv, "csv");
        String[] fields = csv.split(",");
        this.uuid = UUID.fromString(fields[0]);
        this.firstName = fields[1];
        this.lastName = fields[2];
        this.employeeNumber = fields[3];
    }

    public Employee(UUID uuid, String firstName, String lastName, String employeeNumber) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(uuid, employee.uuid) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(employeeNumber, employee.employeeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, firstName, lastName, employeeNumber);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
}
