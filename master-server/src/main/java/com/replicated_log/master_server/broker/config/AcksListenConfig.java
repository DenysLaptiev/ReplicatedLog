package com.replicated_log.master_server.broker.config;

import com.replicated_log.master_server.broker.listener.AcksListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcksListenConfig {

    @Value("${acks.topic.exchange.name}")
    private String ACKS_TOPIC_EXCHANGE_NAME;

    @Value("${acks.topic.binding.key}")
    private String ACKS_TOPIC_BINDING_KEY;

    @Autowired
    private AcksListener acksListener;


    @Bean
    TopicExchange acksTopicExchange() {
        return new TopicExchange(ACKS_TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue acksAutoDeleteQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding acksBinding(TopicExchange acksTopicExchange, Queue acksAutoDeleteQueue) {
        return BindingBuilder.bind(acksAutoDeleteQueue).to(acksTopicExchange).with(ACKS_TOPIC_BINDING_KEY);
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    MessageListenerContainer acksMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(acksAutoDeleteQueue());
        simpleMessageListenerContainer.setMessageListener(acksListener);
        return simpleMessageListenerContainer;
    }
}
