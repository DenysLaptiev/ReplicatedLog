package com.replicated_log.secondary_server_1.controller.rest;


import com.replicated_log.secondary_server_1.model.Item;
import com.replicated_log.secondary_server_1.service.SecondaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(SecondaryRestController.SECONDARY_1_URL)
public class SecondaryRestController {

    public static final String SECONDARY_1_URL = "/secondary1";

    private final Logger LOG = LogManager.getLogger(SecondaryRestController.class);

    @Autowired
    private SecondaryService secondaryService;

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        List<Item> items = secondaryService.getItems();
        LOG.info("--> Get Items List of SecondaryServer");
        return ResponseEntity.ok(items);
    }
}
