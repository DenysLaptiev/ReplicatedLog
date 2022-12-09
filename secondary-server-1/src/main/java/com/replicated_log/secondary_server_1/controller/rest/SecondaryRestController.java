package com.replicated_log.secondary_server_1.controller.rest;

import com.replicated_log.secondary_server_1.model.Ack;
import com.replicated_log.secondary_server_1.model.AckStatusCode;
import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.service.SecondaryClient;
import com.replicated_log.secondary_server_1.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(SecondaryRestController.SECONDARY_1_URL)
@CrossOrigin("*")
public class SecondaryRestController {

    private final Logger LOG = LogManager.getLogger(SecondaryRestController.class);

    public static final String SECONDARY_1_URL = "/secondary1";
    public final String SECONDARY_SERVER_NAME = "Secondary1";

    @Value("${secondary.server.baseurl}")
    private String SECONDARY_SERVER_BASE_URL;

    @Autowired
    private ItemService itemService;

    @Autowired
    private SecondaryClient secondaryClient;

    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = itemService.getItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items-demo")
    public ResponseEntity<Set<Item>> findAllItemsForDemo() {
        Set<Item> items = itemService.getItemsForDemo();
        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        LOG.info("C--> Received Item - " + item.getText());

        LOG.info("C--> Set of " + SECONDARY_SERVER_NAME + ":" + itemService.getItems());
        itemService.simulateProcessing(5);

        itemService.addItem(item);
        LOG.info("C--> Added Item to " + SECONDARY_SERVER_NAME);
        LOG.info("C--> Set of " + SECONDARY_SERVER_NAME + ":" + itemService.getItems());

        secondaryClient.sendAckToMaster(new Ack(SECONDARY_SERVER_BASE_URL, item.getId(), AckStatusCode.SUCCESS));
        LOG.info("C--> Sent Ack to Master");

        return ResponseEntity.ok(item);
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        LOG.info("C--> Check health of " + SECONDARY_SERVER_NAME);
        return ResponseEntity.ok().build();
    }
}
