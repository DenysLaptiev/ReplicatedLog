package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Item;

import java.util.List;

public interface MasterService {
    boolean addItem(Item item);
    List<Item> getItems();
}
