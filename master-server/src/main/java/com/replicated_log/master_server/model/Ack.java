package com.replicated_log.master_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ack implements Comparable<Ack> {

    private Integer id;
    private String serverAddress;
    private Integer itemId;
    private AckStatusCode ackStatusCode;

    public Ack(String serverAddress, Integer itemId, AckStatusCode ackStatusCode) {
        this.serverAddress = serverAddress;
        this.itemId = itemId;
        this.ackStatusCode = ackStatusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ack ack = (Ack) o;
        return serverAddress.equals(ack.serverAddress) && itemId.equals(ack.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverAddress, itemId);
    }

    @Override
    public int compareTo(Ack o) {
        if (this.getItemId().equals(o.getItemId())) {
            return this.getServerAddress().compareTo(o.getServerAddress());
        }
        return this.getItemId() - o.getItemId();
    }
}
