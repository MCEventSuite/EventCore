package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class PlayerJoinQueueRequest extends RedisBaseMessage {
    private String name;
    private UUID uuid;

    public PlayerJoinQueueRequest(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
