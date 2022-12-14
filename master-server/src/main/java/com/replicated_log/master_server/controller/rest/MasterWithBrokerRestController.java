package com.replicated_log.master_server.controller.rest;

import com.replicated_log.master_server.model.*;
import com.replicated_log.master_server.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestController
@RequestMapping(MasterWithBrokerRestController.MASTER_URL)
@CrossOrigin("*")
@Slf4j
public class MasterWithBrokerRestController {

    public static final String MASTER_URL = "/master";

    private final String MASTER_SERVER_BASE_URL;

    private final ItemService itemService;
    private final AddressService addressService;
    private final AckService ackService;
    private final BrokerService brokerService;
    private final PingService pingService;

    @Autowired
    public MasterWithBrokerRestController(ItemService itemService,
                                          AddressService addressService,
                                          AckService ackService,
                                          BrokerService brokerService,
                                          PingService pingService,
                                          @Value("${master.server.baseurl}") String masterServerBaseUrl) {
        this.itemService = itemService;
        this.addressService = addressService;
        this.ackService = ackService;
        this.brokerService = brokerService;
        this.pingService = pingService;
        this.MASTER_SERVER_BASE_URL = masterServerBaseUrl;
    }

    @PostMapping("/item/{w}")
    public ResponseEntity<String> appendItem(@RequestBody Item item, @PathVariable int w) {
        log.info("С--> Start appendItem POST, w = " + w);

        if (!pingService.hasQuorum()) {
            return ResponseEntity.status(SERVICE_UNAVAILABLE).body("There is no quorum. Master switched into read-only mode and doesn`t accept messages append requests");
        }

        item.setW(w);

        Item itemToSend = itemService.addItem(item);
        log.info("С--> Added to Master: " + itemToSend);
        log.info("С--> Set of Master:" + itemService.getItems());

        ackService.addItemToItemAcksMap(itemToSend);
        //because one ack is from Master
        ackService.addAckForItem(itemToSend.getId(), new Ack(MASTER_SERVER_BASE_URL, itemToSend.getId(), AckStatusCode.SUCCESS));

        brokerService.addItemToBrokerMap(itemToSend);

        while ((int) ackService.getNumberOfAcksOfItem(itemToSend.getId()) < itemToSend.getW()) {
            //LOG.info("С--> Waiting for " + (w - ackService.getAllAcksOfItem(itemToSend.getId()).size()) + " ack(s) from Secondary Server(s)...");
        }

        log.info("С--> Received all ACKs");
        log.info("С--> Set of Master:" + itemService.getItems());

        log.info("С--> Finish appendItem POST");
        return ResponseEntity.ok(itemToSend.toString());
    }


    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = itemService.getItems();
        return ResponseEntity.ok(items);
    }


    @PostMapping(value = "/receive-address")
    public ResponseEntity<Address> addSecondaryAddress(@RequestBody Address address) {

        address.setHealthStatus(HealthStatus.HEALTHY);
        addressService.addSecondaryAddress(address);
        log.info("С--> Added Secondary address: " + address.getAddress());

        brokerService.addSecondaryServerToBrokerMap(address);

        pingService.startPing();

        return ResponseEntity.ok(address);
    }


    @GetMapping("/addresses")
    public ResponseEntity<Set<Address>> getAllSecondaryAddresses() {
        Set<Address> addresses = addressService.getAllSecondaryAddresses();
        return ResponseEntity.ok(addresses);
    }


    @PostMapping(value = "/receive-ack")
    public ResponseEntity<Ack> receiveAck(@RequestBody Ack ack) {
        log.info("С--> Received ACK for ItemId=" + ack.getItemId() + " from Secondary " + ack.getServerAddress() + ": " + ack.getAckStatusCode());
        if (AckStatusCode.SUCCESS.equals(ack.getAckStatusCode())) {
            ackService.addAckForItem(ack.getItemId(), ack);
        }
        return ResponseEntity.ok(ack);
    }
}
