package com.replicated_log.secondary_server_2.repository;

import com.replicated_log.secondary_server_2.model.Item;

import java.util.Set;

public interface ItemRepository {

    boolean append(Item item);

    Set<Item> findAll();

    Set<Item> getStorage();
}
