package com.example.homegrown;

import java.util.Collection;

public interface ResourceReader<T> {
    Collection<T> read();
}
