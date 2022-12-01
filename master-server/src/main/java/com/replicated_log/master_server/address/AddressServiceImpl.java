package com.replicated_log.master_server.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressStorage<Address> addressStorage;


    @Override
    public void addSecondaryAddress(Address address) {
        addressStorage.append(address);
    }

    @Override
    public Set<Address> getAllSecondaryAddresses() {
        return addressStorage.findAll();
    }

    @Override
    public int getAllSecondaryAddressesNumber() {
        return addressStorage.findAll().size();
    }
}
