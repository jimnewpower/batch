package com.example.homegrown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

class Batcher<T> implements TaskHandler<T> {
    private BlockingQueue<T> taskQueue = new LinkedBlockingQueue<>();
    private int maxTasksToQueue;
    private Thread workerThread;
    private boolean isRunning = false;

    public Batcher(int maxTasksToQueue, Consumer<List<T>> taskConsumer) {
        this.maxTasksToQueue = maxTasksToQueue;
        this.workerThread = new Thread(batchHandler(taskConsumer));
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    private Runnable batchHandler(Consumer<List<T>> taskConsumer) {
        return () -> {
            while (!workerThread.isInterrupted()) {
                List<T> tasks = new ArrayList<>(maxTasksToQueue);
                while (taskQueue.drainTo(tasks, maxTasksToQueue) == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                isRunning = true;
                taskConsumer.accept(tasks);
                isRunning = false;
            }
        };
    }

    @Override
    public void submit(T task) {
        taskQueue.add(task);
    }

    @Override
    public boolean isFinished() {
        return taskQueue.isEmpty() && !isRunning;
    }
}