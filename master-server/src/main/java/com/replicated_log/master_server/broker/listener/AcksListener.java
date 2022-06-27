package com.replicated_log.master_server.broker.listener;

import com.replicated_log.master_server.broker.service.AckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcksListener implements MessageListener {

    private final Logger LOG = LogManager.getLogger(AcksListener.class);

    @Autowired
    private AckService ackService;

    public void onMessage(Message message) {
        LOG.info("--> Received ACK - " + new String(message.getBody()));
        ackService.incrementAcksReceived();
    }
}
