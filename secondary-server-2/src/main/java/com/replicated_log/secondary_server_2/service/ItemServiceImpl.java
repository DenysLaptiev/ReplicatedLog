package com.replicated_log.secondary_server_2.service;

import com.replicated_log.secondary_server_2.model.Item;
import com.replicated_log.secondary_server_2.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Integer FIRST_ID_OF_DEMO_ITEMS = 1;

    private final ItemRepository itemRepository;

    @Override
    public boolean addItem(Item item) {
        return itemRepository.append(item);
    }

    @Override
    public Set<Item> getItems() {
        return itemRepository.getStorage();
    }

    @Override
    public Set<Item> getItemsForDemo() {
        Set<Item> allItems = itemRepository.getStorage();
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
        log.info("S--> ItemServiceImpl: Items for demo " + demoItems);
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
