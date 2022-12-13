package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.repository.AckRepository;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.service.AckService;
import com.replicated_log.master_server.service.BrokerService;
import com.replicated_log.master_server.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AckServiceImpl implements AckService {

    private final Logger LOG = LogManager.getLogger(AckServiceImpl.class);

    @Autowired
    private AckRepository ackRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BrokerService brokerService;

    @Override
    public synchronized Integer addItemToItemAcksMap(Item item) {
        LOG.info("S--> AckServiceImpl: Adding item " + item);
        return ackRepository.addItemToItemAcksMap(item);
    }

    @Override
    public synchronized void addAckForItem(Integer itemId, Ack ack) {
        LOG.info("S--> AckServiceImpl: Adding ack for itemId=" + itemId);
        ackRepository.addAckForItem(itemId, ack);

        LOG.info("S--> AckServiceImpl: Removing item from broker, itemId=" + itemId);
        brokerService.removeItemFromBrokerMap(itemService.getItemById(itemId), new Address(ack.getServerAddress()));
    }

    @Override
    public Set<Ack> getAllAcksOfItem(Integer itemId) {
        return ackRepository.getAllAcksOfItem(itemId);
    }

    @Override
    public synchronized int getNumberOfAcksOfItem(Integer itemId) {
        LOG.info("S--> AckServiceImpl: Number of acks of itemId=" + itemId + " equals to " + ackRepository.getAllAcksOfItem(itemId).size());
        return ackRepository.getAllAcksOfItem(itemId).size();
    }
}
