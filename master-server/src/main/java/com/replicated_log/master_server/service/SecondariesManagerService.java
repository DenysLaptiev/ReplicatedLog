package com.replicated_log.master_server.service;

import com.replicated_log.master_server.address.Address;

import java.util.Set;

public interface SecondariesManagerService {

    void addSecondaryAddress(Address address);
    Set<Address> getAllSecondaryAddresses();

    int getSecondariesNumber();

    void setSecondariesNumber(int secondariesNumber);

    void incrementSecondariesNumber();
}
