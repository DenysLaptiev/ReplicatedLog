package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.Item;

import java.util.Set;

public interface BrokerService {

    Address addSecondaryServerToBrokerMap(Address secondaryServerAddress);

    void addItemToBrokerMap(Item item);

    void removeItemFromBrokerMap(Item item, Address secondaryServerAddress);

    Set<Item> addNotSentItemsToBrokerMap(Address secondaryServerAddress, Set<Item> notSentItems);

    void publishToSecondaries();

    void publishItemToSecondary(Item item, Address secondaryServerAddress, int sleepMillis);
}
