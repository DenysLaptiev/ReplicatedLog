package com.replicated_log.master_server.service.impl;

import com.replicated_log.master_server.model.Address;
import com.replicated_log.master_server.model.HealthStatus;
import com.replicated_log.master_server.service.AddressService;
import com.replicated_log.master_server.service.PingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

@Service
@Slf4j
public class PingServiceImpl implements PingService {

    private final Integer quorum;

    private final AddressService addressService;

    private static boolean runPing = false;

    @Autowired
    public PingServiceImpl(AddressService addressService,
                           @Value("${server.quorum}") Integer quorum) {
        this.addressService = addressService;
        this.quorum = quorum;
    }

    @Override
    public void runPing() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runPing) {
                    ping();
                    sleepMethod(1000);
                }
            }
        }).start();
    }


    @Override
    public void ping() {
        Set<Address> allSecondaryAddresses = addressService.getAllSecondaryAddresses();
        for (Address secondaryAddress : allSecondaryAddresses) {

            String baseUrl = secondaryAddress.getAddress();
            HealthStatus resultHealthStatus = getConnectionHealthStatus(baseUrl);
            if (HealthStatus.SUSPECTED.equals(secondaryAddress.getHealthStatus()) && HealthStatus.SUSPECTED.equals(resultHealthStatus)) {
                secondaryAddress.setHealthStatus(HealthStatus.UNHEALTHY);
            } else {
                secondaryAddress.setHealthStatus(resultHealthStatus);
            }
        }
    }


    public HealthStatus getConnectionHealthStatus(String baseUrl) {
        HealthStatus resultHealthStatus;
        int code = 0;
        try {
            String healthUrl = baseUrl + "/health";
            URL siteURL = new URL(healthUrl);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);

            connection.connect();
            code = connection.getResponseCode();
            if (code == 200) {
                resultHealthStatus = HealthStatus.HEALTHY;
            } else {
                resultHealthStatus = HealthStatus.SUSPECTED;
            }
        } catch (Exception e) {
            resultHealthStatus = HealthStatus.UNHEALTHY;
        }
        log.info("S--> PingServiceImpl: Health check. Address=" + baseUrl + " Health status=" + resultHealthStatus);
        return resultHealthStatus;
    }

    @Override
    public boolean hasQuorum() {
        Set<Address> allSecondaryAddresses = addressService.getAllSecondaryAddresses();
        if (allSecondaryAddresses.size() < quorum - 1) {
            return false;
        }
        int healthSecondariesNumber = 0;

        for (Address secondaryAddress : allSecondaryAddresses) {
            if (!HealthStatus.UNHEALTHY.equals(secondaryAddress.getHealthStatus())) {
                healthSecondariesNumber++;
            }
        }
        return healthSecondariesNumber >= quorum - 1;
    }

    public void startPing() {
        if (!runPing) {
            runPing = true;
            log.info("S--> PingServiceImpl: Start ping");
            runPing();
        }
    }

    private void sleepMethod(int sleepMillis) {
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
