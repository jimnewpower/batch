package com.example.homegrown;

import java.util.UUID;

public interface Queryable<T> {
    T get(UUID key);
}
