package com.replicated_log.master_server.controller.rest;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.model.AckStatusCode;
import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.service.AckService;
import com.replicated_log.master_server.service.MasterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(MasterRestController.MASTER_URL)
@CrossOrigin("*")
public class MasterRestController {

    private final Logger LOG = LogManager.getLogger(MasterRestController.class);

    public static final String MASTER_URL = "/master";

    @Autowired
    private MasterService masterService;

    @Autowired
    private AckService ackService;

    @PostMapping("/item")
    public ResponseEntity appendItem(@RequestBody Item item) {
        LOG.info("--> Start of appendItem POST method");

        masterService.addItem(item);
        LOG.info("--> Added Item to List of MasterServer");

        int numberOfSecondaries = ackService.getSecondariesNumber();
        LOG.info("--> Waiting for " + numberOfSecondaries + " ack(s) from Secondary Server(s)...");
        masterService.notifyAllSecondaries(item);

        LOG.info("--> End of appendItem POST method");
        return ResponseEntity.ok(item);
    }


    @GetMapping("/items")
    public ResponseEntity<List<Item>> findAllItems() {
        List<Item> items = masterService.getItems();
        return ResponseEntity.ok(items);
    }


    @PostMapping(value = "/receive-address")
    public ResponseEntity<Address> addSecondaryAddress(@RequestBody Address address) {
        masterService.addSecondaryAddress(address);
        LOG.info("--> added Secondary address: " + address.getAddress());
        ackService.incrementSecondariesNumber();
        LOG.info("--> All Secondary addresses: " + masterService.getAllSecondaryAddresses());
        return ResponseEntity.ok(address);
    }


    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAllSecondaryAddresses() {
        List<Address> addresses = masterService.getAllSecondaryAddresses();
        return ResponseEntity.ok(addresses);
    }


    @PostMapping(value = "/receive-ack")
    public ResponseEntity<Ack> receiveAck(@RequestBody Ack ack) {
        LOG.info("--> Received ACK from Server " + ack.getServerName() + ": " + ack.getAckStatusCode());
        if (AckStatusCode.SUCCESS.equals(ack.getAckStatusCode())) {
            ackService.incrementAcksReceived();
        }
        return ResponseEntity.ok(ack);
    }
}
