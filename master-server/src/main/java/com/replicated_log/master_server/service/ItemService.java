package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Item;

import java.util.Set;

public interface ItemService {

    Item addItem(Item item);

    Set<Item> getItems();

    Item getItemById(Integer itemId);
}
