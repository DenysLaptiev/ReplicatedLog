package com.replicated_log.secondary_server_2.service;

import com.replicated_log.secondary_server_2.model.Ack;
import com.replicated_log.secondary_server_2.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SecondaryClient {

    @Autowired
    private WebClient webClient;


    public void sendAddressToMaster(Address address){
        webClient
                .post()
                .uri("/receive-address")
                .bodyValue(address)
                .retrieve()
                .bodyToMono(Address.class)
                .block();
    }


    public void sendAckToMaster(Ack ack){
        webClient
                .post()
                .uri("/receive-ack")
                .bodyValue(ack)
                .retrieve()
                .bodyToMono(Ack.class)
                .block();
    }
}
