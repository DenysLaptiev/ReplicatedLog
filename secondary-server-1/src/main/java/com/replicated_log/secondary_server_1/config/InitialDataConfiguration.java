package com.replicated_log.secondary_server_1.config;

import com.replicated_log.secondary_server_1.model.Address;
import com.replicated_log.secondary_server_1.service.SecondaryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
public class InitialDataConfiguration {

    private final String SECONDARY_SERVER_BASE_URL;

    private final SecondaryClient secondaryClient;

    @Autowired
    public InitialDataConfiguration(SecondaryClient secondaryClient,
                                    @Value("${secondary.server.baseurl}") String secondaryServerBaseUrl) {
        this.secondaryClient = secondaryClient;
        this.SECONDARY_SERVER_BASE_URL = secondaryServerBaseUrl;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent() {

        Address address = new Address(SECONDARY_SERVER_BASE_URL);
        secondaryClient.sendAddressToMaster(address);

        log.info("--> Informed That Secondary1 Started");
    }
}
