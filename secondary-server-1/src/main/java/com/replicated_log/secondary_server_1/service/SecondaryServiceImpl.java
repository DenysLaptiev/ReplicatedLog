package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.repository.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondaryServiceImpl implements SecondaryService {

    @Autowired
    private InMemoryStorage<Item> inMemoryStorage;

    @Override
    public boolean addItem(Item item) {
        return inMemoryStorage.append(item);
    }

    @Override
    public List<Item> getItems() {
        return inMemoryStorage.findAll();
    }
}
