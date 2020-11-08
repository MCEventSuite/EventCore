package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class DisconnectPlayerMessage extends RedisBaseMessage {

    private UUID uuid;
    private String reason;

    public DisconnectPlayerMessage(UUID uuid, String reason) {
        this.uuid = uuid;
        this.reason = reason;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getReason() {
        return reason;
    }
}
