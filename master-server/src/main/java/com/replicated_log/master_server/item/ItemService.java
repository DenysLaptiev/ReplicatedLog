package com.replicated_log.master_server.item;

import java.util.Set;

public interface ItemService {

    Item addItem(Item item);
    Set<Item> getItems();
}
