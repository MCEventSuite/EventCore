package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class StringMessage extends RedisBaseMessage {
    private final String message;

    public StringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
