package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.item.Item;

import java.util.Map;
import java.util.Set;

public interface BrokerStorage {

    String addSecondaryServerToBrokerMap(String secondaryServerName);

    void addItemToBrokerMap(Item item);

    void removeItemFromBrokerMap(Item item, String secondaryServerAddress);

    Set<Item> addNotSentItemsToBrokerMap(String secondaryServerName, Set<Item> notSentItems);

    Map<String, Set<Item>> getBrokerStorage();
}
