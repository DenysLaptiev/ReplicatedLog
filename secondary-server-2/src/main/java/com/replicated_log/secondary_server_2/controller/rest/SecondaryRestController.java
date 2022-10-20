package com.replicated_log.secondary_server_2.controller.rest;


import com.replicated_log.secondary_server_2.model.Ack;
import com.replicated_log.secondary_server_2.model.AckStatusCode;
import com.replicated_log.secondary_server_2.model.Item;
import com.replicated_log.secondary_server_2.service.SecondaryClient;
import com.replicated_log.secondary_server_2.service.SecondaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(SecondaryRestController.SECONDARY_2_URL)
public class SecondaryRestController {

    public static final String SECONDARY_2_URL = "/secondary2";
    public final String SECONDARY_SERVER_NAME = "Secondary2";

    private final Logger LOG = LogManager.getLogger(SecondaryRestController.class);

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

        secondaryService.simulateProcessing(3);

        secondaryClient.sendAckToMaster(new Ack(SECONDARY_SERVER_NAME, AckStatusCode.SUCCESS));
        LOG.info("--> Sent Ack to MasterServer");

        return ResponseEntity.ok(item);
    }
}
