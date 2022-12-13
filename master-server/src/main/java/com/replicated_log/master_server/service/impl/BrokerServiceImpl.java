package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.HealthStatus;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.BrokerRepository;
import com.replicated_log.master_server.service.AddressService;
import com.replicated_log.master_server.service.BrokerService;
import com.replicated_log.master_server.service.ItemService;
import com.replicated_log.master_server.service.MasterClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

@Service
public class BrokerServiceImpl implements BrokerService {

    private final Logger LOG = LogManager.getLogger(BrokerServiceImpl.class);

    @Autowired
    private BrokerRepository brokerRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AddressService addressService;

    private static boolean runBroker = false;


    @Override
    public synchronized Address addSecondaryServerToBrokerMap(Address secondaryServerAddress) {
        LOG.info("S--> BrokerServiceImpl: Adding secondary to broker " + secondaryServerAddress);
        Address secondaryServerAddressAdded = brokerRepository.addSecondaryServerToBrokerMap(secondaryServerAddress);

        Set<Item> itemsFromBeginning = itemService.getItems();
        LOG.info("S--> BrokerServiceImpl: Adding items from beginning to address in broker " + itemsFromBeginning);
        addNotSentItemsToBrokerMap(secondaryServerAddressAdded, itemsFromBeginning);
        if (!itemsFromBeginning.isEmpty()) {

            //when secondary address is added to Master, we start the broker(if it is not started yet).
            startBroker();
        }
        return secondaryServerAddressAdded;
    }

    @Override
    public synchronized void addItemToBrokerMap(Item item) {
        LOG.info("S--> BrokerServiceImpl: Adding item " + item);
        brokerRepository.addItemToBrokerMap(item);

        //when first item is added to Master, we start the broker.
        startBroker();
    }

    @Override
    public void removeItemFromBrokerMap(Item item, Address secondaryServerAddress) {
        LOG.info("S--> BrokerServiceImpl: Removing item " + item + " for Address " + secondaryServerAddress);
        brokerRepository.removeItemFromBrokerMap(item, secondaryServerAddress);
    }

    @Override
    public synchronized Set<Item> addNotSentItemsToBrokerMap(Address secondaryServerAddress, Set<Item> notSentItems) {
        LOG.info("S--> BrokerServiceImpl: Adding items " + notSentItems + " for Address " + secondaryServerAddress);
        return brokerRepository.addNotSentItemsToBrokerMap(secondaryServerAddress, notSentItems);
    }

    @Override
    public synchronized void publishToSecondaries() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runBroker) {
                    LOG.info("S--> BrokerServiceImpl: Publishing to Secondaries");
                    runBroker = false;

                    Map<Address, Set<Item>> brokerStorageMap = brokerRepository.getBrokerStorage();
                    for (Map.Entry<Address, Set<Item>> entry : brokerStorageMap.entrySet()) {

                        Address secondaryServerAddress = entry.getKey();
                        Set<Item> items = entry.getValue();

                        if (!items.isEmpty()) {
                            runBroker = true;

                            for (Item item : items) {
                                if (!HealthStatus.UNHEALTHY.equals(secondaryServerAddress.getHealthStatus())) {
                                    LOG.info("S--> BrokerServiceImpl: Publishing of item " + item + " to " + secondaryServerAddress + " (" + secondaryServerAddress.getHealthStatus() + ")");
                                    publishItemToSecondary(item, secondaryServerAddress, 300);
                                }
                            }
                        }
                    }

                    sleepMethod(1000);
                }
            }
        }).start();
    }

    @Override
    public void publishItemToSecondary(Item item, Address secondaryServerAddress, int sleepMillis) {
        String baseUrl = secondaryServerAddress.getAddress();

        MasterClient masterClient = new MasterClient(WebClient.create(baseUrl));
        masterClient.notifySecondaryAsync(item);

        sleepMethod(sleepMillis);
    }

    private void startBroker() {
        if (!runBroker) {
            runBroker = true;
            LOG.info("S--> BrokerServiceImpl: Start broker");
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
