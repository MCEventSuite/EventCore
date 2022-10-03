package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminPauseSessionRequest extends RedisBaseMessage {
    private String name;
    private boolean resume;

    public AdminPauseSessionRequest(String name, boolean resume) {
        this.name = name;
        this.resume = resume;
    }

    public String getName() {
        return this.name;
    }

    public boolean isResume() {
        return this.resume;
    }
}
