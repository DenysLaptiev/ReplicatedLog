package com.replicated_log.master_server.ack;

import com.replicated_log.master_server.controller.rest.MasterWithBrokerRestController;
import com.replicated_log.master_server.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AckServiceImpl implements AckService {

    private final Logger LOG = LogManager.getLogger(AckServiceImpl.class);

    @Autowired
    private AckStorage<Ack> ackStorage;

    @Override
    public Integer addItemToItemAcksMap(Item item) {
        return ackStorage.addItemToItemAcksMap(item);
    }

    @Override
    public void addAckForItem(Integer itemId, Ack ack) {
        ackStorage.addAckForItem(itemId, ack);
    }

    @Override
    public Set<Ack> getAllAcksOfItem(Integer itemId) {
        return ackStorage.getAllAcksOfItem(itemId);
    }

    @Override
    public int getNumberOfAcksOfItem(Integer itemId) {
        LOG.info("--> itemId=" + itemId);
        LOG.info("--> ackStorage.getAllAcksOfItem(itemId).size()=" + ackStorage.getAllAcksOfItem(itemId).size());
        return ackStorage.getAllAcksOfItem(itemId).size();
    }
}
