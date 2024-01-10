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
		BatchJob batchJob = new BatchJob("mock_transactions.csv");
		batchJob.run();
	}
}
