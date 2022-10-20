package com.replicated_log.secondary_server_1.service;

import com.replicated_log.secondary_server_1.model.Ack;
import com.replicated_log.secondary_server_1.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SecondaryClient {

    @Autowired
    private WebClient webClient;


    public Address sendAddressToMaster(Address address){
        return webClient
                .post()
                .uri("/receive-address")
                .bodyValue(address)
                .retrieve()
                .bodyToMono(Address.class)
                .block();
    }


    public Ack sendAckToMaster(Ack ack){
        return webClient
                .post()
                .uri("/receive-ack")
                .bodyValue(ack)
                .retrieve()
                .bodyToMono(Ack.class)
                .block();
    }
}
