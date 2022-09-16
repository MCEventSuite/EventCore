package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class ServerAnnounceMeetState extends RedisBaseMessage {
    private String name;
    private boolean started;
    private long ends;

    public ServerAnnounceMeetState(String name, boolean started, long ends) {
        this.name = name;
        this.started = started;
        this.ends = ends;
    }

    public String getName() {
        return this.name;
    }

    public boolean isStarted() {
        return this.started;
    }

    public long getEnds() {
        return this.ends;
    }
}
