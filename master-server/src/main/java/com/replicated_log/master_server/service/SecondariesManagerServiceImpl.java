package com.replicated_log.master_server.service;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.address.AddressStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class SecondariesManagerServiceImpl implements SecondariesManagerService {

    @Autowired
    private AddressStorage<Address> addressStorage;

    private static int secondariesNumber;

    @Override
    public void addSecondaryAddress(Address address) {
        addressStorage.append(address);
    }

    @Override
    public Set<Address> getAllSecondaryAddresses() {
        return addressStorage.findAll();
    }

    @Override
    public int getSecondariesNumber() {
        return secondariesNumber;
    }

    @Override
    public void setSecondariesNumber(int secondariesNumber) {
        SecondariesManagerServiceImpl.secondariesNumber = secondariesNumber;
    }


    @Override
    public void incrementSecondariesNumber() {
        int old = getSecondariesNumber();
        setSecondariesNumber(old + 1);
    }
}
