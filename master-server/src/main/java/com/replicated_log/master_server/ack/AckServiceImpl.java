package com.replicated_log.master_server.ack;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.broker.BrokerService;
import com.replicated_log.master_server.item.Item;
import com.replicated_log.master_server.item.ItemService;
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

    @Autowired
    private ItemService itemService;

    @Autowired
    private BrokerService brokerService;

    @Override
    public synchronized Integer addItemToItemAcksMap(Item item) {
        return ackStorage.addItemToItemAcksMap(item);
    }

    @Override
    public synchronized void addAckForItem(Integer itemId, Ack ack) {
        ackStorage.addAckForItem(itemId, ack);
        brokerService.removeItemFromBrokerMap(itemService.getItemById(itemId), new Address(ack.getServerAddress()));
    }

    @Override
    public Set<Ack> getAllAcksOfItem(Integer itemId) {
        return ackStorage.getAllAcksOfItem(itemId);
    }

    @Override
    public synchronized int getNumberOfAcksOfItem(Integer itemId) {
        LOG.info("--> itemId=" + itemId);
        LOG.info("--> ackStorage.getAllAcksOfItem(itemId).size()=" + ackStorage.getAllAcksOfItem(itemId).size());
        return ackStorage.getAllAcksOfItem(itemId).size();
    }
}
