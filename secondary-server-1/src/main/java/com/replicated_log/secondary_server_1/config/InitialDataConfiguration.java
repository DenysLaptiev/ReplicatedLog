package com.replicated_log.secondary_server_1.config;

import com.replicated_log.secondary_server_1.model.Address;
import com.replicated_log.secondary_server_1.service.SecondaryClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class InitialDataConfiguration {

    private final Logger LOG = LogManager.getLogger(InitialDataConfiguration.class);

    @Value("${secondary.server.baseurl}")
    private String SECONDARY_SERVER_BASE_URL;

    @Autowired
    private SecondaryClient secondaryClient;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent() {

        Address address = new Address(SECONDARY_SERVER_BASE_URL);
        secondaryClient.sendAddressToMaster(address);

        LOG.info("--> Informed That Secondary1 Started");
    }
}
