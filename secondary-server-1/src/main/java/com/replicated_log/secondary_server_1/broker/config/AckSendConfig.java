package com.replicated_log.secondary_server_1.broker.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AckSendConfig {

    @Value("${acks.topic.exchange.name}")
    private String ACKS_TOPIC_EXCHANGE_NAME;

    @Bean
    TopicExchange acksTopicExchange() {
        return new TopicExchange(ACKS_TOPIC_EXCHANGE_NAME);
    }
}
