package com.replicated_log.master_server.item;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
public class ItemStorageImpl implements ItemStorage<Item> {

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
        return item;
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
}
