package com.replicated_log.secondary_server_2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ack {
    private String serverName;
    private AckStatusCode ackStatusCode;
}
