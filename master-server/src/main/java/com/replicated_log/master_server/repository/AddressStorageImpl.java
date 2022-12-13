package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressStorageImpl implements AddressStorage<Address> {

    private final Logger LOG = LogManager.getLogger(AddressStorageImpl.class);

    private List<Address> addresses;

    @Override
    public synchronized boolean append(Address address) {

        LOG.info("--> AddressStorageImpl append method");
        return getStorage().add(address);
    }

    @Override
    public List<Address> findAll() {
        return getStorage();
    }

    @Override
    public List<Address> getStorage() {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }
}
