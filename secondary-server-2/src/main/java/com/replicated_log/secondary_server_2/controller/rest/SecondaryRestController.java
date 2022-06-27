package com.replicated_log.secondary_server_2.controller.rest;


import com.replicated_log.secondary_server_2.model.Item;
import com.replicated_log.secondary_server_2.service.SecondaryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(SecondaryRestController.SECONDARY_2_URL)
public class SecondaryRestController {

    public static final String SECONDARY_2_URL = "/secondary2";

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
