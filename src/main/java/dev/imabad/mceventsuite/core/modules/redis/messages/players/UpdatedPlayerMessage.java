package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class UpdatedPlayerMessage extends RedisBaseMessage {

    private UUID player;

    public UpdatedPlayerMessage(UUID player) {
        this.player = player;
    }

    public UUID getUUID() {
        return player;
    }

}
