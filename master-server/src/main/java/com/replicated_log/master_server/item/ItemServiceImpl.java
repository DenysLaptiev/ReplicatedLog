package com.replicated_log.master_server.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {

    private final Logger LOG = LogManager.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemStorage<Item> itemStorage;


    @Override
    public Item addItem(Item item) {
        return itemStorage.append(item);
    }

    @Override
    public Set<Item> getItems() {
        return itemStorage.findAll();
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemStorage.getItemById(itemId);
    }
}
