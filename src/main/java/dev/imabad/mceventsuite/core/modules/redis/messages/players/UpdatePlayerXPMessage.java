package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class UpdatePlayerXPMessage extends RedisBaseMessage {

    private UUID uuid;
    private int newLevel;

    public UpdatePlayerXPMessage(UUID uuid, int newLevel) {
        this.uuid = uuid;
        this.newLevel = newLevel;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getNewLevel() {
        return newLevel;
    }
}
