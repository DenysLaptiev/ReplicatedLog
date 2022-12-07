package com.replicated_log.master_server.ping;

import com.replicated_log.master_server.address.Address;
import com.replicated_log.master_server.address.AddressService;
import com.replicated_log.master_server.address.HealthStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

@Service
public class PingServiceImpl implements PingService {

    private final Logger LOG = LogManager.getLogger(PingServiceImpl.class);

    @Value("${server.quorum}")
    private Integer QUORUM;

    private static boolean runPing = false;

    @Autowired
    private AddressService addressService;

    @Override
    public void runPing() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runPing) {
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


    public HealthStatus getConnectionHealthStatus(String url) {
        HealthStatus resultHealthStatus = HealthStatus.HEALTHY;
        int code = 0;
        try {
            url = url + "/health";
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //TODO:check this
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
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
        LOG.info("resultHealthStatus=" + resultHealthStatus);
        return resultHealthStatus;
    }

    @Override
    public boolean hasQuorum() {
        Set<Address> allSecondaryAddresses = addressService.getAllSecondaryAddresses();
        if (allSecondaryAddresses.size() < QUORUM - 1) {
            return false;
        }
        int healthSecondariesNumber = 0;

        for (Address secondaryAddress : allSecondaryAddresses) {
            if (!HealthStatus.UNHEALTHY.equals(secondaryAddress.getHealthStatus())) {
                healthSecondariesNumber++;
            }
        }

        return healthSecondariesNumber >= QUORUM - 1;
    }

    public void startPing() {
        if (!runPing) {
            runPing = true;
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
