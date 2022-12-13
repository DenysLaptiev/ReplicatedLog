package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.Item;

import java.util.Map;
import java.util.Set;

public interface BrokerRepository {

    Address addSecondaryServerToBrokerMap(Address secondaryServerAddress);

    void addItemToBrokerMap(Item item);

    void removeItemFromBrokerMap(Item item, Address secondaryServerAddress);

    Set<Item> addNotSentItemsToBrokerMap(Address secondaryServerAddress, Set<Item> notSentItems);

    Map<Address, Set<Item>> getBrokerStorage();
}
