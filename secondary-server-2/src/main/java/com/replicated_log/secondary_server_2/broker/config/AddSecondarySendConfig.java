package com.replicated_log.secondary_server_2.broker.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddSecondarySendConfig {

    @Value("${add.secondary.topic.exchange.name}")
    private String ADD_SECONDARY_TOPIC_EXCHANGE_NAME;

    @Bean
    TopicExchange addSecondaryTopicExchange() {
        return new TopicExchange(ADD_SECONDARY_TOPIC_EXCHANGE_NAME);
    }
}
