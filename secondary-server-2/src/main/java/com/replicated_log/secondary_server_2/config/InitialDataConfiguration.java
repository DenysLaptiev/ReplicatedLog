package com.replicated_log.secondary_server_2.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class InitialDataConfiguration {

    private final Logger LOG = LogManager.getLogger(InitialDataConfiguration.class);

    @Autowired
    private TopicExchange addSecondaryTopicExchange;

    @Value("${add.secondary.topic.routingKey.secondary2}")
    private String ADD_SECONDARY_TOPIC_ROUTING_KEY;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @PostConstruct
    public void postConstruct() {
        String exchangeName = addSecondaryTopicExchange.getName();
        String routingKey = ADD_SECONDARY_TOPIC_ROUTING_KEY;
        String addSecondaryString = "Secondary2 Started";

        amqpTemplate.convertAndSend(exchangeName, routingKey, addSecondaryString);
        LOG.info("--> Informed That Secondary2 Started");
    }
}
