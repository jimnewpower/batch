package com.example.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class TransactionRowMapper implements RowMapper<Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionRowMapper.class);

    @Override
    public Transaction mapRow(java.sql.ResultSet resultSet, int rowNum) throws java.sql.SQLException {
        int id = resultSet.getInt("id");
        String transactionId = resultSet.getString("transaction_id");
        String timestamp = resultSet.getString("timestamp");
        String vendor = resultSet.getString("vendor");
        String amount = resultSet.getString("amount");

        log.info("id: " + id);
        log.info("transactionId: " + transactionId);
        log.info("timestamp: " + timestamp);
        log.info("vendor: " + vendor);
        log.info("amount: " + amount);

        return new Transaction(id, transactionId, timestamp, vendor, amount);
    }
}
