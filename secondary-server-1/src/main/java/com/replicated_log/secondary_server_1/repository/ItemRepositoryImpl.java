package com.replicated_log.secondary_server_1.repository;

import com.replicated_log.secondary_server_1.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private Set<Item> items;

    @Override
    public boolean append(Item item) {
        boolean result = getStorage().add(item);
        if (result) {
            log.info("R--> ItemRepositoryImpl: Added item to storage " + item);
        }
        return result;
    }

    @Override
    public Set<Item> getStorage() {
        if (items == null) {
            items = new TreeSet<>();
        }
        return items;
    }
}
