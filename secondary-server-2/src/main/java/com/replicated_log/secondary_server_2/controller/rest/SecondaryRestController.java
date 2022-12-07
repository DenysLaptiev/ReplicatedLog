package com.replicated_log.secondary_server_2.controller.rest;


import com.replicated_log.secondary_server_2.model.Ack;
import com.replicated_log.secondary_server_2.model.AckStatusCode;
import com.replicated_log.secondary_server_2.model.Item;
import com.replicated_log.secondary_server_2.service.SecondaryClient;
import com.replicated_log.secondary_server_2.service.SecondaryService;
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

    public static final String SECONDARY_2_URL = "/secondary2";
    public final String SECONDARY_SERVER_NAME = "Secondary2";

    @Value("${secondary.server.baseurl}")
    private String SECONDARY_SERVER_BASE_URL;

    private final Logger LOG = LogManager.getLogger(SecondaryRestController.class);

    @Autowired
    private SecondaryService secondaryService;

    @Autowired
    private SecondaryClient secondaryClient;

    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = secondaryService.getItems();
        LOG.info("--> Get Items Set of SecondaryServer");
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items-demo")
    public ResponseEntity<Set<Item>> findAllItemsForDemo() {
        Set<Item> items = secondaryService.getItemsForDemo();
        LOG.info("--> Get Items Set of SecondaryServer for Demo");
        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        LOG.info("--> Received Item - " + item.getText());

        LOG.info("--> Set of Secondary2Server:" + secondaryService.getItems());
        secondaryService.simulateProcessing(10);

        secondaryService.addItem(item);
        LOG.info("--> Added Item to Set of SecondaryServer");
        LOG.info("--> Set of Secondary2Server:" + secondaryService.getItems());

        secondaryClient.sendAckToMaster(new Ack(SECONDARY_SERVER_BASE_URL, item.getId(), AckStatusCode.SUCCESS));
        LOG.info("--> Sent Ack to MasterServer");

        return ResponseEntity.ok(item);
    }

    @GetMapping("/health")
    public ResponseEntity health() {

        LOG.info("--> Check health of Secondary server");
        return ResponseEntity.ok().build();
    }
}
