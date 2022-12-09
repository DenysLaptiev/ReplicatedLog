package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.repository.AddressRepository;
import com.replicated_log.master_server.service.AddressService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService {

    private final Logger LOG = LogManager.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressRepository addressRepository;


    @Override
    public void addSecondaryAddress(Address address) {
        LOG.info("S--> AddressServiceImpl: Adding address " + address);
        addressRepository.append(address);
    }

    @Override
    public Set<Address> getAllSecondaryAddresses() {
        return addressRepository.findAll();
    }
}
