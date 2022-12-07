package com.replicated_log.master_server.ping;

import com.replicated_log.master_server.address.HealthStatus;

public interface PingService {

    void startPing();

    void runPing();

    void ping();

    HealthStatus getConnectionHealthStatus(String url);

    boolean hasQuorum();
}
