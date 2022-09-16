package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminDeleteSpotRequest extends RedisBaseMessage {
    private String name;
    private int position;

    public AdminDeleteSpotRequest(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public int getPosition() {
        return this.position;
    }
}
