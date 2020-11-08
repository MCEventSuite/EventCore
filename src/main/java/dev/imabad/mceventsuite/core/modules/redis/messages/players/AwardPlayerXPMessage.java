package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class AwardPlayerXPMessage extends RedisBaseMessage {

    private UUID uuid;
    private int amount;

    public AwardPlayerXPMessage(UUID uuid, int amount) {
        this.uuid = uuid;
        this.amount = amount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getAmount() {
        return amount;
    }
}
