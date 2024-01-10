package com.example.homegrown;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BatchJob implements Runnable {

    private String resourceName;

    public BatchJob(String resourceName) {
        this.resourceName = Objects.requireNonNull(resourceName, "resource name");
    }

    @Override
    public void run() {
        System.out.println();
        System.out.println("Processing batch job for input: " + resourceName);
        System.out.println();

        Batcher batcher = new Batcher<String>(10, strings -> {
            List<String> content = new ArrayList<>(strings);
            System.out.println("******* Batch Operation *******");
            content.stream().filter(s -> !s.startsWith(TransactionFields.USER_ID.toString())).forEach(System.out::println);
        });

        List<String> lines = readLines();
        lines.stream().forEach(batcher::submit);

        long start = System.currentTimeMillis();

        while (!batcher.isFinished()) {
            ;
        }

        System.out.println("Elapsed time for batch job: " + (System.currentTimeMillis() - start));
    }

    private List<String> readLines() {
        List<String> lines = new ArrayList<>();
        Resource resource = new ClassPathResource(resourceName);
        try (
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
