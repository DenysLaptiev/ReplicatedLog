package com.replicated_log.secondary_server_1.broker.listener;

import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.service.SecondaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemsListener implements MessageListener {

    private final Logger LOG = LogManager.getLogger(ItemsListener.class);

    @Autowired
    private SecondaryService secondaryService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private TopicExchange acksTopicExchange;

    @Value("${acks.topic.routingKey.secondary1}")
    private String ACKS_TOPIC_ROUTING_KEY;

    public void onMessage(Message message) {
        String text = new String(message.getBody());
        Item item = new Item(text);

        LOG.info("--> Received Item - " + item.getText());

        secondaryService.addItem(item);
        LOG.info("--> Added Item to List of SecondaryServer");

        String exchangeName = acksTopicExchange.getName();
        String routingKey = ACKS_TOPIC_ROUTING_KEY;
        String ackString = "ACK";

        amqpTemplate.convertAndSend(exchangeName, routingKey, ackString);
        LOG.info("--> Sent ACK to RabbitMQ Broker");
    }

}
