package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Item;
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
                .onErrorContinue((x, y) -> LOG.info("W--> MasterClient: Connection error: " + x.getMessage()))
                .subscribe();
    }
}
