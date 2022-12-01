package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.item.Item;

import java.util.Set;

public interface BrokerService {

    String addSecondaryServerToBrokerMap(String secondaryServerAddress);

    void addItemToBrokerMap(Item item);

    void removeItemFromBrokerMap(Item item, String secondaryServerAddress);

    Set<Item> addNotSentItemsToBrokerMap(String secondaryServerAddress, Set<Item> notSentItems);

    Set<String> getAllSecondaryAddressesForItem(Item item);

    boolean publishToSecondaries(Item item);

    void publishItemToSecondary(Item item,String secondaryServerName,int sleepMillis);

    boolean isAckForItemReceivedFromSecondary(Integer itemId,String secondaryServerName);

    void refreshBrokerMap();
}
