package com.replicated_log.master_server.repository.impl;

import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.ItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final Logger LOG = LogManager.getLogger(ItemRepositoryImpl.class);

    private static Integer ITEM_ID = 0;

    private Set<Item> items;

    @Override
    public Item append(Item item) {

        Integer id = ++ITEM_ID;

        //TODO:for testing of items order
//        if(id.equals(3)){
//            id = 4;
//        }else if (id.equals(4)){
//            id = 3;
//        }

        item.setId(id);
        getStorage().add(item);
        LOG.info("R--> ItemRepositoryImpl: Added item to storage " + item);
        return item;
    }

    @Override
    public Set<Item> findAll() {
        return getStorage();
    }

    @Override
    public Item getItemById(Integer itemId) {
        Set<Item> allItems = getStorage();
        for (Item item : allItems) {
            if(item.getId().equals(itemId)){
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