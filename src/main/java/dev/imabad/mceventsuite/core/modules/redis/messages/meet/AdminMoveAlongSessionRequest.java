package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminMoveAlongSessionRequest extends RedisBaseMessage {
    private String name;

    public AdminMoveAlongSessionRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
