package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AddressStorageImpl implements AddressStorage<Address> {

    private final Logger LOG = LogManager.getLogger(AddressStorageImpl.class);

    private Set<Address> addresses;

    @Override
    public synchronized Address append(Address address) {

        LOG.info("--> AddressStorageImpl append method");
        getStorage().add(address);
        return address;
    }

    @Override
    public Set<Address> findAll() {
        return getStorage();
    }

    @Override
    public Set<Address> getStorage() {
        if (addresses == null) {
            addresses = new HashSet<>();
        }
        return addresses;
    }
}
