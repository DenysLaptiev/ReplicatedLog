package com.replicated_log.master_server.broker.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcksWaiter extends Thread {

    private final Logger LOG = LogManager.getLogger(AcksWaiter.class);

    private AckService ackService;

    public AcksWaiter(AckService ackService) {
        this.ackService = ackService;
    }

    @Override
    public void run() {
        while (ackService.getRemainedAcks() > 0) {
            LOG.info("--> We need " + ackService.getRemainedAcks() + " ACK(S) to Complete");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ackService.setAcksReceived(0);
    }
}
