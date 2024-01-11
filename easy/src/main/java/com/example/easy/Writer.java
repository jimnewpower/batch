package com.example.easy;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.writer.RecordWriter;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Writer implements RecordWriter<UserTransaction> {

    private final Queryable<Employee> employeesDatabase;
    private final Queryable<Transaction> transactionsDatabase;
    private final PrintStream printStream;
    
    public Writer(Queryable<Employee> employeesDatabase, Queryable<Transaction> transactionsDatabase, PrintStream printStream) {
        this.employeesDatabase = Objects.requireNonNull(employeesDatabase, "employees database");
        this.transactionsDatabase = Objects.requireNonNull(transactionsDatabase, "transactions database");
        this.printStream = Objects.requireNonNull(printStream, "print stream");
    }
    
    @Override
    public void open() throws Exception {
        printStream.println("Opening writer");
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        printStream.println("Writing records:");
        Stream<Record<UserTransaction>> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(batch.iterator(), Spliterator.ORDERED), false);
        List<UserTransaction> userTransactions = stream.map(Record::getPayload).toList();
        for (UserTransaction userTransaction : userTransactions) {
            Employee employee = employeesDatabase.get(UUID.fromString(userTransaction.getUserId()));
            Transaction transaction = transactionsDatabase.get(UUID.fromString(userTransaction.getTransactionId()));
            printStream.println(
                    transaction.getTimestamp() + " "  +
                    employee.getFirstName() + " " +
                    employee.getLastName() + "   " +
                    transaction.getVendor() + "   $" + transaction.getAmount()
            );
        }
    }

    @Override
    public void close() throws Exception {
        printStream.println("Closing writer");
    }

}
