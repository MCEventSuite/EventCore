package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.List;
import java.util.UUID;

public class UpdateStaffTrackMessage extends RedisBaseMessage {

    private List<UUID> players;

    public UpdateStaffTrackMessage(List<UUID> players) {
        this.players = players;
    }

    public List<UUID> getPlayers() {
        return players;
    }
}
