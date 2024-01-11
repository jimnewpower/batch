package com.example.easy;

import java.util.UUID;

public interface Queryable<T> {
    T get(UUID key);
}
