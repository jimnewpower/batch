package com.example.easy;

import org.jeasy.batch.core.filter.HeaderRecordFilter;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class EasyApplication implements CommandLineRunner {
    private static final String EMPLOYEES_FILENAME = "employees_db.csv";
    private static final String TRANSACTIONS_DB_FILENAME = "transactions_db.csv";
    private static final String TRANSACTIONS_FILENAME = "mock_transactions.csv";

    public static void main(String[] args) {
        SpringApplication.run(EasyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new DataHandler().translateCSVToSQLInsertText(EMPLOYEES_FILENAME, "employee", "(employee_id, first_name, last_name, employee_number)");
        new DataHandler().translateCSVToSQLInsertText(TRANSACTIONS_DB_FILENAME, "transaction", "(transaction_id, timestamp, vendor, amount)");
        new DataHandler().translateCSVToSQLInsertText(TRANSACTIONS_FILENAME, "employee_transaction", "(employee_id, transaction_id)");

        TransactionsDatabase transactionsDatabase = loadTransactionsDB();
        EmployeesDatabase employeesDatabase = loadEmployeesDB();

        Path dataSource = new TempFileCreator().createTempFileFromResource(TRANSACTIONS_FILENAME);

        final PrintStream printStream = System.out;

        Job job = new JobBuilder<String, UserTransaction>()
                .named("Easy Batch Job")
                .reader(new FlatFileRecordReader(dataSource))
                .filter(new HeaderRecordFilter<>())
                .mapper(new DelimitedRecordMapper<>(UserTransaction.class, "userId", "transactionId"))
                .writer(new Writer(employeesDatabase, transactionsDatabase, printStream))
                .build();

        try (JobExecutor jobExecutor = new JobExecutor()) {
            JobReport jobReport = jobExecutor.execute(job);
            printStream.println(jobReport);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            printStream.println("Job finished");
        }
    }

    private TransactionsDatabase loadTransactionsDB() throws IOException {
        Path transactionsDBSource = new TempFileCreator().createTempFileFromResource(TRANSACTIONS_DB_FILENAME);
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

        return new TransactionsDatabase(transactions);
    }

    private EmployeesDatabase loadEmployeesDB() throws IOException {
        Path employeesSource = new TempFileCreator().createTempFileFromResource(EMPLOYEES_FILENAME);
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

        return new EmployeesDatabase(employees);
    }
}
