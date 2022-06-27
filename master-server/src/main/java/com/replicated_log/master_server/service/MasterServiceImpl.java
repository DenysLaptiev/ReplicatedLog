package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterServiceImpl implements MasterService {

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
