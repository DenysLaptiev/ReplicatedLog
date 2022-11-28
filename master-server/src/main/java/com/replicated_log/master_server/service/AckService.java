package com.replicated_log.master_server.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AckService {

    private static int secondariesNumber;
    private static int acksReceived;

//    key=itemId, value=numberOfAcks
    private static Map<Integer, Integer> itemAcks = new HashMap<>();

    public int getSecondariesNumber() {
        return secondariesNumber;
    }

    public void setSecondariesNumber(int secondariesNumber) {
        AckService.secondariesNumber = secondariesNumber;
    }

    public int getAcksReceived() {
        return acksReceived;
    }

    public void setAcksReceived(int acksReceived) {
        AckService.acksReceived = acksReceived;
    }

    public void addItemIdForAcks(Integer itemId) {
        itemAcks.put(itemId, 0);
    }

    public synchronized void incrementAcksReceivedForItemId(Integer itemId) {
        itemAcks.put(itemId, itemAcks.get(itemId) + 1);
    }

    public Integer getAllAcksReceivedForItemId(Integer itemId) {
        return itemAcks.get(itemId);
    }

    public void incrementAcksReceived() {
        int old = getAcksReceived();
        setAcksReceived(old + 1);
    }

    public void incrementSecondariesNumber() {
        int old = getSecondariesNumber();
        setSecondariesNumber(old + 1);
    }

}
