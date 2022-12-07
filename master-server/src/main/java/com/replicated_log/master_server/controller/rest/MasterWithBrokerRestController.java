package com.replicated_log.master_server.controller.rest;

import com.replicated_log.master_server.ack.Ack;
import com.replicated_log.master_server.ack.AckService;
import com.replicated_log.master_server.ack.AckStatusCode;
import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.address.AddressService;
import com.replicated_log.master_server.address.HealthStatus;
import com.replicated_log.master_server.broker.BrokerService;
import com.replicated_log.master_server.item.Item;
import com.replicated_log.master_server.item.ItemService;
import com.replicated_log.master_server.ping.PingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.HTTP_VERSION_NOT_SUPPORTED;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestController
@RequestMapping(MasterWithBrokerRestController.MASTER_URL)
@CrossOrigin("*")
public class MasterWithBrokerRestController {

    public static final String MASTER_URL = "/master";

    @Value("${master.server.baseurl}")
    private String MASTER_SERVER_BASE_URL;

    private final Logger LOG = LogManager.getLogger(MasterWithBrokerRestController.class);

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
        LOG.info("--> Start of appendItem POST method with write concern parameter w = " + w);

        if (!pingService.hasQuorum()) {
            return ResponseEntity.status(SERVICE_UNAVAILABLE).body("There is no quorum. Master switched into read-only mode and doesn`t accept messages append requests");
        }

        item.setW(w);

        Item itemToSend = itemService.addItem(item);
        LOG.info("--> itemToSend=" + itemToSend);
        LOG.info("--> Added Item to Set of MasterServer");
        LOG.info("--> Set of MasterServer:" + itemService.getItems());

        ackService.addItemToItemAcksMap(itemToSend);
        //because one ack is from Master
        ackService.addAckForItem(itemToSend.getId(), new Ack(MASTER_SERVER_BASE_URL, itemToSend.getId(), AckStatusCode.SUCCESS));

        brokerService.addItemToBrokerMap(itemToSend);

//        Set<Address> secondariesAddresses = addressService.getAllSecondaryAddresses();
//        int numberOfSecondaries = addressService.getAllSecondaryAddressesNumber();
//
//        LOG.info("--> Waiting for " + (w - 1) + " ack(s) from Secondary Server(s)...");
////        masterService.notifyAllSecondaries(itemToSend, true);
//
//        brokerService.publishToSecondaries(itemToSend);


        while ((int) ackService.getNumberOfAcksOfItem(itemToSend.getId()) < itemToSend.getW()) {
            LOG.info("--> Waiting for " + (w - ackService.getAllAcksOfItem(itemToSend.getId()).size()) + " ack(s) from Secondary Server(s)...");
        }


        /*Thread acksWaiter = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO: should we wait in new Thread?
                while ((int) ackService.getNumberOfAcksOfItem(itemToSend.getId()) < itemToSend.getW()) {
                    LOG.info("--> Waiting for " + (w - ackService.getAllAcksOfItem(itemToSend.getId()).size()) + " ack(s) from Secondary Server(s)...");
                }
            }
        });
        acksWaiter.start();
        try {
            acksWaiter.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        LOG.info("--> Received all ACKs");
        LOG.info("--> Set of MasterServer:" + itemService.getItems());

        LOG.info("--> End of appendItem POST method");
        return ResponseEntity.ok(itemToSend);
//        return new RequestHandler(itemToSend.getId()).handleRequest(itemToSend,ackService);
    }


    @GetMapping("/items")
    public ResponseEntity<Set<Item>> findAllItems() {
        Set<Item> items = itemService.getItems();
        LOG.info("--> Get Items Set of MasterServer");
        return ResponseEntity.ok(items);
    }


    @PostMapping(value = "/receive-address")
    public ResponseEntity<Address> addSecondaryAddress(@RequestBody Address address) {
        address.setHealthStatus(HealthStatus.HEALTHY);
        addressService.addSecondaryAddress(address);
        LOG.info("--> added Secondary address: " + address.getAddress());

        brokerService.addSecondaryServerToBrokerMap(address);


        pingService.startPing();

        return ResponseEntity.ok(address);
    }


    @GetMapping("/addresses")
    public ResponseEntity<Set<Address>> getAllSecondaryAddresses() {
        Set<Address> addresses = addressService.getAllSecondaryAddresses();
        LOG.info("--> Get Secondary Addresses Set of MasterServer");
        return ResponseEntity.ok(addresses);
    }


    @PostMapping(value = "/receive-ack")
    public ResponseEntity<Ack> receiveAck(@RequestBody Ack ack) {
        LOG.info("--> Received ACK for ItemId =" + ack.getItemId() + " from Server " + ack.getServerAddress() + ": " + ack.getAckStatusCode());
        if (AckStatusCode.SUCCESS.equals(ack.getAckStatusCode())) {
            ackService.addAckForItem(ack.getItemId(), ack);
        }
        return ResponseEntity.ok(ack);
    }
}
