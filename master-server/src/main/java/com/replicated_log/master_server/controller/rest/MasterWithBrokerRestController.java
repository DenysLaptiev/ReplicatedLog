package com.replicated_log.master_server.controller.rest;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.service.AckService;
import com.replicated_log.master_server.model.AckStatusCode;
import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.service.AddressService;
import com.replicated_log.master_server.model.HealthStatus;
import com.replicated_log.master_server.service.BrokerService;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.service.ItemService;
import com.replicated_log.master_server.service.PingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestController
@RequestMapping(MasterWithBrokerRestController.MASTER_URL)
@CrossOrigin("*")
public class MasterWithBrokerRestController {

    private final Logger LOG = LogManager.getLogger(MasterWithBrokerRestController.class);

    public static final String MASTER_URL = "/master";

    @Value("${master.server.baseurl}")
    private String MASTER_SERVER_BASE_URL;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AckService ackService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private PingService pingService;


    @PostMapping("/item/{w}")
    public ResponseEntity appendItem(@RequestBody Item item, @PathVariable int w) {
        LOG.info("С--> Start appendItem POST, w = " + w);

        if (!pingService.hasQuorum()) {
            return ResponseEntity.status(SERVICE_UNAVAILABLE).body("There is no quorum. Master switched into read-only mode and doesn`t accept messages append requests");
        }

        item.setW(w);

        Item itemToSend = itemService.addItem(item);
        LOG.info("С--> Added to Master: " + itemToSend);
        LOG.info("С--> Set of Master:" + itemService.getItems());

        ackService.addItemToItemAcksMap(itemToSend);
        //because one ack is from Master
        ackService.addAckForItem(itemToSend.getId(), new Ack(MASTER_SERVER_BASE_URL, itemToSend.getId(), AckStatusCode.SUCCESS));

        brokerService.addItemToBrokerMap(itemToSend);

        while ((int) ackService.getNumberOfAcksOfItem(itemToSend.getId()) < itemToSend.getW()) {
            //LOG.info("С--> Waiting for " + (w - ackService.getAllAcksOfItem(itemToSend.getId()).size()) + " ack(s) from Secondary Server(s)...");
        }

        LOG.info("С--> Received all ACKs");
        LOG.info("С--> Set of Master:" + itemService.getItems());

        LOG.info("С--> Finish appendItem POST");
        return ResponseEntity.ok(itemToSend);
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
        LOG.info("С--> Added Secondary address: " + address.getAddress());

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
        LOG.info("С--> Received ACK for ItemId=" + ack.getItemId() + " from Secondary " + ack.getServerAddress() + ": " + ack.getAckStatusCode());
        if (AckStatusCode.SUCCESS.equals(ack.getAckStatusCode())) {
            ackService.addAckForItem(ack.getItemId(), ack);
        }
        return ResponseEntity.ok(ack);
    }
}
