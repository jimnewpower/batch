package com.example.homegrown;

import java.io.PrintStream;
import java.util.*;

/**
 * A batch job that will read a file and batch process the data.
 */
class BatchJob<T> implements Runnable {

    private final int maxTasksToQueue;
    private final ResourceReader<T> resourceReader;
    private final PrintStream printStream;
    
    BatchJob(int maxTasksToQueue, ResourceReader<T> resourceReader, PrintStream printStream) {
        this.maxTasksToQueue = Math.max(1, maxTasksToQueue);
        this.resourceReader = Objects.requireNonNull(resourceReader, "resource reader");
        this.printStream = Objects.requireNonNull(printStream, "print stream");
    }

    @Override
    public void run() {
        printStream.println();
        printStream.println("Processing batch job...");
        printStream.println();

        // create the batcher, including the consumer that will process the data
        Batcher<T> batcher = new Batcher<>(maxTasksToQueue, data -> {
            // process the chunk of data for a single batch job
            List<T> content = new ArrayList<>(data);
            printStream.println("** Batch Operation " + UUID.randomUUID());
            content.forEach(printStream::println);
        });

        // read the data and submit to the batcher
        Collection<T> data = resourceReader.read();
        data.forEach(batcher::submit);

        // start timer and wait for batcher to finish
        long start = System.currentTimeMillis();

        while (!batcher.isFinished()) {
            ;
        }

        printStream.println("Elapsed time for batch job: " + (System.currentTimeMillis() - start));
    }
}
