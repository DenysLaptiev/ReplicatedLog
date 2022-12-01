package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.item.Item;
import com.replicated_log.master_server.service.MasterClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Component
public class BrokerStorageImpl implements BrokerStorage {

    private Map<String, Set<Item>> brokerStorage;

    @Override
    public String addSecondaryServerToBrokerMap(String secondaryServerAddress) {
        Map<String, Set<Item>> brokerStorage = getBrokerStorage();
        if (!brokerStorage.containsKey(secondaryServerAddress)) {
            brokerStorage.put(secondaryServerAddress, new TreeSet<>());
        }
        return secondaryServerAddress;
    }

    @Override
    public void addItemToBrokerMap(Item item) {
        Map<String, Set<Item>> brokerStorage = getBrokerStorage();
        for (String secondaryServerAddress : brokerStorage.keySet()) {
            Set<Item> items = brokerStorage.get(secondaryServerAddress);
            items.add(item);
            brokerStorage.put(secondaryServerAddress,items);
        }
    }

    @Override
    public void removeItemFromBrokerMap(Item item, String secondaryServerAddress) {
        Map<String, Set<Item>> brokerStorage = getBrokerStorage();
        for (String address : brokerStorage.keySet()) {
            if(address.equals(secondaryServerAddress)){
                brokerStorage.get(address).remove(item);
            }
        }
    }

    @Override
    public Set<Item> addNotSentItemsToBrokerMap(String secondaryServerAddress, Set<Item> notSentItems) {
        Map<String, Set<Item>> brokerStorage = getBrokerStorage();
        Set<Item> items;
        if (brokerStorage.containsKey(secondaryServerAddress)) {
            items = brokerStorage.get(secondaryServerAddress);
        } else {
            items = new TreeSet<>();
        }
        items.addAll(notSentItems);
        brokerStorage.put(secondaryServerAddress, items);
        return items;
    }


    @Override
    public Map<String, Set<Item>> getBrokerStorage() {
        if (brokerStorage == null) {
            brokerStorage = new LinkedHashMap<>();
        }
        return brokerStorage;
    }

}
