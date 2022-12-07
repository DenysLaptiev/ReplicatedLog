package com.replicated_log.master_server.broker;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.address.AddressService;
import com.replicated_log.master_server.address.HealthStatus;
import com.replicated_log.master_server.item.Item;
import com.replicated_log.master_server.item.ItemService;
import com.replicated_log.master_server.service.MasterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class BrokerServiceImpl implements BrokerService {

    @Autowired
    private BrokerRepository brokerRepository;

    @Autowired
    private ItemService itemService;

//    @Autowired
//    private AckService ackService;

    @Autowired
    private AddressService addressService;

    private static boolean runBroker = false;


    @Override
    public synchronized Address addSecondaryServerToBrokerMap(Address secondaryServerAddress) {
        Address secondaryServerAddressAdded = brokerRepository.addSecondaryServerToBrokerMap(secondaryServerAddress);
        Set<Item> itemsFromBeginning = itemService.getItems();
        addNotSentItemsToBrokerMap(secondaryServerAddressAdded, itemsFromBeginning);
        if (!itemsFromBeginning.isEmpty()) {
            startBroker();
        }
        return secondaryServerAddressAdded;
    }

    @Override
    public synchronized void addItemToBrokerMap(Item item) {
        brokerRepository.addItemToBrokerMap(item);

        //when first item is added to Master, we start the broker.
        startBroker();
    }

    @Override
    public void removeItemFromBrokerMap(Item item, Address secondaryServerAddress) {
        brokerRepository.removeItemFromBrokerMap(item, secondaryServerAddress);
    }

    @Override
    public synchronized Set<Item> addNotSentItemsToBrokerMap(Address secondaryServerAddress, Set<Item> notSentItems) {
        return brokerRepository.addNotSentItemsToBrokerMap(secondaryServerAddress, notSentItems);
    }

    //TODO: remove this method
    @Override
    public Set<Address> getAllSecondaryAddressesForItem(Item item) {
        Set<Address> addresses = new HashSet<>();
        Map<Address, Set<Item>> brokerStorageMap = brokerRepository.getBrokerStorage();
        for (Address secondaryAddress : brokerStorageMap.keySet()) {
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
    public synchronized void publishToSecondaries() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runBroker) {
                    runBroker = false;

                    Map<Address, Set<Item>> brokerStorageMap = brokerRepository.getBrokerStorage();
                    for (Map.Entry<Address, Set<Item>> entry : brokerStorageMap.entrySet()) {

                        Address secondaryServerAddress = entry.getKey();
                        Set<Item> items = entry.getValue();

                        if (!items.isEmpty()) {
                            runBroker = true;

                            for (Item item : items) {
                                if(!HealthStatus.UNHEALTHY.equals(secondaryServerAddress.getHealthStatus())){
                                    publishItemToSecondary(item, secondaryServerAddress, 300);
                                }
                                //removeAckedItemsFromBrokerMap();
                            }
                        }
                    }

                    sleepMethod(1000);
                }
            }
        }).start();

    }

//    @Override
//    public boolean publishToSecondaries(Item item) {
//
//        Set<String> addresses;
//        //Set<String> addressesToSend;
//        boolean allAcksReceived = false;
//
//        while (allAcksReceived == false) {
//            addresses = getAllSecondaryAddressesForItem(item);
//            for (String address : addresses) {
//                //addressesToSend = new TreeSet<>();
//                boolean ackReceived = false;
//
//                publishItemToSecondary(item, address, 300);
//                ackReceived = isAckForItemReceivedFromSecondary(item.getId(), address);
//
//                if (ackReceived) {
//                    removeItemFromBrokerMap(item, address);
//                }
//            }
//            //addresses = addressesToSend;
//            if (addresses.size() == 0) {
//                allAcksReceived = true;
//            }
//        }
//        return true;
//    }


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
    public void publishItemToSecondary(Item item, Address secondaryServerAddress, int sleepMillis) {
        String baseUrl = secondaryServerAddress.getAddress();
        MasterClient masterClient = new MasterClient(WebClient.create(baseUrl));
        masterClient.notifySecondaryAsync(item);

        sleepMethod(sleepMillis);
    }

//    @Override
//    public boolean isAckForItemReceivedFromSecondary(Integer itemId, String secondaryServerAddress) {
//
//        boolean ackReceived = false;
//
//        Set<Ack> allAcksOfItem = ackService.getAllAcksOfItem(itemId);
//        for (Ack ack : allAcksOfItem) {
//            if (secondaryServerAddress.equals(ack.getServerAddress())) {
//                ackReceived = true;
//                return ackReceived;
//            }
//        }
//        return ackReceived;
//    }

//    @Override
//    public synchronized void removeAckedItemsFromBrokerMap() {
//        Map<String, Set<Item>> brokerStorageMap = brokerRepository.getBrokerStorage();
//        for (Map.Entry<String, Set<Item>> entry : brokerStorageMap.entrySet()) {
//
//            String secondaryServerAddress = entry.getKey();
//            Set<Item> items = entry.getValue();
//
//            for (Item item : items) {
//                boolean ackReceived = isAckForItemReceivedFromSecondary(item.getId(), secondaryServerAddress);
//
//                if (ackReceived) {
//                    removeItemFromBrokerMap(item, secondaryServerAddress);
//                }
//            }
//        }
//    }

    private void startBroker() {
        if (!runBroker) {
            runBroker = true;
            publishToSecondaries();
        }
    }

    private void sleepMethod(int sleepMillis) {
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
