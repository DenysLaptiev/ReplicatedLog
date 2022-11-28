package com.replicated_log.master_server.repository;

import java.util.Set;

public interface Storage<T> {

    T append(T item);
    Set<T> findAll();
    Set<T> getStorage();
}