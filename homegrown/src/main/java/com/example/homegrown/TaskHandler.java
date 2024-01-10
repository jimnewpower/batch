package com.example.homegrown;

public interface TaskHandler<T> {
    void submit(T task);
    boolean isFinished();
}
