package com.example.homegrown;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomegrownApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HomegrownApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ResourceReaderText resourceReaderText = new ResourceReaderText("mock_transactions.csv");
		final int maxTasksToQueue = 5;
		BatchJob<String> batchJob = new BatchJob<>(maxTasksToQueue, resourceReaderText, System.out);
		batchJob.run();
	}
}
