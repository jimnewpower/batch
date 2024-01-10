package com.example.easy;

import java.util.Collection;

interface ResourceReader<T> {
    Collection<T> read();
}
