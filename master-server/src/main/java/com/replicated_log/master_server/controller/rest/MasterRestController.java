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

import java.util.Set;

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

        Item itemToSend = masterService.addItem(item);
        LOG.info("--> Set of MasterServer:" + masterService.getItems());

        ackService.addItemIdForAcks(itemToSend.getId());
        ackService.incrementAcksReceivedForItemId(itemToSend.getId());

        int numberOfSecondaries = ackService.getSecondariesNumber();
        LOG.info("--> Waiting for " + numberOfSecondaries + " ack(s) from Secondary Server(s)...");
        masterService.notifyAllSecondaries(itemToSend, false);

        LOG.info("--> End of appendItem POST method");
        return ResponseEntity.ok(itemToSend);
    }

    @PostMapping("/item/{w}")
    public ResponseEntity appendItem(@RequestBody Item item, @PathVariable int w) {
        LOG.info("--> Start of appendItem POST method");

        Item itemToSend = masterService.addItem(item);
        LOG.info("--> Added Item to Set of MasterServer");
        LOG.info("--> Set of MasterServer:" + masterService.getItems());

        ackService.addItemIdForAcks(itemToSend.getId());
        ackService.incrementAcksReceivedForItemId(itemToSend.getId());

        LOG.info("--> Waiting for " + (w - 1) + " ack(s) from Secondary Server(s)...");
        masterService.notifyAllSecondaries(itemToSend, true);

        while ((int) ackService.getAllAcksReceivedForItemId(itemToSend.getId()) < w) {
            //LOG.info("--> Waiting for " + (w - ackService.getAllAcksReceivedForItemId(itemToSend.getId())) + " ack(s) from Secondary Server(s)...");
        }

        LOG.info("--> Received all ACKs");
        LOG.info("--> Set of MasterServer:" + masterService.getItems());

        LOG.info("--> End of appendItem POST method");
        return ResponseEntity.ok(itemToSend);
    }


    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = masterService.getItems();
        LOG.info("--> Get Items Set of MasterServer");
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
    public ResponseEntity<Set<Address>> getAllSecondaryAddresses() {
        Set<Address> addresses = masterService.getAllSecondaryAddresses();
        LOG.info("--> Get Secondary Addresses Set of MasterServer");
        return ResponseEntity.ok(addresses);
    }


    @PostMapping(value = "/receive-ack")
    public ResponseEntity<Ack> receiveAck(@RequestBody Ack ack) {
        LOG.info("--> Received ACK for ItemId =" + ack.getItemId() + " from Server " + ack.getServerName() + ": " + ack.getAckStatusCode());
        if (AckStatusCode.SUCCESS.equals(ack.getAckStatusCode())) {
            //ackService.incrementAcksReceived();
            ackService.incrementAcksReceivedForItemId(ack.getItemId());
        }
        return ResponseEntity.ok(ack);
    }
}
