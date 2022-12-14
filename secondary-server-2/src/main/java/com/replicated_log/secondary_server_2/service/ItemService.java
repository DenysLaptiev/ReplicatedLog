package com.replicated_log.secondary_server_2.service;

import com.replicated_log.secondary_server_2.model.Item;

import java.util.Set;

public interface ItemService {

    boolean addItem(Item item);

    Set<Item> getItems();

    Set<Item> getItemsForDemo();

    void simulateProcessing(int seconds);
}
