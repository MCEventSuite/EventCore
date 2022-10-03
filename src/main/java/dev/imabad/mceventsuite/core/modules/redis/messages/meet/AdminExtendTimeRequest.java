package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminExtendTimeRequest extends RedisBaseMessage {
    private String name;
    private int time;
    private boolean session;

    public AdminExtendTimeRequest(String name, int time, boolean session) {
        this.name = name;
        this.time = time;
        this.session = session;
    }

    public String getName() {
        return this.name;
    }

    public int getTime() {
        return this.time;
    }

    public boolean isSession() {
        return this.session;
    }
}
