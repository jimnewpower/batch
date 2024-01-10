package com.example.easy;

import org.jeasy.batch.core.processor.RecordProcessor;
import org.jeasy.batch.core.record.Header;
import org.jeasy.batch.core.record.Record;

public class UserTransactionProcessor implements RecordProcessor<String, UserTransaction> {
    @Override
    public Record<UserTransaction> processRecord(Record<String> record) throws Exception {
        Record<UserTransaction> userTransactionRecord = new Record<UserTransaction>() {
            @Override
            public Header getHeader() {
                return record.getHeader();
            }

            @Override
            public UserTransaction getPayload() {
                String[] tokens = record.getPayload().split(",");
                UserTransaction userTransaction = new UserTransaction(tokens[0], tokens[1]);
                return userTransaction;
            }
        };

        System.out.println(userTransactionRecord.getPayload().toString());

        return userTransactionRecord;
    }
}
