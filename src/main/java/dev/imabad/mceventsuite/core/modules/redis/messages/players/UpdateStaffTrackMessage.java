package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class UpdateStaffTrackMessage extends RedisBaseMessage {

    private UUID player;
    private int addMinutes;

    public UpdateStaffTrackMessage(UUID player, int addMinutes) {
        this.player = player;
        this.addMinutes = addMinutes;
    }

    public UUID getUUID() {
        return player;
    }

    public int getAddMinutes() {
        return addMinutes;
    }
}
