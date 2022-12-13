package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.model.Item;

import java.util.Set;

public interface AckService {

    Integer addItemToItemAcksMap(Item item);

    void addAckForItem(Integer itemId, Ack ack);

    Set<Ack> getAllAcksOfItem(Integer itemId);

    int getNumberOfAcksOfItem(Integer itemId);
}
