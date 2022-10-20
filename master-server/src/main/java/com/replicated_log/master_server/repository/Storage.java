package com.replicated_log.master_server.repository;

import java.util.List;

public interface Storage<T> {
    boolean append(T item);
    List<T> findAll();
    List<T> getStorage();
}