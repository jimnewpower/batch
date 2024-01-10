package com.example.homegrown;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomegrownApplication implements CommandLineRunner {
	private static final String TRANSACTIONS_FILENAME = "mock_transactions.csv";

	public static void main(String[] args) {
		SpringApplication.run(HomegrownApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ResourceReader<String> resourceReader = new ResourceReaderText(TRANSACTIONS_FILENAME);
		final int maxTasksToQueue = 100;
		BatchJob<String> batchJob = new BatchJob<>(maxTasksToQueue, resourceReader, System.out);
		batchJob.run();
	}
}
