package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.item.Item;

import java.util.Set;

public interface BrokerService {

    Address addSecondaryServerToBrokerMap(Address secondaryServerAddress);

    void addItemToBrokerMap(Item item);

    void removeItemFromBrokerMap(Item item, Address secondaryServerAddress);

    Set<Item> addNotSentItemsToBrokerMap(Address secondaryServerAddress, Set<Item> notSentItems);


    //TODO: remove this method
    Set<Address> getAllSecondaryAddressesForItem(Item item);

    //boolean publishToSecondaries(Item item);

    void publishToSecondaries();

    void publishItemToSecondary(Item item, Address secondaryServerAddress, int sleepMillis);

    //boolean isAckForItemReceivedFromSecondary(Integer itemId, String secondaryServerName);

    //void removeAckedItemsFromBrokerMap();
}
