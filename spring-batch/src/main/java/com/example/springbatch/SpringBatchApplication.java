package com.example.springbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SpringBatchApplication implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
//        System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchApplication.class, args)));
    }

    @Override
    public void run(String... args) throws Exception {
//        jdbcTemplate.execute("SELECT * FROM employee LIMIT 0;");
    }
}
