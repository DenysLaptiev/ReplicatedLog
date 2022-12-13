package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Item;
import org.springframework.web.reactive.function.client.WebClient;

public class MasterClient {

    private WebClient webClient;

    public MasterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Item notifySecondary(Item item){
        return webClient
                .post()
                .uri("/item")
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item.class)
                .block();
    }
}
