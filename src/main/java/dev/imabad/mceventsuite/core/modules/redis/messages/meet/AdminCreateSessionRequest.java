package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminCreateSessionRequest extends RedisBaseMessage {
    private String name;
    private String displayName;
    private int meetTime;
    private int sessionTime;

    public AdminCreateSessionRequest(String name, String displayName, int meetTime, int sessionTime) {
        this.name = name;
        this.displayName = displayName;
        this.meetTime = meetTime;
        this.sessionTime = sessionTime;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getMeetTime() {
        return this.meetTime;
    }

    public int getSessionTime() {
        return this.sessionTime;
    }
}
