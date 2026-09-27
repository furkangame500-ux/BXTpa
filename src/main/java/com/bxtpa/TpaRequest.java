package com.bxtpa;

import java.util.UUID;

public class TpaRequest {

    private final UUID sender;
    private final UUID target;
    private final boolean tphere; // true = sender wants target to come to them
    private final long createdAt;

    public TpaRequest(UUID sender, UUID target, boolean tphere) {
        this.sender = sender;
        this.target = target;
        this.tphere = tphere;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getTarget() {
        return target;
    }

    public boolean isTphere() {
        return tphere;
    }

    public boolean isExpired(int expireSeconds) {
        return System.currentTimeMillis() - createdAt > expireSeconds * 1000L;
    }
}
