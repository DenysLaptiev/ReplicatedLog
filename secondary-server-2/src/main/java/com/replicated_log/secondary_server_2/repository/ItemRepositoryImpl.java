package com.replicated_log.secondary_server_2.repository;

import com.replicated_log.secondary_server_2.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final Logger LOG = LogManager.getLogger(ItemRepositoryImpl.class);

    private Set<Item> items;

    @Override
    public boolean append(Item item) {
        boolean result = getStorage().add(item);
        if (result) {
            LOG.info("R--> ItemRepositoryImpl: Added item to storage " + item);
        }
        return result;
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
