package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class PlayerTimeReminderMessage extends RedisBaseMessage {
    private UUID uuid;
    private String timeString;

    public PlayerTimeReminderMessage(UUID uuid, String timeString) {
        this.uuid = uuid;
        this.timeString = timeString;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getTimeString() {
        return this.timeString;
    }
}
