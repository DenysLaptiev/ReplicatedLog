package com.replicated_log.master_server.broker.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AckService {

    private final Logger LOG = LogManager.getLogger(AckService.class);

    private static int secondariesNumber;
    private static int acksReceived;

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

    public void incrementAcksReceived() {
        int old = getAcksReceived();
        setAcksReceived(old + 1);
    }

    public void incrementSecondariesNumber() {
        int old = getSecondariesNumber();
        setSecondariesNumber(old + 1);
    }

    public int getRemainedAcks(){
        return getSecondariesNumber() - getAcksReceived();
    }
}
