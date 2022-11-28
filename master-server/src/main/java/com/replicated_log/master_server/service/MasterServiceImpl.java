package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.AddressStorage;
import com.replicated_log.master_server.repository.ItemStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Service
public class MasterServiceImpl implements MasterService {

    private final Logger LOG = LogManager.getLogger(MasterServiceImpl.class);

    @Autowired
    private ItemStorage<Item> itemStorage;

    @Autowired
    private AddressStorage<Address> addressStorage;

    @Override
    public Item addItem(Item item) {
        return itemStorage.append(item);
    }

    @Override
    public Set<Item> getItems() {
        return itemStorage.findAll();
    }

    @Override
    public void addSecondaryAddress(Address address) {
        addressStorage.append(address);
    }

    @Override
    public Set<Address> getAllSecondaryAddresses() {
        return addressStorage.findAll();
    }

    @Override
    public void notifyAllSecondaries(Item item, boolean isAsync) {
        Set<Address> secondaryAddresses = getAllSecondaryAddresses();

        LOG.info("--> notifyAllSecondaries method, isAsync=" + isAsync);
        LOG.info("--> secondaryAddresses=" + secondaryAddresses);

        MasterClient masterClient;
        for (Address address : secondaryAddresses) {
            String baseUrl = address.getAddress();
            masterClient = new MasterClient(WebClient.create(baseUrl));
            if (isAsync) {
                masterClient.notifySecondaryAsync(item);
            } else {
                masterClient.notifySecondary(item);
            }
        }
    }
}
