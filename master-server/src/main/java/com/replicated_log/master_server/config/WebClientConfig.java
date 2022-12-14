package com.replicated_log.master_server.config;

import com.replicated_log.master_server.service.MasterClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebClientConfig {

    @Bean
    public MasterClient getWebClient() {
        return new MasterClient();
    }
}
