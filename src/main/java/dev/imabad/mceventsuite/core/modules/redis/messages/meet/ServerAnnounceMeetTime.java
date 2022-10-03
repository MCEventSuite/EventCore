package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;
import dev.imabad.mceventsuite.core.modules.servers.objects.Server;

public class ServerAnnounceMeetTime extends RedisBaseMessage {
    private String name;
    private long start;
    private long end;

    public ServerAnnounceMeetTime(String name, long start, long end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return this.name;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }
}
