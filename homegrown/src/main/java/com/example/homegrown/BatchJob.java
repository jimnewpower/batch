package com.example.homegrown;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A batch job that will read a file and batch process the data.
 */
class BatchJob<T> implements Runnable {

    private final ExecutorService executorService;

    private final int maxTasksToQueue;
    private final ResourceReader<T> resourceReader;
    private final PrintStream printStream;
    
    BatchJob(ExecutorService executorService, int maxTasksToQueue, ResourceReader<T> resourceReader, PrintStream printStream) {
        this.executorService = Objects.requireNonNull(executorService, "executor service");
        this.maxTasksToQueue = Math.max(1, maxTasksToQueue);
        this.resourceReader = Objects.requireNonNull(resourceReader, "resource reader");
        this.printStream = Objects.requireNonNull(printStream, "print stream");
    }

    @Override
    public void run() {
        printStream.println();
        printStream.println("Processing batch job...");
        printStream.println();

        Set<Future<?>> futures = new HashSet<>();

        // create the batcher, including the consumer that will process the data
        Batcher<T> batcher = new Batcher<>(maxTasksToQueue, data -> {
            // process the chunk of data for a single batch job
            List<T> content = new ArrayList<>(data);
//            batchTask(content).run();
            futures.add(executorService.submit(batchTask(content)));
        });

        // read the data and submit to the batcher
        Collection<T> data = resourceReader.read();
        data.forEach(batcher::submit);

        // start timer and wait for batcher to finish
        long start = System.currentTimeMillis();

        while (!batcher.isFinished()) {
            ;
        }

        // wait for all tasks to finish
        futures.forEach(future -> {
            try {
                while (!future.isDone()) {
                    System.out.println("Waiting for task to complete...");
                    future.get(1, TimeUnit.SECONDS); // Wait for 1 second before checking again
                }
                System.out.println("Task is done: " + future.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        printStream.println("Elapsed time for batch job: " + (System.currentTimeMillis() - start));

        System.out.println("Done");
    }

    private Runnable batchTask(List<T> data) {
        return () -> {
            // process the chunk of data for a single batch job
            printStream.println("** Batch Operation " + UUID.randomUUID());
            data.forEach(printStream::println);
        };
    }
}
