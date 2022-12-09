package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.repository.ItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class ItemServiceImpl implements ItemService {

    private final Logger LOG = LogManager.getLogger(ItemServiceImpl.class);

    private final Integer FIRST_ID_OF_DEMO_ITEMS = 1;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public boolean addItem(Item item) {
        LOG.info("S--> ItemServiceImpl: Adding item " + item);
        return itemRepository.append(item);
    }

    @Override
    public Set<Item> getItems() {
        return itemRepository.findAll();
    }

    @Override
    public Set<Item> getItemsForDemo() {
        Set<Item> allItems = itemRepository.findAll();
        Set<Item> demoItems = new TreeSet<>();
        Integer firstIdOfDemoItems = FIRST_ID_OF_DEMO_ITEMS;
        Integer idOfDemoItem = firstIdOfDemoItems;
        for (Item item : allItems) {
            if (idOfDemoItem.equals(item.getId())) {
                demoItems.add(item);
                idOfDemoItem++;
            } else {
                return demoItems;
            }
        }
        LOG.info("S--> ItemServiceImpl: Items for demo " + demoItems);
        return demoItems;
    }

    @Override
    public void simulateProcessing(int seconds) {
        try {
            for (int i = 0; i < seconds; i++) {
                System.out.println((seconds - i) + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}