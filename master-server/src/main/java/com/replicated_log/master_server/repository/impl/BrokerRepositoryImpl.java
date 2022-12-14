package com.replicated_log.master_server.repository.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.BrokerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Component
public class BrokerRepositoryImpl implements BrokerRepository {

    private final Logger LOG = LogManager.getLogger(BrokerRepositoryImpl.class);

    private Map<Address, Set<Item>> brokerStorage;

    @Override
    public Address addSecondaryServerToBrokerMap(Address secondaryServerAddress) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
        if (!brokerStorage.containsKey(secondaryServerAddress)) {
            brokerStorage.put(secondaryServerAddress, new TreeSet<>());
            LOG.info("R--> BrokerRepositoryImpl: Added address to broker " + secondaryServerAddress);
        }
        return secondaryServerAddress;
    }

    @Override
    public void addItemToBrokerMap(Item item) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
        for (Address secondaryServerAddress : brokerStorage.keySet()) {
            Set<Item> items = brokerStorage.get(secondaryServerAddress);
            items.add(item);
            brokerStorage.put(secondaryServerAddress, items);
        }
        LOG.info("R--> BrokerRepositoryImpl: Added item to broker " + item);
    }

    @Override
    public void removeItemFromBrokerMap(Item item, Address secondaryServerAddress) {
        Map<Address, Set<Item>> brokerStorage = getBrokerStorage();
        for (Address address : brokerStorage.keySet()) {
            if (address.equals(secondaryServerAddress)) {
                brokerStorage.get(address).remove(item);
                LOG.info("R--> BrokerRepositoryImpl: Removed item from broker. Item=" + item + "Address=" + secondaryServerAddress);
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
        LOG.info("R--> BrokerRepositoryImpl: Added items to broker " + notSentItems);
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
