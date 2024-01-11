package com.example.homegrown;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A batch job that will read a file and batch process the data.
 */
class BatchProcess<I, O> implements Runnable {

    public static class Builder<I, O> {
        private Database database;
        private Function<I, O> mapper;
        private ExecutorService executorService;

        private int maxTasksToQueue;
        private ResourceReader<I> resourceReader;
        private PrintStream printStream;

        public Builder() {
            this.maxTasksToQueue = 100;
            this.printStream = System.out;
        }

        public BatchProcess<I, O> build() {
            return new BatchProcess<>(database, mapper, executorService, maxTasksToQueue, resourceReader, printStream);
        }

        public Builder<I, O> database(Database database) {
            this.database = database;
            return this;
        }

        public Builder<I, O> mapper(Function<I, O> mapper) {
            this.mapper = mapper;
            return this;
        }

        public Builder<I, O> executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder<I, O> maxTasksToQueue(int maxTasksToQueue) {
            this.maxTasksToQueue = maxTasksToQueue;
            return this;
        }

        public Builder<I, O> resourceReader(ResourceReader<I> resourceReader) {
            this.resourceReader = resourceReader;
            return this;
        }

        public Builder<I, O> printStream(PrintStream printStream) {
            this.printStream = printStream;
            return this;
        }
    }

    private final Database database;
    private final Function<I, O> mapper;
    private final ExecutorService executorService;

    private final int maxTasksToQueue;
    private final ResourceReader<I> resourceReader;
    private final PrintStream printStream;
    
    BatchProcess(Database database, Function<I, O> mapper, ExecutorService executorService, int maxTasksToQueue, ResourceReader<I> resourceReader, PrintStream printStream) {
        this.database = Objects.requireNonNull(database, "database");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
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
        Batcher<O> batcher = new Batcher<>(maxTasksToQueue, data -> {
            // process the chunk of data for a single batch job
            List<O> content = new ArrayList<>(data);
            futures.add(executorService.submit(batchTask(content)));
        });

        // read the data, map it, and submit to the batcher for processing
        resourceReader.read().stream().map(mapper).sorted().forEach(batcher::submit);

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

    private Runnable batchTask(List<O> data) {
        return () -> {
            // process the chunk of data for a single batch job
            printStream.println("** Batch Operation " + UUID.randomUUID());
            Function<O, String> function = (O o) -> {
                if (o instanceof UserTransaction userTransaction) {
                    Employee employee = database.getEmployee(UUID.fromString(userTransaction.getUserId()));
                    Transaction transaction = database.getTransaction(UUID.fromString(userTransaction.getTransactionId()));
                    return transaction.getTimestamp() + " "  +
                            employee.getFirstName() + " " +
                            employee.getLastName() + "   " +
                            transaction.getVendor() + "   $" + transaction.getAmount();
                } else {
                    return o.toString();
                }
            };
            data.stream().map(function).forEach(printStream::println);
        };
    }
}
