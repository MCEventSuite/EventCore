package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class AwardPlayerXPMessage extends RedisBaseMessage {

    private UUID uuid;
    private int amount;
    private String reason;

    public AwardPlayerXPMessage(UUID uuid, int amount, String reason) {
        this.uuid = uuid;
        this.amount = amount;
        this.reason = reason;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }
}
