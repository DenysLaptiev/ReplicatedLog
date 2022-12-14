package com.replicated_log.master_server.repository.impl;

import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private static Integer ITEM_ID = 0;

    private Set<Item> items;

    @Override
    public Item append(Item item) {

        Integer id = ++ITEM_ID;

        //this lines if uncommented can be used for testing of items order
        /*
        if(id.equals(3)){
            id = 4;
        }else if (id.equals(4)){
            id = 3;
        }
        */

        item.setId(id);
        getStorage().add(item);
        log.info("R--> ItemRepositoryImpl: Added item to storage " + item);
        return item;
    }

    @Override
    public Item getItemById(Integer itemId) {
        Set<Item> allItems = getStorage();
        for (Item item : allItems) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Set<Item> getStorage() {
        if (items == null) {
            items = new TreeSet<>();
        }
        return items;
    }
}
