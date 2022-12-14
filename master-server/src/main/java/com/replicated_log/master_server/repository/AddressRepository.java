package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Address;

import java.util.Set;

public interface AddressRepository {

    Address append(Address address);

    Set<Address> findAll();

    Set<Address> getStorage();
}
