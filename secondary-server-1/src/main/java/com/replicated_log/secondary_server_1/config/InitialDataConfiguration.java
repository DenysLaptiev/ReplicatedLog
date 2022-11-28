package com.replicated_log.secondary_server_1.config;

import com.replicated_log.secondary_server_1.model.Address;
import com.replicated_log.secondary_server_1.service.SecondaryClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class InitialDataConfiguration {

    private final Logger LOG = LogManager.getLogger(InitialDataConfiguration.class);

    @Value("${secondary.server.baseurl}")
    private String SECONDARY_SERVER_BASE_URL;

    @Autowired
    private SecondaryClient secondaryClient;

    @PostConstruct
    public void postConstruct() {

        Address addr = new Address(SECONDARY_SERVER_BASE_URL);
        secondaryClient.sendAddressToMaster(addr);

        LOG.info("--> Informed That Secondary1 Started");
    }
}
