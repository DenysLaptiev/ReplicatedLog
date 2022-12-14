package com.replicated_log.secondary_server_2.controller.rest;


import com.replicated_log.secondary_server_2.model.Ack;
import com.replicated_log.secondary_server_2.model.AckStatusCode;
import com.replicated_log.secondary_server_2.model.Item;
import com.replicated_log.secondary_server_2.service.ItemService;
import com.replicated_log.secondary_server_2.service.SecondaryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping(SecondaryRestController.SECONDARY_2_URL)
@Slf4j
public class SecondaryRestController {

    public static final String SECONDARY_2_URL = "/secondary2";
    public final String SECONDARY_SERVER_NAME = "Secondary2";

    private static final int PROCESSING_SECONDS = 10;

    private final String SECONDARY_SERVER_BASE_URL;

    private final ItemService itemService;
    private final SecondaryClient secondaryClient;

    @Autowired
    public SecondaryRestController(ItemService itemService,
                                   SecondaryClient secondaryClient,
                                   @Value("${secondary.server.baseurl}") String secondaryServerBaseUrl) {
        this.itemService = itemService;
        this.secondaryClient = secondaryClient;
        this.SECONDARY_SERVER_BASE_URL = secondaryServerBaseUrl;
    }

    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = itemService.getItems();
        log.info("С--> Get Items Set of SecondaryServer");
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items-demo")
    public ResponseEntity<Set<Item>> findAllItemsForDemo() {
        Set<Item> items = itemService.getItemsForDemo();
        log.info("С--> Get Items Set of SecondaryServer for Demo");
        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        log.info("С--> Received Item - " + item.getText());

        log.info("С--> Set of " + SECONDARY_SERVER_NAME + ":" + itemService.getItems());
        itemService.simulateProcessing(PROCESSING_SECONDS);

        itemService.addItem(item);
        log.info("С--> Added Item to " + SECONDARY_SERVER_NAME);
        log.info("С--> Set of " + SECONDARY_SERVER_NAME + ":" + itemService.getItems());

        secondaryClient.sendAckToMaster(new Ack(SECONDARY_SERVER_BASE_URL, item.getId(), AckStatusCode.SUCCESS));
        log.info("С--> Sent Ack to Master");

        return ResponseEntity.ok(item);
    }

    @GetMapping("/health")
    public ResponseEntity<HttpStatus> health() {
        log.info("С--> Check health of " + SECONDARY_SERVER_NAME);
        return ResponseEntity.ok().build();
    }
}
