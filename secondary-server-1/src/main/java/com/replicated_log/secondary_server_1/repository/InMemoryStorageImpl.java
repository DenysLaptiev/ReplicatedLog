package com.replicated_log.secondary_server_1.repository;

import com.replicated_log.secondary_server_1.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryStorageImpl implements InMemoryStorage<Item> {

    private List<Item> items;

    @Override
    public boolean append(Item item) {
        return getStorage().add(item);
    }

    @Override
    public List<Item> findAll() {
        return getStorage();
    }

    public List<Item> getStorage() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}
