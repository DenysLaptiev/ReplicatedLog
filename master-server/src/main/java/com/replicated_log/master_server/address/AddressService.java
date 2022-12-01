package com.replicated_log.master_server.address;

import java.util.Set;

public interface AddressService {

    void addSecondaryAddress(Address address);
    Set<Address> getAllSecondaryAddresses();
    int getAllSecondaryAddressesNumber();
}
