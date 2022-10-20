package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.repository.ItemStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondaryServiceImpl implements SecondaryService {

    @Autowired
    private ItemStorage itemStorage;

    @Override
    public boolean addItem(Item item) {
        return itemStorage.append(item);
    }

    @Override
    public List<Item> getItems() {
        return itemStorage.findAll();
    }

    @Override
    public void simulateProcessing(int seconds) {
        try {
            for (int i = 0; i < seconds; i++) {
                System.out.println((seconds - i)+ "...");
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
