package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class MasterClient {

    private final WebClient webClient = WebClient.create();

    public void notifySecondary(Item item, String baseUrl) {
        webClient
                .post()
                .uri(baseUrl + "/item")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item.class)
                .block();
    }

    public void notifySecondaryAsync(Item item, String baseUrl) {
        webClient
                .post()
                .uri(baseUrl + "/item")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item.class)
                .onErrorContinue((x, y) -> log.info("W--> MasterClient: Connection error: " + x.getMessage()))
                .subscribe();
    }
}
