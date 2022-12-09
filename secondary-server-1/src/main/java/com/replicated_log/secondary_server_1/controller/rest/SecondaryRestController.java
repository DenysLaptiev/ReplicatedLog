package com.replicated_log.secondary_server_1.controller.rest;


import com.replicated_log.secondary_server_1.model.Ack;
import com.replicated_log.secondary_server_1.model.AckStatusCode;
import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.service.SecondaryClient;
import com.replicated_log.secondary_server_1.service.SecondaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SecondaryRestController.SECONDARY_1_URL)
public class SecondaryRestController {

    private final Logger LOG = LogManager.getLogger(SecondaryRestController.class);

    public static final String SECONDARY_1_URL = "/secondary1";
    public final String SECONDARY_SERVER_NAME = "Secondary1";

    @Autowired
    private SecondaryService secondaryService;

    @Autowired
    private SecondaryClient secondaryClient;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> findAllItems() {
        List<Item> items = secondaryService.getItems();
        LOG.info("--> Get Items List of SecondaryServer");
        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        LOG.info("--> Received Item - " + item.getText());

        secondaryService.addItem(item);
        LOG.info("--> Added Item to List of SecondaryServer");

        secondaryService.simulateProcessing(10);

        secondaryClient.sendAckToMaster(new Ack(SECONDARY_SERVER_NAME, AckStatusCode.SUCCESS));
        LOG.info("--> Sent Ack to MasterServer");

        return ResponseEntity.ok(item);
    }
}
