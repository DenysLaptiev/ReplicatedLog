package com.replicated_log.master_server.service;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.address.HealthStatus;
import com.replicated_log.master_server.controller.rest.MasterWithBrokerRestController;
import com.replicated_log.master_server.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class MasterClient {

    private final Logger LOG = LogManager.getLogger(MasterClient.class);

    private WebClient webClient;

    public MasterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void notifySecondary(Item item) {
        webClient
                .post()
                .uri("/item")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item.class)
                .block();
    }

    public void notifySecondaryAsync(Item item) {
        webClient
                .post()
                .uri("/item")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item.class)
                .onErrorContinue((x,y)-> LOG.info("!-> Connection error: "+x.getMessage()))
                .subscribe();
    }

//    public void ping(Address address){
//        webClient
//                .get()
//                .uri("/health")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .retrieve()
//                .bodyToMono(Object.class)
//                .onErrorContinue((x,y)-> {
//                    if(address.getHealthStatus().equals(HealthStatus.SUSPECTED)){
//                        address.setHealthStatus(HealthStatus.UNHEALTHY);
//                    } else if (address.getHealthStatus().equals(HealthStatus.HEALTHY)) {
//                        address.setHealthStatus(HealthStatus.SUSPECTED);
//                    }
//                    LOG.info("!-> Connection error: "+x.getMessage());
//                }).doOnNext()
//                .subscribe();
//    }
}
