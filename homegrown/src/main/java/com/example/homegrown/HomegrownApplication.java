package com.example.homegrown;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class HomegrownApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HomegrownApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ResourceReader<String> resourceReader = new ResourceReaderText("mock_transactions.csv");
		final int maxTasksToQueue = 50;
		BatchJob<String> batchJob = new BatchJob<>(maxTasksToQueue, resourceReader, System.out);
		batchJob.run();
	}
}
