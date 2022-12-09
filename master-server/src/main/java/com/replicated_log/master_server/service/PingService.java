package com.replicated_log.master_server.service;

import com.replicated_log.master_server.model.HealthStatus;

public interface PingService {

    void startPing();

    void runPing();

    void ping();

    HealthStatus getConnectionHealthStatus(String url);

    boolean hasQuorum();
}
