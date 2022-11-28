package com.replicated_log.secondary_server_1.repository;

import com.replicated_log.secondary_server_1.model.Item;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
public class ItemStorageImpl implements ItemStorage {

    private Set<Item> items;

    @Override
    public boolean append(Item item) {
        return getStorage().add(item);
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
