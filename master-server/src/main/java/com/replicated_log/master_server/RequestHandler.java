package com.replicated_log.master_server;

import com.replicated_log.master_server.ack.AckService;
import com.replicated_log.master_server.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;


//TODO: remove this class
public class RequestHandler {

    private final Logger LOG = LogManager.getLogger(RequestHandler.class);

    int i;

    public RequestHandler(int i) {
        this.i = i;
    }

    public ResponseEntity handleRequest(Item itemToSend, AckService ackService) {

        while ((int) ackService.getNumberOfAcksOfItem(itemToSend.getId()) < itemToSend.getW()) {
            LOG.info("===> i= " + i + " itemId= " + itemToSend.getId() + " w=" + itemToSend.getW());
        }
        return ResponseEntity.ok(itemToSend);
    }
}
