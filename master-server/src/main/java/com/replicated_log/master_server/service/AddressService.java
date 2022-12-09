package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Address;

import java.util.Set;

public interface AddressService {

    void addSecondaryAddress(Address address);

    Set<Address> getAllSecondaryAddresses();
}
