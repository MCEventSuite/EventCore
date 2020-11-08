package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class ChangePlayerServerMessage extends RedisBaseMessage {

    private UUID uuid;
    private String serverName;

    public ChangePlayerServerMessage(UUID uuid, String serverName) {
        this.uuid = uuid;
        this.serverName = serverName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServerName() {
        return serverName;
    }
}
