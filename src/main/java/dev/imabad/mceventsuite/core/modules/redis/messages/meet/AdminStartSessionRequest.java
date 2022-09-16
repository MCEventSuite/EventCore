package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminStartSessionRequest extends RedisBaseMessage {
    private String name;

    public AdminStartSessionRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
