package com.replicated_log.master_server.ack;

import com.replicated_log.master_server.item.Item;

import java.util.Map;
import java.util.Set;

public interface AckStorage<Ack> {

    Integer addItemToItemAcksMap(Item item);

    void addAckForItem(Integer itemId, Ack ack);

    Set<Ack> getAllAcksOfItem(Integer itemId);

    Map<Integer, Set<Ack>> getStorage();
}
