package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.ItemRepository;
import com.replicated_log.master_server.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item addItem(Item item) {
        log.info("S--> ItemServiceImpl: Adding item " + item);
        return itemRepository.append(item);
    }

    @Override
    public Set<Item> getItems() {
        return itemRepository.getStorage();
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemRepository.getItemById(itemId);
    }
}
