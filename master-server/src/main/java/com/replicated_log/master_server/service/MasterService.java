package com.replicated_log.master_server.service;

import com.replicated_log.master_server.item.Item;

import java.util.Set;

public interface MasterService {
    Item addItem(Item item);
    Set<Item> getItems();
//    void addSecondaryAddress(Address address);
//    Set<Address> getAllSecondaryAddresses();
//    void notifyAllSecondaries(Item item, boolean isAsync);
}
