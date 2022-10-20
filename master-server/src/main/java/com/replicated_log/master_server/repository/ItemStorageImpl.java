package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemStorageImpl implements ItemStorage<Item> {

    private List<Item> items;

    @Override
    public boolean append(Item item) {
        return getStorage().add(item);
    }

    @Override
    public List<Item> findAll() {
        return getStorage();
    }

    @Override
    public List<Item> getStorage() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}
