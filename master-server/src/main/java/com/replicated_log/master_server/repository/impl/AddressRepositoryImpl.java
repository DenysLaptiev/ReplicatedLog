package com.replicated_log.master_server.repository.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class AddressRepositoryImpl implements AddressRepository {

    private Set<Address> addresses;

    @Override
    public synchronized Address append(Address address) {
        getStorage().add(address);
        log.info("R--> AddressRepositoryImpl: Added address to storage " + address);
        return address;
    }

    @Override
    public Set<Address> getStorage() {
        if (addresses == null) {
            addresses = new HashSet<>();
        }
        return addresses;
    }
}
