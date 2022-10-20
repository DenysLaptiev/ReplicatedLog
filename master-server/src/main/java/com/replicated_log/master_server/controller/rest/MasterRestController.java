package com.replicated_log.master_server.controller.rest;

import com.replicated_log.master_server.broker.service.AckService;
import com.replicated_log.master_server.broker.service.AcksWaiter;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.service.MasterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(MasterRestController.MASTER_URL)
public class MasterRestController {

    public static final String MASTER_URL = "/master";

    private final Logger LOG = LogManager.getLogger(MasterRestController.class);

    @Autowired
    private MasterService masterService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;

    @Autowired
    private AckService ackService;

    @PostMapping
    public ResponseEntity appendItem(@RequestBody Item item) {
        LOG.info("--> Start of POST method");

        masterService.addItem(item);
        LOG.info("--> Added Item to List of MasterServer");

        String exchangeName = fanoutExchange.getName();
        String routingKey = "";
        String text = item.getText();

        amqpTemplate.convertAndSend(exchangeName, routingKey, text);
        LOG.info("--> Sent Item to RabbitMQ Broker");

        LOG.info("--> Waiting for ACK...");
        AcksWaiter acksWaiter = new AcksWaiter(ackService);
        acksWaiter.start();
        try {
            acksWaiter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        LOG.info("--> End of POST method");
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        List<Item> items = masterService.getItems();
        LOG.info("--> Get Items List of MasterServer");
        return ResponseEntity.ok(items);
    }
}
