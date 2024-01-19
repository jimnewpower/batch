package com.example.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("======== Batch job finished with status: " + jobExecution.getStatus());

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            List<EmployeeTransactionJoin> allTransactions = jdbcTemplate
                    .query("SELECT employee_id, first_name, last_name, employee_number, transaction_id, timestamp, vendor, amount FROM employee_transaction_join", new DataClassRowMapper<>(EmployeeTransactionJoin.class))
                    .stream()
                    .sorted(Comparator.comparing(EmployeeTransactionJoin::getEmployeeId).thenComparing(EmployeeTransactionJoin::getTimestamp))
                    .toList();

            log.info("======== Join Results:");
            allTransactions
                    .forEach(join -> log.info(String.format("%15s %15s %9s %20s %20s", join.getFirstName(), join.getLastName(), NumberFormat.getCurrencyInstance().format(Double.parseDouble(join.getAmount())), join.getVendor(), join.getTimestamp())));

            double total = allTransactions
                    .stream()
                    .flatMapToDouble(join -> DoubleStream.of(Double.parseDouble(join.getAmount())))
                    .sum();

            log.info("========");
            log.info("======== Batch status   : " + jobExecution.getStatus());
            log.info("======== Start time     : " + jobExecution.getStartTime());
            log.info("======== End time       : " + jobExecution.getEndTime());
            Duration duration = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime());
            log.info("======== Execution time : " + formatDuration(duration));
            log.info(String.format("========  Total Transactions: %s", NumberFormat.getInstance().format(allTransactions.size())));
            log.info(String.format("========  Total Spent: %s", NumberFormat.getCurrencyInstance().format(total)));
            log.info("========");
        }
    }

    private static String formatDuration(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}