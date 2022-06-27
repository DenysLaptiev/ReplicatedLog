package com.replicated_log.master_server.broker.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemsSendConfig {

    @Value("${items.fanout.exchange.name}")
    private String ITEMS_FANOUT_EXCHANGE_NAME;

    @Bean
    FanoutExchange itemsFanoutExchange() {
        return new FanoutExchange(ITEMS_FANOUT_EXCHANGE_NAME);
    }
}
