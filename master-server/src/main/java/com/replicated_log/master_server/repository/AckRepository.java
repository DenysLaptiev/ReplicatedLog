package com.replicated_log.master_server.repository;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.model.Item;

import java.util.Map;
import java.util.Set;

public interface AckRepository {

    Integer addItemToItemAcksMap(Item item);

    void addAckForItem(Integer itemId, Ack ack);

    Set<Ack> getAllAcksOfItem(Integer itemId);

    Map<Integer, Set<Ack>> getStorage();
}
