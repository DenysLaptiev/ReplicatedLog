package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Ack;
import com.replicated_log.secondary_server_1.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SecondaryClient {

    private final Logger LOG = LogManager.getLogger(SecondaryClient.class);

    @Autowired
    private WebClient webClient;

    public void sendAddressToMaster(Address address) {
        LOG.info("W--> SecondaryClient: Sending address " + address);
        webClient
                .post()
                .uri("/receive-address")
                .bodyValue(address)
                .retrieve()
                .bodyToMono(Address.class)
                .block();
    }

    public void sendAckToMaster(Ack ack) {
        LOG.info("W--> SecondaryClient: Sending ack " + ack);
        webClient
                .post()
                .uri("/receive-ack")
                .bodyValue(ack)
                .retrieve()
                .bodyToMono(Ack.class)
                .block();
    }
}
