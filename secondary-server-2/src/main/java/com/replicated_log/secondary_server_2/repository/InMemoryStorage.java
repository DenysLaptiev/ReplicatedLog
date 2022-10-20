package com.replicated_log.secondary_server_2.repository;

import java.util.List;

public interface InMemoryStorage<T> {
    boolean append(T item);
    List<T> findAll();
}
