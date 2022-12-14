package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Ack;
import com.replicated_log.secondary_server_1.model.Address;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@AllArgsConstructor
public class SecondaryClient {

    private final WebClient webClient;

    public void sendAddressToMaster(Address address) {
        log.info("W--> SecondaryClient: Sending address " + address);
        webClient
                .post()
                .uri("/receive-address")
                .bodyValue(address)
                .retrieve()
                .bodyToMono(Address.class)
                .block();
    }

    public void sendAckToMaster(Ack ack) {
        log.info("W--> SecondaryClient: Sending ack " + ack);
        webClient
                .post()
                .uri("/receive-ack")
                .bodyValue(ack)
                .retrieve()
                .bodyToMono(Ack.class)
                .block();
    }
}
