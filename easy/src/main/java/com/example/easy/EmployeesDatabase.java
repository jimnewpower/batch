package com.example.easy;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EmployeesDatabase implements Queryable<Employee> {
    private final Map<UUID, Employee> employees;

    public EmployeesDatabase(Map<UUID, Employee> employees) {
        this.employees = Objects.requireNonNull(employees, "employees");
    }

    @Override
    public Employee get(UUID key) {
        return employees.get(key);
    }
}
