package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Ack;
import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.Item;
import com.replicated_log.master_server.repository.AckRepository;
import com.replicated_log.master_server.service.AckService;
import com.replicated_log.master_server.service.BrokerService;
import com.replicated_log.master_server.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class AckServiceImpl implements AckService {

    private final AckRepository ackRepository;
    private final ItemService itemService;
    private final BrokerService brokerService;

    @Override
    public synchronized Integer addItemToItemAcksMap(Item item) {
        log.info("S--> AckServiceImpl: Adding item " + item);
        return ackRepository.addItemToItemAcksMap(item);
    }

    @Override
    public synchronized void addAckForItem(Integer itemId, Ack ack) {
        log.info("S--> AckServiceImpl: Adding ack for itemId=" + itemId);
        ackRepository.addAckForItem(itemId, ack);

        log.info("S--> AckServiceImpl: Removing item from broker, itemId=" + itemId);
        brokerService.removeItemFromBrokerMap(itemService.getItemById(itemId), new Address(ack.getServerAddress()));
    }

    @Override
    public Set<Ack> getAllAcksOfItem(Integer itemId) {
        return ackRepository.getAllAcksOfItem(itemId);
    }

    @Override
    public synchronized int getNumberOfAcksOfItem(Integer itemId) {
        int numberOfAcksOfItem = ackRepository.getAllAcksOfItem(itemId).size();
        log.info("S--> AckServiceImpl: Number of acks of itemId=" + itemId + " equals to " + numberOfAcksOfItem);
        return numberOfAcksOfItem;
    }
}
