package com.replicated_log.secondary_server_1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Value("${master.server.baseurl}")
    private String MASTER_SERVER_BASE_URL;

    @Bean
    public WebClient getWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(MASTER_SERVER_BASE_URL)
                .build();
    }
}
