package com.replicated_log.secondary_server_1.repository;

import com.replicated_log.secondary_server_1.model.Item;

import java.util.List;

public interface ItemStorage {

    boolean append(Item item);

    List<Item> findAll();

    List<Item> getStorage();
}
