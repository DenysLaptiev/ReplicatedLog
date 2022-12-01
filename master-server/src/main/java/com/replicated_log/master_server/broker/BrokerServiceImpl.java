package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.ack.Ack;
import com.replicated_log.master_server.ack.AckService;
import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.address.AddressService;
import com.replicated_log.master_server.item.Item;
import com.replicated_log.master_server.item.ItemService;
import com.replicated_log.master_server.service.MasterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class BrokerServiceImpl implements BrokerService {

    @Autowired
    private BrokerStorage brokerStorage;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AckService ackService;

    @Autowired
    private AddressService addressService;


    @Override
    public synchronized String addSecondaryServerToBrokerMap(String secondaryServerAddress) {
        String secondaryServerAddressAdded = brokerStorage.addSecondaryServerToBrokerMap(secondaryServerAddress);
        Set<Item> itemsFromBeginning = itemService.getItems();
        addNotSentItemsToBrokerMap(secondaryServerAddressAdded, itemsFromBeginning);
        return secondaryServerAddressAdded;
    }

    @Override
    public synchronized void addItemToBrokerMap(Item item) {
        brokerStorage.addItemToBrokerMap(item);
    }

    @Override
    public void removeItemFromBrokerMap(Item item, String secondaryServerAddress) {
        brokerStorage.removeItemFromBrokerMap(item, secondaryServerAddress);
    }

    @Override
    public synchronized Set<Item> addNotSentItemsToBrokerMap(String secondaryServerAddress, Set<Item> notSentItems) {
        return brokerStorage.addNotSentItemsToBrokerMap(secondaryServerAddress, notSentItems);
    }

    @Override
    public Set<String> getAllSecondaryAddressesForItem(Item item) {
        Set<String> addresses = new HashSet<>();
        Map<String, Set<Item>> brokerStorageMap = brokerStorage.getBrokerStorage();
        for (String secondaryAddress : brokerStorageMap.keySet()) {
            if (brokerStorageMap.get(secondaryAddress).contains(item)) {
                addresses.add(secondaryAddress);
            }
        }
        return addresses;
    }
/*

    @Override
    public boolean publishToSecondaries() {

        boolean isComplete = false;

        while (isComplete == false) {
            //Set<Address> addresses = addressService.getAllSecondaryAddresses();
            Map<String, Set<Item>> brokerStorageMap = brokerStorage.getBrokerStorage();
            for (String address : brokerStorageMap.keySet()) {
                Set<Item> items = brokerStorageMap.get(address);

                for (Item item : items) {
                    int w = item.getW();
                    publishItemToSecondary(item, address, 300);
                }
            }

        }
        return isComplete;
    }
*/

/*
    @Override
    public boolean publishToSecondaries() {

        Set<Item> items = itemService.getItems();

        Map<String, Set<Item>> brokerStorageMap = brokerStorage.getBrokerStorage();

        for (Item item : items) {
            Set<String> addresses = getAllSecondaryAddressesForItem(item);
            for (String address : addresses) {
                boolean ackReceived = false;
                publishItemToSecondary(item, address, 300);
                ackReceived = isAckForItemReceivedFromSecondary(item.getId(), address);
                if (ackReceived) {
                    removeItemFromBrokerMap(item, address);
                }
            }
        }
        return true;
    }*/


    @Override
    public boolean publishToSecondaries(Item item) {

        Set<String> addresses = getAllSecondaryAddressesForItem(item);
        for (String address : addresses) {
            boolean ackReceived = false;
            publishItemToSecondary(item, address, 300);
            ackReceived = isAckForItemReceivedFromSecondary(item.getId(), address);
            if (ackReceived) {
                removeItemFromBrokerMap(item, address);
            }
        }

        return true;
    }


    /*@Override
    public void publishItemToSecondary(Item item, String secondaryServerAddress, int sleepMillis) {
        boolean ackReceived = false;
        while (ackReceived == false) {
            String baseUrl = secondaryServerAddress;
            MasterClient masterClient = new MasterClient(WebClient.create(baseUrl));
            masterClient.notifySecondaryAsync(item);

            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ackReceived = isAckForItemReceivedFromSecondary(item.getId(), secondaryServerAddress);
        }
    }*/


    @Override
    public void publishItemToSecondary(Item item, String secondaryServerAddress, int sleepMillis) {
        String baseUrl = secondaryServerAddress;
        MasterClient masterClient = new MasterClient(WebClient.create(baseUrl));
        masterClient.notifySecondaryAsync(item);

        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAckForItemReceivedFromSecondary(Integer itemId, String secondaryServerName) {

        boolean ackReceived = false;

        Set<Ack> allAcksOfItem = ackService.getAllAcksOfItem(itemId);
        for (Ack ack : allAcksOfItem) {
            if (secondaryServerName.equals(ack.getServerName())) {
                ackReceived = true;
                return ackReceived;
            }
        }
        return ackReceived;
    }

    @Override
    public void refreshBrokerMap() {

    }
}
