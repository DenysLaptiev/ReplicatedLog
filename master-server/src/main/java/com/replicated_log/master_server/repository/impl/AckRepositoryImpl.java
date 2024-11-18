package com.replicated_log.master_server.repository.impl;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.AckRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Component
public class AckRepositoryImpl implements AckRepository {

    private final Logger LOG = LogManager.getLogger(AckRepositoryImpl.class);

    private static Integer ACK_ID = 0;

    // key=itemId, value=Set<Ack>
    private Map<Integer, Set<Ack>> itemAcks;

    @Override
    public Integer addItemToItemAcksMap(Item item) {
        Map<Integer, Set<Ack>> itemAcks = getStorage();
        Integer itemId = item.getId();
        if (!itemAcks.containsKey(itemId)) {
            itemAcks.put(itemId, new TreeSet<>());
        }
        LOG.info("R--> AckRepositoryImpl: Added item to storage " + item);
        return itemId;
    }

    @Override
    public synchronized void addAckForItem(Integer itemId, Ack ack) {
        Map<Integer, Set<Ack>> itemAcks = getStorage();
        if (itemAcks.containsKey(itemId)) {
            ack.setId(++ACK_ID);
            itemAcks.get(itemId).add(ack);
            LOG.info("R--> AckRepositoryImpl: Added ack for itemId=" + itemId);
        }
    }

    @Override
    public Set<Ack> getAllAcksOfItem(Integer itemId) {
        Set<Ack> allAcksOfItem = new TreeSet<>();
        Map<Integer, Set<Ack>> itemAcks = getStorage();
        if (itemAcks.containsKey(itemId)) {
            allAcksOfItem = itemAcks.get(itemId);
        }
        return allAcksOfItem;
    }

    @Override
    public Map<Integer, Set<Ack>> getStorage() {
        if (itemAcks == null) {
            itemAcks = new TreeMap<>();
        }
        return itemAcks;
    }
}