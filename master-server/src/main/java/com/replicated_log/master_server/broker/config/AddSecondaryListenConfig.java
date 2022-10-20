package com.replicated_log.master_server.broker.config;

import com.replicated_log.master_server.broker.listener.AddSecondaryListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddSecondaryListenConfig {

    @Value("${add.secondary.topic.exchange.name}")
    private String ADD_SECONDARY_TOPIC_EXCHANGE_NAME;

    @Value("${add.secondary.topic.binding.key}")
    private String ADD_SECONDARY_TOPIC_BINDING_KEY;

    @Autowired
    private AddSecondaryListener addSecondaryListener;


    @Bean
    TopicExchange addSecondaryTopicExchange() {
        return new TopicExchange(ADD_SECONDARY_TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue addSecondaryAutoDeleteQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding addSecondaryBinding(TopicExchange addSecondaryTopicExchange, Queue addSecondaryAutoDeleteQueue) {
        return BindingBuilder.bind(addSecondaryAutoDeleteQueue).to(addSecondaryTopicExchange).with(ADD_SECONDARY_TOPIC_BINDING_KEY);
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    MessageListenerContainer addSecondaryMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(addSecondaryAutoDeleteQueue());
        simpleMessageListenerContainer.setMessageListener(addSecondaryListener);
        return simpleMessageListenerContainer;
    }
}
