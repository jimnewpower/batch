package com.example.easy;

import org.jeasy.batch.core.filter.HeaderRecordFilter;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.listener.BatchListener;
import org.jeasy.batch.core.reader.FileRecordReader;
import org.jeasy.batch.core.reader.RecordReader;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class EasyApplication implements CommandLineRunner {
    private static final String TRANSACTIONS_FILENAME = "mock_transactions.csv";

    public static void main(String[] args) {
        SpringApplication.run(EasyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Path dataSource = new TempFileCreator().createTempFileFromResource(TRANSACTIONS_FILENAME);

        Job job = new JobBuilder<String, String>()
                .named("Easy Batch Job")
                .reader(new FlatFileRecordReader(dataSource))
                .filter(new HeaderRecordFilter<>())
                .mapper(new DelimitedRecordMapper<>(UserTransaction.class, "userId", "transactionId"))
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        JobReport jobReport = jobExecutor.execute(job);
        jobExecutor.shutdown();

        System.out.println(jobReport);
    }
}
