package com.replicated_log.secondary_server_1.broker.config;

import com.replicated_log.secondary_server_1.broker.listener.ItemsListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ItemsListenConfig {

    @Value("${items.fanout.exchange.name}")
    private String ITEMS_FANOUT_EXCHANGE_NAME;

    @Autowired
    private ItemsListener itemsListener;

    @Bean
    FanoutExchange itemsFanoutExchange() {
        return new FanoutExchange(ITEMS_FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public Queue itemsAutoDeleteQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding itemsBinding(FanoutExchange itemsFanoutExchange, Queue itemsAutoDeleteQueue) {
        return BindingBuilder.bind(itemsAutoDeleteQueue).to(itemsFanoutExchange);
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    MessageListenerContainer itemsMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(itemsAutoDeleteQueue());
        simpleMessageListenerContainer.setMessageListener(itemsListener);
        return simpleMessageListenerContainer;
    }
}
