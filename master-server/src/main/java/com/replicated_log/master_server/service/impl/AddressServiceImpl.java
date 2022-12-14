package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.repository.AddressRepository;
import com.replicated_log.master_server.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public void addSecondaryAddress(Address address) {
        log.info("S--> AddressServiceImpl: Adding address " + address);
        addressRepository.append(address);
    }

    @Override
    public Set<Address> getAllSecondaryAddresses() {
        return addressRepository.getStorage();
    }
}
