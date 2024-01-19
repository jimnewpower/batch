package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<EmployeeTransaction> reader() {
        return new FlatFileItemReaderBuilder<EmployeeTransaction>()
                .name("employeeTransactionItemReader")
                .resource(new ClassPathResource("mock_transactions.csv"))
                .delimited()
                .names("employeeId","transactionId")
                .targetType(EmployeeTransaction.class)
                .build();
    }

    @Bean
    public ItemProcessor<EmployeeTransaction, EmployeeTransactionJoin> processor() {
        return new TransactionProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<EmployeeTransactionJoin> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<EmployeeTransactionJoin>()
                .sql("INSERT INTO employee_transaction_join (employee_id, first_name, last_name, employee_number, transaction_id, timestamp, vendor, amount) VALUES (:employeeId, :firstName, :lastName, :employeeNumber, :transactionId, :timestamp, :vendor, :amount)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importEmployeeTransactionJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importEmployeeTransactionJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<EmployeeTransaction> reader,
                      ItemProcessor<EmployeeTransaction, EmployeeTransactionJoin> processor,
                      JdbcBatchItemWriter<EmployeeTransactionJoin> writer) {
        return new StepBuilder("step1", jobRepository)
                .<EmployeeTransaction, EmployeeTransactionJoin> chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]

}