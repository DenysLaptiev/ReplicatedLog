package com.replicated_log.master_server.service;

import com.replicated_log.master_server.item.Item;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class MasterClient {

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
                .subscribe();
    }
}
