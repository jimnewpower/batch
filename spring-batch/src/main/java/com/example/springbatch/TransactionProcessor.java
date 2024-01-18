package com.example.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TransactionProcessor implements ItemProcessor<EmployeeTransaction, EmployeeTransactionJoin> {

    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public EmployeeTransactionJoin process(final EmployeeTransaction employeeTransaction) throws Exception {
        log.info("Processing (" + employeeTransaction + ")");

        String employeeId = employeeTransaction.employeeId();
        if (employeeId == null) {
            throw new IllegalArgumentException("employeeId is null");
        }

        log.info("employeeId: " + employeeId);

        Employee employee = getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("employee is null");
        }

        String transactionId = employeeTransaction.transactionId();
        if (transactionId == null) {
            throw new IllegalArgumentException("transactionId is null");
        }

        log.info("transactionId: " + transactionId);

        Transaction transaction = getTransaction(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("transaction is null");
        }

        final EmployeeTransactionJoin userTransactionJoin = new EmployeeTransactionJoin(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmployeeNumber(),
                transaction.getTransactionId(),
                transaction.getTimestamp(),
                transaction.getVendor(),
                transaction.getAmount()
        );

        log.info("Converting (" + employeeTransaction + ") into (" + userTransactionJoin + ")");

        return userTransactionJoin;
    }

    private Employee getEmployee(String employeeId) {
        final String sqlQuery = "SELECT id, employee_id, first_name, last_name, employee_number FROM employee WHERE employee_id = ?";
        log.info("sqlQuery: " + sqlQuery);
        log.info("employeeId: " + employeeId);
        return jdbcTemplate.queryForObject(
                sqlQuery,
                new Object[]{employeeId},
                new EmployeeRowMapper()
        );
    }

    private Transaction getTransaction(String transactionId) {
        final String sqlQuery = "SELECT id, transaction_id, timestamp, vendor, amount FROM transaction WHERE transaction_id = ?";
        log.info("sqlQuery: " + sqlQuery);
        log.info("transactionId: " + transactionId);
        return jdbcTemplate.queryForObject(
                sqlQuery,
                new Object[]{transactionId},
                new TransactionRowMapper()
        );
    }
}