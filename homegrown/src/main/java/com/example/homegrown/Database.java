package com.example.homegrown;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Database {

    public static Database create(
        String employeesDBResource,
        String transactionsDBResource
    ) throws IOException {
        return new Database(
            loadEmployeesDB(employeesDBResource),
            loadTransactionsDB(transactionsDBResource)
        );
    }

    private EmployeesDB employeesDatabase;
    private TransactionsDB transactionsDatabase;

    private Database(
        EmployeesDB employeesDB,
        TransactionsDB transactionsDB
    ) {
        this.employeesDatabase = employeesDB;
        this.transactionsDatabase = transactionsDB;
    }

    public Employee getEmployee(UUID uuid) {
        return employeesDatabase.get(uuid);
    }

    public Transaction getTransaction(UUID uuid) {
        return transactionsDatabase.get(uuid);
    }

    private static TransactionsDB loadTransactionsDB(String transactionsDBFilename) throws IOException {
        Path transactionsDBSource = new TempFileCreator().createTempFileFromResource(transactionsDBFilename);
        List<String> transactionsCSV = Files.readAllLines(transactionsDBSource);
        Map<UUID, Transaction> transactions = new HashMap<>();
        for (String csv : transactionsCSV) {
            // skip header row
            if (csv.startsWith("UUID")) {
                continue;
            }
            Transaction transaction = new Transaction(csv);
            System.out.println(transaction);
            transactions.put(transaction.getUuid(), transaction);
        }

        return new TransactionsDB(transactions);
    }

    private static EmployeesDB loadEmployeesDB(String employeesDBFilename) throws IOException {
        Path employeesSource = new TempFileCreator().createTempFileFromResource(employeesDBFilename);
        List<String> employeesCSV = Files.readAllLines(employeesSource);
        Map<UUID, Employee> employees = new HashMap<>();
        for (String csv : employeesCSV) {
            // skip header row
            if (csv.startsWith("UUID")) {
                continue;
            }
            Employee employee = new Employee(csv);
            System.out.println(employee);
            employees.put(employee.getUuid(), employee);
        }

        return new EmployeesDB(employees);
    }

    private static class EmployeesDB implements Queryable<Employee> {
        private final Map<UUID, Employee> employees;

        public EmployeesDB(Map<UUID, Employee> employees) {
            this.employees = Objects.requireNonNull(employees, "employees");
        }

        @Override
        public Employee get(UUID key) {
            return employees.get(key);
        }
    }

    private static class TransactionsDB implements Queryable<Transaction> {
        private final Map<UUID, Transaction> transactions;

        public TransactionsDB(Map<UUID, Transaction> transactions) {
            this.transactions = Objects.requireNonNull(transactions, "transactions");
        }

        @Override
        public Transaction get(UUID key) {
            return transactions.get(key);
        }
    }
}
