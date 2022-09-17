package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class PlayerLeaveVenueMessage extends RedisBaseMessage {
    private UUID uuid;

    public PlayerLeaveVenueMessage(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
