package com.replicated_log.secondary_server_2.controller.rest;


import com.replicated_log.secondary_server_2.model.Ack;
import com.replicated_log.secondary_server_2.model.AckStatusCode;
import com.replicated_log.secondary_server_2.model.Item;
import com.replicated_log.secondary_server_2.service.SecondaryClient;
import com.replicated_log.secondary_server_2.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping(SecondaryRestController.SECONDARY_2_URL)
public class SecondaryRestController {

    private final Logger LOG = LogManager.getLogger(SecondaryRestController.class);

    public static final String SECONDARY_2_URL = "/secondary2";
    public final String SECONDARY_SERVER_NAME = "Secondary2";

    @Value("${secondary.server.baseurl}")
    private String SECONDARY_SERVER_BASE_URL;

    @Autowired
    private ItemService itemService;

    @Autowired
    private SecondaryClient secondaryClient;

    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = itemService.getItems();
        LOG.info("С--> Get Items Set of SecondaryServer");
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items-demo")
    public ResponseEntity<Set<Item>> findAllItemsForDemo() {
        Set<Item> items = itemService.getItemsForDemo();
        LOG.info("С--> Get Items Set of SecondaryServer for Demo");
        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        LOG.info("С--> Received Item - " + item.getText());

        LOG.info("С--> Set of " + SECONDARY_SERVER_NAME + ":" + itemService.getItems());
        itemService.simulateProcessing(10);

        itemService.addItem(item);
        LOG.info("С--> Added Item to " + SECONDARY_SERVER_NAME);
        LOG.info("С--> Set of " + SECONDARY_SERVER_NAME + ":" + itemService.getItems());

        secondaryClient.sendAckToMaster(new Ack(SECONDARY_SERVER_BASE_URL, item.getId(), AckStatusCode.SUCCESS));
        LOG.info("С--> Sent Ack to Master");

        return ResponseEntity.ok(item);
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        LOG.info("С--> Check health of " + SECONDARY_SERVER_NAME);
        return ResponseEntity.ok().build();
    }
}
