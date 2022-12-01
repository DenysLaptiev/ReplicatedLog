package com.replicated_log.master_server.ack;

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
    private String serverName;
    private Integer itemId;
    private AckStatusCode ackStatusCode;

    public Ack(String serverName, Integer itemId, AckStatusCode ackStatusCode) {
        this.serverName = serverName;
        this.itemId = itemId;
        this.ackStatusCode = ackStatusCode;
    }

    @Override
    public int compareTo(Ack o) {
        return this.getId() - o.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ack ack = (Ack) o;
        return id.equals(ack.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
