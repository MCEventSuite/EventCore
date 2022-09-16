package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class BooleanResponse extends RedisBaseMessage {
    private boolean value;

    public BooleanResponse(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }
}
