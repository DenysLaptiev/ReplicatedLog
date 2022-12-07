package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.item.Item;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BrokerRepositoryImpl implements BrokerRepository {

    private Map<Address, Set<Item>> brokerStorage;

    @Override
    public Address addSecondaryServerToBrokerMap(Address secondaryServerAddress) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
        if (!brokerStorage.containsKey(secondaryServerAddress)) {
            brokerStorage.put(secondaryServerAddress, new TreeSet<>());
        }
        return secondaryServerAddress;
    }

    @Override
    public void addItemToBrokerMap(Item item) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
        for (Address secondaryServerAddress : brokerStorage.keySet()) {
            Set<Item> items = brokerStorage.get(secondaryServerAddress);
            items.add(item);
            brokerStorage.put(secondaryServerAddress,items);
        }
    }

    @Override
    public void removeItemFromBrokerMap(Item item, Address secondaryServerAddress) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
        for (Address address : brokerStorage.keySet()) {
            if(address.equals(secondaryServerAddress)){
                brokerStorage.get(address).remove(item);
            }
        }
    }

    @Override
    public Set<Item> addNotSentItemsToBrokerMap(Address secondaryServerAddress, Set<Item> notSentItems) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
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
    public Map<Address, Set<Item>> getBrokerStorage() {
        if (brokerStorage == null) {
            brokerStorage = new LinkedHashMap<>();
        }
        return brokerStorage;
    }

}
