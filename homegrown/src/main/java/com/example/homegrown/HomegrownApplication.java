package com.example.homegrown;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SpringBootApplication
public class HomegrownApplication implements CommandLineRunner {
	private static final String TRANSACTIONS_FILENAME = "mock_transactions.csv";

	public static void main(String[] args) {
		SpringApplication.run(HomegrownApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		Database database = Database.create("employees_db.csv", "transactions_db.csv");

		Function<String, UserTransaction> mapper = (String line) -> {
			String[] fields = line.split(",");
			return new UserTransaction(
				fields[TransactionFields.USER_ID.ordinal()],
				fields[TransactionFields.TRANSACTION_ID.ordinal()]
			);
		};

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		BatchProcess<String, UserTransaction> batchProcess = new BatchProcess.Builder()
			.database(database)
			.mapper(mapper)
			.executorService(executorService)
			.maxTasksToQueue(100)
			.resourceReader(new ResourceReaderText(TRANSACTIONS_FILENAME))
			.build();

		batchProcess.run();
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
