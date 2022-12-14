package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Item;

import java.util.Set;

public interface ItemRepository {

    Item append(Item item);

    Item getItemById(Integer itemId);

    Set<Item> getStorage();
}
