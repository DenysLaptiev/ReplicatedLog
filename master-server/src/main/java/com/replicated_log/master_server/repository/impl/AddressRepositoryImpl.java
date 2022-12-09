package com.replicated_log.master_server.repository.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.repository.AddressRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AddressRepositoryImpl implements AddressRepository {

    private final Logger LOG = LogManager.getLogger(AddressRepositoryImpl.class);

    private Set<Address> addresses;

    @Override
    public synchronized Address append(Address address) {
        getStorage().add(address);
        LOG.info("R--> AddressRepositoryImpl: Added address to storage " + address);
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
