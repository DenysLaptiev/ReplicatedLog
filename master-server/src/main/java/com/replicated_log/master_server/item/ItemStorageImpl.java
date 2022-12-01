package com.replicated_log.master_server.item;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
public class ItemStorageImpl implements ItemStorage<Item> {

    private static Integer ITEM_ID = 0;

    private Set<Item> items;

    @Override
    public Item append(Item item) {
        item.setId(++ITEM_ID);
        getStorage().add(item);
        return item;
    }

    @Override
    public Set<Item> findAll() {
        return getStorage();
    }

    @Override
    public Set<Item> getStorage() {
        if (items == null) {
            items = new TreeSet<>();
        }
        return items;
    }
}
