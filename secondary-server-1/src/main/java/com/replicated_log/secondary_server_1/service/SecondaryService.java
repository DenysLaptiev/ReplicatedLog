package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Item;

import java.util.List;

public interface SecondaryService {
    boolean addItem(Item item);
    List<Item> getItems();
    void simulateProcessing(int seconds);
}
