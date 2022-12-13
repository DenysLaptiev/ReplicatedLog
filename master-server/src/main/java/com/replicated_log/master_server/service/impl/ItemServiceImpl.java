package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.ItemRepository;
import com.replicated_log.master_server.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {

    private final Logger LOG = LogManager.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item addItem(Item item) {
        LOG.info("S--> ItemServiceImpl: Adding item " + item);
        return itemRepository.append(item);
    }

    @Override
    public Set<Item> getItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemRepository.getItemById(itemId);
    }
}
