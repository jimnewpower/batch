package com.example.homegrown;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class HomegrownApplication implements CommandLineRunner {
	private static final String TRANSACTIONS_FILENAME = "mock_transactions.csv";

	public static void main(String[] args) {
		SpringApplication.run(HomegrownApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		ResourceReader<String> resourceReader = new ResourceReaderText(TRANSACTIONS_FILENAME);
		final int maxTasksToQueue = 100;
		BatchJob<String> batchJob = new BatchJob<>(executorService, maxTasksToQueue, resourceReader, System.out);
		batchJob.run();
		cleanup(executorService);
	}

	private void cleanup(ExecutorService executorService) {
		executorService.shutdown();
		try {
			Thread.sleep(1000);
			if (!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}

}
